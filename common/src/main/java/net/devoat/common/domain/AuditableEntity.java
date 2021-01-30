package net.devoat.common.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AuditableEntity<ID extends Serializable> implements IdentifiableEntity<ID> {

	@Column(name="ADD_DATE")
	private Date addDate;
	@Column(name="ADD_USER")
	private String addUser;
	@Column(name="UPDATE_DATE")
	private Date updateDate;
	@Column(name="UPDATE_USER")
	private String updateUser;
	public Date getAddDate() {
		return addDate;
	}
	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	public String getAddUser() {
		return addUser;
	}
	public void setAddUser(String addUser) {
		this.addUser = addUser;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	
}
