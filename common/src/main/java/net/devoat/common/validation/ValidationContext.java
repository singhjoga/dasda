package net.devoat.common.validation;

import java.util.HashMap;
import java.util.Map;

public class ValidationContext {
	private static ThreadLocal<ValidationContext> instance = new ThreadLocal<ValidationContext>() {
		@Override
		protected ValidationContext initialValue() {
			return new ValidationContext();
		}
		
	};

	public static void reset() {
		instance.get().clear();
	}


	public static ValidationContext getInstance() {
		return instance.get();
	}
	private Map<String, Object> properties;
	private boolean isUpdate = false;
	public ValidationContext() {
		properties = new HashMap<String, Object>();
	}

	public void clear() {
		properties.clear();
	}
	public Map<String, Object> getProperties() {
		return properties;
	}


	public boolean isUpdate() {
		return isUpdate;
	}


	public void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

}
