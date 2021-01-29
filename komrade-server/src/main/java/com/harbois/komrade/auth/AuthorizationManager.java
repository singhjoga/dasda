package com.harbois.komrade.auth;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.harbois.komrade.auth.domain.EntityAction;
import com.harbois.komrade.auth.domain.EntityPermission;
import com.harbois.komrade.auth.domain.EntityPermissions;
import com.harbois.komrade.auth.domain.RolePermissionEntity;
import com.harbois.komrade.auth.domain.UserPermission;
import com.harbois.komrade.auth.domain.ÊntityDefinition;
import com.harbois.komrade.auth.parsing.EntityActionParser;
import com.harbois.komrade.auth.parsing.ParseException;
import com.harbois.komrade.auth.parsing.RolePermissionParser;
import com.harbois.oauth.api.v1.settings.AppAuthPermissionRepository;
import com.thetechnovator.common.java.StringProperties;

@Component
public class AuthorizationManager {
	private static String ENTITY_ACtIONS_PROP="entity-actions";
	private static String ROLE_PERMISSIONS_PROP="role-permissions";
	@Autowired
	private AppAuthPermissionRepository permRepo;
	
	// permissions for each role
	private Map<String, RolePermissionEntity> rolePermissions = new TreeMap<String, RolePermissionEntity>();

	// permission instances for each user
	private Map<String, UserPermission> userPermissions = new TreeMap<>();

	@PostConstruct
	public void init() {
		refresh();
	}

	public void refresh() {
		String entityActions = getEntityActionSettings();
		String roleActions = getRolePermissionSettings();
		
		EntityActionParser parser = new EntityActionParser();
		List<ÊntityDefinition> entities;
		try {
			entities = parser.parse(new ByteArrayInputStream(entityActions.getBytes()));
			RolePermissionParser roleParser = new RolePermissionParser();
			Map<String, RolePermissionEntity> parsedRolePermissions = roleParser.parse(new ByteArrayInputStream(roleActions.getBytes()), entities);
			// TODO: Concurrency
			rolePermissions.clear();
			rolePermissions.putAll(parsedRolePermissions);
		} catch (IOException | ParseException e) {
			throw new IllegalStateException("Error parsing role permissions: " + e.getMessage(), e);
		}
	}
	private void refreshUserPermissions() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			return;
		}
		String username = auth.getName();
		Set<String> roles = new TreeSet<>();
		for (GrantedAuthority role : auth.getAuthorities()) {
			roles.add(role.getAuthority());
		}
		refreshUserPermissions(username, roles);
	}
	public void refreshUserPermissions(String username, Set<String> roles) {
		EntityPermissions allermissions = getRolePermissions(roles);
		UserPermission userPerm = new UserPermission();
		userPerm.setUsername(username);
		userPerm.setEntityPermissions(allermissions);
		userPermissions.remove(username);
		userPermissions.put(username, userPerm);
	}
	public EntityPermissions getRolePermissions(Set<String> roles) {
		EntityPermissions result = new EntityPermissions();
		for (String roleName : roles) {
			if (!rolePermissions.containsKey(roleName)) {
				continue;
			}
			RolePermissionEntity rolePerm = rolePermissions.get(roleName);
			for (String entityName: rolePerm.getAllowedEntities().keySet()) {
				EntityPermission src = rolePerm.getAllowedEntities().get(entityName);
				EntityPermission dest = result.get(entityName);
				if (dest==null) {
					dest = new EntityPermission();
					dest.setEntityName(entityName);
					result.put(entityName, dest);
				}
				AuthUtil.merge(src, dest); //copy permissions
			}
		}
		
		return result;
	}
	public boolean hasPermission(String entity, String action, StringProperties constraintVariables) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			return false;
		}
		return hasPermission(auth.getName(), entity, action, constraintVariables);
	}
	public boolean hasPermission(String username, String entity, String action, StringProperties constraintVariables) {
		if (!userPermissions.containsKey(username)) {
			refreshUserPermissions();
		}
		UserPermission userPerm = userPermissions.get(username);
		EntityPermission entityPerm=userPerm.getEntityPermissions().get(entity);
		if (entityPerm==null) {
			//no permission for the entity
			return false;
		}
		EntityAction entityAction = entityPerm.getActionsMap().get(action);
		if (entityAction==null) {
			//no permission for the action
			return false;
		}
		//check if the constraints are satisfied
		StringProperties constraints = entityAction.getConstraintObj();
		if (!constraints.isEmpty()) {
			if (constraintVariables==null) {
				throw new IllegalArgumentException("Constraint variables parameter must be provided");
			}
		}
		for (String constraint: constraints.keySet()) {
			//check if the constraint variable is provided
			if (!constraintVariables.containsKey(constraint)) {
				throw new IllegalArgumentException("Required constraint variable not provided: "+constraint);
			}
			String userValue = constraintVariables.get(constraint);
			String requiredValueStr=constraints.get(constraint);
			//required value could be comma delimited list
			String[] requiredValues = StringUtils.split(requiredValueStr,",");
			boolean found=false;
			for (String requiredValue: requiredValues) {
				boolean startAny=requiredValue.startsWith("*");
				boolean endAny=requiredValue.endsWith("*");
				if (startAny && endAny) {
					String matchValue = StringUtils.substringBetween(requiredValue, "*");
					found = userValue.contains(matchValue);
				}else if (startAny) {
					String matchValue = StringUtils.substringAfter(requiredValue, "*");
					found = userValue.endsWith(matchValue);
				}else if (endAny) {
					String matchValue = StringUtils.substringBefore(requiredValue, "*");
					found = userValue.startsWith(matchValue);
				}else {
					found=userValue.equals(requiredValue);
				}
				if (found) {
					break;
				}
			}
			if (!found) {
				return false;
			}
		}
		
		return true;
	}
	public EntityPermissions getUserPermissions() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			return null;
		}
		String username=auth.getName();
		if (!userPermissions.containsKey(username)) {
			refreshUserPermissions();
		}
		UserPermission userPerm = userPermissions.get(username);
		
		return userPerm.getEntityPermissions();
	}
	public EntityPermissions getRolePermissions(String role) {

		if (!rolePermissions.containsKey(role)) {
			return new EntityPermissions();
		}
		return rolePermissions.get(role).getAllowedEntities();
	}
	public String getEntityActionSettings() {
		return permRepo.getPropertyValue(ENTITY_ACtIONS_PROP);
	}
	public String getRolePermissionSettings() {
		return permRepo.getPropertyValue(ROLE_PERMISSIONS_PROP);
	}

	public void saveRolePermissionSettings(String value) {
		permRepo.setPropertyValue(ROLE_PERMISSIONS_PROP,value);
		refresh();
		userPermissions.clear(); //will be reloaded
	}

}
