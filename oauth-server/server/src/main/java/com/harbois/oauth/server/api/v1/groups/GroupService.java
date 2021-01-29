package com.harbois.oauth.server.api.v1.groups;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.harbois.oauth.server.api.v1.common.AbstractService;
import com.harbois.oauth.server.api.v1.groups.GroupRole.GroupRoleId;
import com.harbois.oauth.server.api.v1.groups.GroupUser.GroupUserId;
import com.harbois.oauth.server.api.v1.roles.Role;
import com.harbois.oauth.server.api.v1.roles.RoleService;
import com.harbois.oauth.server.api.v1.users.User;
import com.harbois.oauth.server.api.v1.users.UserService;

@Component
public class GroupService extends AbstractService<Group, Long>{

	private GroupRepository repo;
	
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userService;
	@Autowired
	private GroupRoleRepository roleRepo;
	@Autowired
	private GroupUserRepository userRepo;
	
	@Autowired
	public GroupService(GroupRepository repo) {
		super(repo,Group.class, Long.class);
		this.repo=repo;
	}
	public List<Role> getRoleList(Long id) {
		return roleService.findByGroupId(id);
	}
	public List<User> getUserList(Long id) {
		return userService.findByGroupId(id);
	}	
	public List<Group> findByRoleId(Long roleId) {
		return repo.findByRoleId(roleId);
	}
	public List<Group> findByUsername(String username, String clientId) {
		return repo.findByUsername(username, clientId);
	}
	public void attachRoles(Long id, Long[] roleIds) {
		List<GroupRole> existing = roleRepo.findByGroupId(id);
		Set<Long> existingRoleIds = existing.stream().map(GroupRole::getRoleId).collect(Collectors.toSet());
		List<GroupRole> newGroupRoles = new ArrayList<>();
		for (Long roleId: roleIds) {
			if (!existingRoleIds.contains(roleId)) {
				GroupRole groupRole = new GroupRole();
				groupRole.setId(new GroupRoleId(id, roleId));
				newGroupRoles.add(groupRole);
			}
		}
		
		roleRepo.saveAll(newGroupRoles);
	}
	
	public void detachRoles(Long id, Long[] roleIds) {
		List<GroupRole> roles = new ArrayList<>();
		for (Long roleId: roleIds) {
			GroupRole groupRole = new GroupRole();
			groupRole.setId(new GroupRoleId(id, roleId));
			roles.add(groupRole);
		}
		
		roleRepo.deleteAll(roles);
		
	}
	public void attachUsers(Long id, String[] usernames) {
		List<GroupUser> existing = userRepo.findByGroupId(id);
		Set<String> existingUsers = existing.stream().map(GroupUser::getUsername).collect(Collectors.toSet());
		List<GroupUser> newGroupUsers = new ArrayList<>();
		for (String username: usernames) {
			if (!existingUsers.contains(username)) {
				GroupUser groupUser = new GroupUser();
				groupUser.setId(new GroupUserId(id, username));
				newGroupUsers.add(groupUser);
			}
		}
		
		userRepo.saveAll(newGroupUsers);
	}
	public void detachUsers(Long id, String[] usernames) {
		List<GroupUser> users = new ArrayList<>();
		for (String username: usernames) {
			GroupUser groupRole = new GroupUser();
			groupRole.setId(new GroupUserId(id, username));
			users.add(groupRole);
		}
		
		userRepo.deleteAll(users);
		
	}
}
