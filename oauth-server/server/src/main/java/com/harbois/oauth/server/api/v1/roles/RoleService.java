package com.harbois.oauth.server.api.v1.roles;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.harbois.oauth.server.api.v1.common.AbstractService;
import com.harbois.oauth.server.api.v1.groups.Group;
import com.harbois.oauth.server.api.v1.groups.GroupRole;
import com.harbois.oauth.server.api.v1.groups.GroupRoleRepository;
import com.harbois.oauth.server.api.v1.groups.GroupService;
import com.harbois.oauth.server.api.v1.groups.GroupRole.GroupRoleId;

@Component
public class RoleService extends AbstractService<Role, Long>{

	private RoleRepository repo;
	@Autowired
	private GroupService groupService;
	@Autowired
	private GroupRoleRepository groupRoleRepo;
	
	@Autowired
	public RoleService(RoleRepository repo) {
		super(repo,Role.class, Long.class);
		this.repo=repo;
	}
	public List<Role> findByGroupId(Long groupId) {
		return repo.findByGroupId(groupId);
	}
	public List<Group> getGroupList(Long id) {
		return groupService.findByRoleId(id);
	}
	public List<Role> findByUsername(String username, String clientId) {
		return repo.findByUsername(username, clientId);
	}
	public List<Role> findSystemRoles(String username, String clientId) {
		return repo.findSystemRoles(username, clientId);
	}
	public void attachToGroups(Long id, Long[] groupIds) {
		List<GroupRole> existing = groupRoleRepo.findByRoleId(id);
		Set<Long> existingGroupIds = existing.stream().map(GroupRole::getGroupId).collect(Collectors.toSet());
		List<GroupRole> newGroupRoles = new ArrayList<>();
		for (Long groupId: groupIds) {
			if (!existingGroupIds.contains(groupId)) {
				GroupRole groupRole = new GroupRole();
				groupRole.setId(new GroupRoleId(groupId,id));
				newGroupRoles.add(groupRole);
			}
		}
		
		groupRoleRepo.saveAll(newGroupRoles);
	}
	
	public void detachFromGroups(Long id, Long[] groupIds) {
		List<GroupRole> roles = new ArrayList<>();
		for (Long groupId: groupIds) {
			GroupRole groupRole = new GroupRole();
			groupRole.setId(new GroupRoleId(groupId, id));
			roles.add(groupRole);
		}
		
		groupRoleRepo.deleteAll(roles);
		
	}	
}
