package com.harbois.oauth.server.api.v1.groups;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="USER_GROUP_USER")
public class GroupUser{

	@Id
	@EmbeddedId
	private GroupUserId id;
	
	public GroupUserId getId() {
		return id;
	}

	public void setId(GroupUserId id) {
		this.id = id;
	}
	public Long getGroupId() {
		return id.groupId;
	}
	public String getUsername() {
		return id.username;
	}
	
	@Embeddable
	public static class GroupUserId implements Serializable{
		private static final long serialVersionUID = 1L;
		@Column(name="USER_GROUP_ID")
		private Long groupId;
		@Column(name="USERNAME")
		private String username;
		public GroupUserId() {
			super();
		}
		public GroupUserId(Long groupId, String username) {
			super();
			this.groupId = groupId;
			this.username = username;
		}

	}
}
