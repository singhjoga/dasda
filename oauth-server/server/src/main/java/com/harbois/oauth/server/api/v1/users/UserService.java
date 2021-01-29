package com.harbois.oauth.server.api.v1.users;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.harbois.oauth.server.api.v1.clients.OAuthClient;
import com.harbois.oauth.server.api.v1.common.AbstractService;
import com.harbois.oauth.server.api.v1.common.UserContext;
import com.harbois.oauth.server.api.v1.groups.Group;
import com.harbois.oauth.server.api.v1.groups.GroupService;
import com.harbois.oauth.server.api.v1.groups.GroupUser;
import com.harbois.oauth.server.api.v1.groups.GroupUser.GroupUserId;
import com.harbois.oauth.server.api.v1.groups.GroupUserRepository;

@Component
public class UserService extends AbstractService<User, String>{
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private GroupUserRepository groupUserRepo;
	@Autowired
	private GroupService groupService;	
	private UserRepository repo;
	@Autowired
	public UserService(UserRepository repo) {
		super(repo,User.class, String.class);
		this.repo=repo;
	}
	@Override
	protected void beforeSave(User newObj, boolean isAdd) {
		//if add, encode the secret
		if (isAdd) {
			newObj.setPassword(passwordEncoder.encode(newObj.getPassword()));
		}
	}

	public List<User> findByGroupId(Long groupId) {
		return repo.findByGroupId(groupId);
	}
	public List<Group> getGroupList(String username) {
		return groupService.findByUsername(username, UserContext.getInstance().getClientId());
	}
	public void attachToGroups(String username, Long[] groupIds) {
		List<GroupUser> existing = groupUserRepo.findByUsername(username);
		Set<Long> existingGroupIds = existing.stream().map(GroupUser::getGroupId).collect(Collectors.toSet());
		List<GroupUser> newGroupUsers = new ArrayList<>();
		for (Long groupId: groupIds) {
			if (!existingGroupIds.contains(groupId)) {
				GroupUser groupUser = new GroupUser();
				groupUser.setId(new GroupUserId(groupId,username));
				newGroupUsers.add(groupUser);
			}
		}
		
		groupUserRepo.saveAll(newGroupUsers);
	}
	
	public void detachFromGroups(String username, Long[] groupIds) {
		List<GroupUser> users = new ArrayList<>();
		for (Long groupId: groupIds) {
			GroupUser groupUser = new GroupUser();
			groupUser.setId(new GroupUserId(groupId, username));
			users.add(groupUser);
		}
		
		groupUserRepo.deleteAll(users);
		
	}
	public void updatePassword(String id, String newPassword) {
		User obj = getById(id);
		obj.setPassword(passwordEncoder.encode(newPassword));
		save(obj);
	}	
}
