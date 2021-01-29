package com.harbois.komrade.v1.globalvars;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.harbois.common.auditing.Auditable;
import com.harbois.komrade.constants.Entities;
import com.harbois.komrade.v1.common.properties.PersistentPropertyEnvBased;
import com.harbois.oauth.api.v1.common.Views;
import com.harbois.oauth.api.v1.common.domain.Deactivateable;

@Entity(name="GLOBAL_VAR")
@AttributeOverride(name="id",column=@Column(name="GLOBAL_VAR_ID"))
public class GlobalVariable extends PersistentPropertyEnvBased<GlobalVariable, GlobalVariableValue> implements Auditable<String>,Deactivateable{
	@Column(name="IS_DISABLED")
	@JsonView(value= {Views.List.class,Views.Update.class})
	private Boolean isDisabled;	

	@Override
	public GlobalVariable copyTo(GlobalVariable copyTo) {
		copyTo.setIsDisabled(isDisabled);
		return super.copyTo(copyTo);
	}
	@Override
	public GlobalVariable create() {
		return new GlobalVariable();
	}
	public Boolean getIsDisabled() {
		return isDisabled;
	}
	public void setIsDisabled(Boolean isDisabled) {
		this.isDisabled = isDisabled;
	}
	@Override
	public String getObjectType() {
		return Entities.GLOBAL_VARIABLE;
	}

}
