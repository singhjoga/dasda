package com.harbois.komrade.v1.component.version.properties;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;

import com.harbois.komrade.v1.common.properties.PersistentPropertyEnvBasedValue;

@Entity(name="COMP_VER_PROP_VAL")
@AttributeOverrides({
	@AttributeOverride(name="id",column=@Column(name="COMP_VER_PROP_VAL_ID")),
	@AttributeOverride(name="propertyId",column=@Column(name="COMP_VER_PROP_ID"))
})
public class ComponentVersionPropertyValue extends PersistentPropertyEnvBasedValue<ComponentVersionPropertyValue>{
	@Override
	public ComponentVersionPropertyValue create() {
		return new ComponentVersionPropertyValue();
	}
}
