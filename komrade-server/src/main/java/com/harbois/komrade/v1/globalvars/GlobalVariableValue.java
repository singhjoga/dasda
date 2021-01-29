package com.harbois.komrade.v1.globalvars;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;

import com.harbois.komrade.v1.common.properties.PersistentPropertyEnvBasedValue;

@Entity(name="GLOBAL_VAR_VAL")
@AttributeOverrides({
	@AttributeOverride(name="id",column=@Column(name="GLOBAL_VAR_VAL_ID")),
	@AttributeOverride(name="propertyId",column=@Column(name="GLOBAL_VAR_ID"))
})
public class GlobalVariableValue extends PersistentPropertyEnvBasedValue<GlobalVariableValue>{
	@Override
	public GlobalVariableValue create() {
		return new GlobalVariableValue();
	}
}
