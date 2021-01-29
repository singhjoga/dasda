package com.harbois.komrade.v1.system.files;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonView;
import com.harbois.common.auditing.Auditable;
import com.harbois.common.validation.annotations.NotNullable;
import com.harbois.common.validation.annotations.UpdatePolicy;
import com.harbois.komrade.constants.Entities;
import com.harbois.oauth.api.v1.common.Views;
import com.harbois.oauth.api.v1.common.domain.BaseAuditableEntity;
import com.harbois.oauth.api.v1.common.domain.FunctionalId;

@Entity(name="SYS_FILE")
public class SystemFile extends BaseAuditableEntity implements Auditable<String>, FunctionalId{
	@Id
	@Column(name="FILE_NAME")
	@JsonView(value= {Views.Add.class,Views.List.class})
	@UpdatePolicy(updateable=false)
	@NotNullable
	private String fileName;
	@NotNullable
	@Column(name="DESCRIPTION")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	private String description;
	@NotNullable	
	@Column(name="FILE_TYPE")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	private String type;
	@Column(name="FILE_SIZE")
	@JsonView(value= {Views.List.class})
	private Long size;
	
	@Column(name="CONTENTS")
	@NotNullable
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.View.class})
	private String contents;

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	@Override
	public String getObjectType() {
		return Entities.SYSTEM_FILE;
	}
	@Override
	public void setId(String id) {
		this.fileName=id;
	}
	@Override
	public String getId() {
		return fileName;
	}
	@Override
	public String getName() {
		return fileName;
	}
}
