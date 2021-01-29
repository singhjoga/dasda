package com.harbois.oauth.api.v1.groups;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="USER_GROUP_ROLE")
public class GroupRole{

	@Id
	@EmbeddedId
	private GroupRoleId id;
	
	public GroupRoleId getId() {
		return id;
	}

	public void setId(GroupRoleId id) {
		this.id = id;
	}
	public Long getGroupId() {
		return id.groupId;
	}
	public Long getRoleId() {
		return id.roleId;
	}
	
	@Embeddable
	public static class GroupRoleId implements Serializable{
		private static final long serialVersionUID = 1L;
		@Column(name="USER_GROUP_ID")
		private Long groupId;
		@Column(name="USER_ROLE_ID")
		private Long roleId;
		public GroupRoleId() {
			super();
		}
		public GroupRoleId(Long groupId, Long roleId) {
			super();
			this.groupId = groupId;
			this.roleId = roleId;
		}
		
	}
}
