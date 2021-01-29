package com.harbois.komrade.v1.credentials;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.harbois.komrade.v1.common.properties.PersistentPropertyEnvBased;
import com.harbois.komrade.v1.common.properties.PersistentPropertyEnvBasedValue;
import com.harbois.komrade.v1.credentials.provider.ProviderCredentialsType;


public class CredentialsRequestBuilder<P extends PersistentPropertyEnvBased<P,V>, V extends PersistentPropertyEnvBasedValue<V>> {
	private static final String EXPRESSION_LOWER_ENV_CODE = "${LOWER_ENV_CODE}";
	private static final String EXPRESSION_ENV_CODE = "${ENV_CODE}";	
	private Map<String, CredentialsRequest> requestList = new TreeMap<>();
	private List<PersistentPropertyEnvBased<P,V>> credProps2 = new ArrayList<PersistentPropertyEnvBased<P,V>>();
	public void addRequest(String key, ProviderCredentialsType type, String envCode) {
		CredentialsRequest req = requestList.get(key);
		if (req==null) {
			req = new CredentialsRequest();
			requestList.put(key, req);
		}
		req.setKey(key);
		req.setEnvCode(envCode);
		req.getRequiredTypes().add(type);
	}
	public List<CredentialsRequest> build(){
		return new ArrayList<CredentialsRequest>(requestList.values());
	}
	
	public List<PersistentPropertyEnvBased<P,V>> credentailsProperties2() {
		return credProps2;
	}
	public List<CredentialsRequest> buildFromList2(Collection<? extends PersistentPropertyEnvBased<P,V>> properties) {
		
		//build the credentials request
		for (PersistentPropertyEnvBased<P,V> prop : properties) {
			ProviderCredentialsType type = ProviderCredentialsType.forCode(prop.getTypeCode());
			if (type==null || prop.getValues() == null || prop.getValues().isEmpty()) {
				continue;
			}
			boolean credentialsProp=false;
			for (PersistentPropertyEnvBasedValue<V> value: prop.getValues()) {
				if (value == null || StringUtils.isEmpty(value.getValue())) {
					continue;
				}
				String envCode=value.getEnvCode();
				String key = getEnvKeePassTitle(value.getValue(), envCode);
				String keepassEnvCode = envCode;
				/*
				 * All passwords for BASE environment are stored on the SYSTEM keepass file.
				 * Here we have to check environment code to be used. In BASE also environment
				 * specific keepass entry is possible using Expressions.
				 */
				// Check if property value is from BASE and it is a BASE password

				if ((value.getEnvCode().equals("BASE") || value.getIsInherited() )&& key.equals(value.getValue())) {
					keepassEnvCode = "SYSTEM";
				}
				credentialsProp=true;
				addRequest(key, type, keepassEnvCode);				
			}
			if (credentialsProp) {
				credProps2.add(prop);
			}
		}
		
		return build();
	}

	private String getEnvKeePassTitle(String title, String envCode) {
		// envCode can be null for system setting passwords which are not environment
		// specific
		if (envCode == null || title == null)
			return title;

		/*
		 * To avoid defining of un-necessary entries for environments. Title in CMDB can
		 * start with ${LOWER_ENV_CODE} or ${ENV_CODE} variables. Substitute these
		 * variables
		 */
		if (title.startsWith(EXPRESSION_LOWER_ENV_CODE)) {
			return title.replace(EXPRESSION_LOWER_ENV_CODE, envCode.toLowerCase());
		}
		if (title.startsWith(EXPRESSION_ENV_CODE)) {
			return title.replace(EXPRESSION_ENV_CODE, envCode);
		}

		return title;
	}
}
