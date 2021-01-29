package com.harbois.komrade.v1.component.properties;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.harbois.common.auditing.Auditable;
import com.harbois.komrade.constants.Entities;
import com.harbois.komrade.v1.common.properties.PersistentPropertyEnvBased;
import com.harbois.oauth.api.v1.common.Views;

@Entity(name="COMP_PROP")
@AttributeOverride(name="id",column=@Column(name="COMP_PROP_ID"))
public class ComponentProperty extends PersistentPropertyEnvBased<ComponentProperty, ComponentPropertyValue> implements Auditable<String>{
	@Column(name="COMP_ID")
	@JsonView(value= {Views.List.class,Views.Add.class})
	private String componentId;	
	
	@Override
	public ComponentProperty copyTo(ComponentProperty copyTo) {
		return super.copyTo(copyTo);
	}
	
	public String getComponentId() {
		return componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	@Override
	public ComponentProperty create() {
		return new ComponentProperty();
	}

	@Override
	public String getObjectType() {
		return Entities.COMPONENT_PROPERTY;
	}

}
