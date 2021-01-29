package com.harbois.komrade.v1.component.version.properties;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.harbois.common.auditing.Auditable;
import com.harbois.komrade.constants.Entities;
import com.harbois.komrade.v1.common.properties.PersistentPropertyEnvBased;
import com.harbois.oauth.api.v1.common.Views;

@Entity(name="COMP_VER_PROP")
@AttributeOverride(name="id",column=@Column(name="COMP_VER_PROP_ID"))
public class ComponentVersionProperty extends PersistentPropertyEnvBased<ComponentVersionProperty, ComponentVersionPropertyValue> implements Auditable<String>{
	@Column(name="COMP_VER_ID")
	@JsonView(value= {Views.List.class,Views.Add.class})
	private String componentVersionId;	
	
	@Override
	public ComponentVersionProperty copyTo(ComponentVersionProperty copyTo) {
		return super.copyTo(copyTo);
	}
	public String getComponentVersionId() {
		return componentVersionId;
	}
	public void setComponentVersionId(String componentVersionId) {
		this.componentVersionId = componentVersionId;
	}
	@Override
	public ComponentVersionProperty create() {
		return new ComponentVersionProperty();
	}

	@Override
	public String getObjectType() {
		return Entities.COMPONENT_VERSION_PROPERTY;
	}

}
