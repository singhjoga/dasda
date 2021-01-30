package net.devoat.common.auditing;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonView;

import net.devoat.common.Views;
import net.devoat.common.domain.IdentifiableEntity;

@Entity(name = "AUDIT_LOG")
public class AuditLog implements IdentifiableEntity<String>{

	@Id
	@Column(name = "AUDIT_ID")
	@JsonView(value=Views.List.class)
	private String id;
	@Column(name = "ACTION")
	@JsonView(value=Views.List.class)
	private String action;
	@Column(name = "ACTION_DATE")
	@JsonView(value=Views.List.class)
	private Date date;
	@Column(name = "ACTION_USER")
	@JsonView(value=Views.List.class)
	private String user;
	@Column(name = "OBJ_TYPE")
	@JsonView(value=Views.List.class)
	private String objectType;
	@Column(name = "OBJ_NAME")
	@JsonView(value=Views.List.class)
	private String objectName;
	@Column(name = "FILTER_VALUE")
	@JsonView(value=Views.List.class)
	private String filterValue;	
	@Column(name = "OBJ_ID")
	@JsonView(value=Views.List.class)
	private String objectId;
	@Column(name = "ACTION_DETAILS")
	@JsonView(value=Views.List.class)
	private String details;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getFilterValue() {
		return filterValue;
	}
	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}

}