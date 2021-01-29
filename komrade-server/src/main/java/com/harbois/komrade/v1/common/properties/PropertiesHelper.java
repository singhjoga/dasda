package com.harbois.komrade.v1.common.properties;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.harbois.komrade.v1.credentials.CredentialsHandler;
import com.harbois.komrade.v1.credentials.CredentialsRequest;
import com.harbois.komrade.v1.credentials.CredentialsRequestBuilder;
import com.harbois.komrade.v1.credentials.CredentialsResponse;
import com.harbois.komrade.v1.credentials.provider.CredentialsEntry;
import com.harbois.komrade.v1.credentials.provider.CredentialsType;
import com.harbois.komrade.v1.credentials.provider.ProviderCredentialsType;
import com.harbois.oauth.api.v1.common.exception.AccessDeniedException;
import com.harbois.oauth.api.v1.common.exception.TechnicalException;

@Component
public class PropertiesHelper<P extends PersistentPropertyEnvBased<P,V>, V extends PersistentPropertyEnvBasedValue<V>> {
	private static final Logger LOG = LoggerFactory.getLogger(PropertiesHelper.class);
	private static final String EXPRESSION_LOWER_ENV_CODE = "${LOWER_ENV_CODE}";
	private static final String EXPRESSION_ENV_CODE = "${ENV_CODE}";
	
	@Value("${failWhenPropertyReferenceNotFound:true}")
	private boolean failWhenPropertyReferenceNotFound;

	@Value("${access.passwords.key:}")
	private String acessPasswordsKey;
	
	@Autowired
	private CredentialsHandler credentialsHandler;
	public void inheritPropsFromBase(List<V> values, Set<String> requiredEnvs) {
		if (values == null) return;
		Set<String> availableEnvs = new HashSet<String>();
		V baseValue=null;
		int baseIndex=-1;
		boolean baseFound=false;
		for (int i=0;i<values.size();i++) {
			V value = values.get(i);
			if (value.getEnvCode().equals("BASE")) {
				baseValue=value;
				baseFound=true;
				baseIndex=i;
			}
			availableEnvs.add(value.getEnvCode());
		}
		if (!baseFound) {
			//Base value is not available
			return;
		}
		for (String envCode: requiredEnvs) {
			if (envCode.equals("BASE")) continue;
			if (!availableEnvs.contains(envCode)) {
				//value for the env is not available, use the BASE
				baseValue.setEnvCode(envCode);
				baseValue.setIsInherited(true);
				//return as base
			}else {
				//env value exists, delete base
				values.remove(baseIndex);
			}
		}
	}
	public void resolveReferences2(Collection<? extends PersistentPropertyEnvBased<?,?>> globalProperties, Collection<? extends PersistentPropertyEnvBased<?,?>> componentProperties) {
		resolveReferences2(globalProperties, componentProperties, failWhenPropertyReferenceNotFound);
	}
	public void resolveReferences2(Collection<? extends PersistentPropertyEnvBased<?,?>> globalProperties, Collection<? extends PersistentPropertyEnvBased<?,?>> componentProperties, boolean throwErrorWhenRefNotFound) {
		Map<String, PersistentPropertyEnvBased<?,?>> map = new HashMap<>();

		// Add all properties to map
		addProprtiesToMap2(globalProperties, map);
		addProprtiesToMap2(componentProperties, map);

		// Now resolve the references
		for (PersistentPropertyEnvBased<?,?> prop : componentProperties) {
			resolveReference2(prop, map, 0,throwErrorWhenRefNotFound);
		}
	}
	private void resolveReference2(PersistentPropertyEnvBased<?,?> prop, Map<String, PersistentPropertyEnvBased<?,?>> map, int level, boolean throwErrorWhenRefNotFound) {
		if (prop.getValues().isEmpty() || prop.getValues().get(0).getValue()==null) {
			return;
		}
		/*
		 * This procedure resolves multi level references i.e. referenced value is a
		 * reference to another property and so on. However, to avoid cyclic references,
		 * only 10 level of reference levels are allow
		 */

		if (level >= 10) {
			throw new TechnicalException("Property references cannot go deeper than 10 levels for property: " + prop.getName());
		}
		// Search for reference token
		PersistentPropertyEnvBasedValue<?> value = prop.getValues().get(0);
		int refStart = value.getValue().indexOf("${");

		if (refStart == -1) {
			// Property does not have a reference
			return;
		}

		int refEnd = value.getValue().indexOf("}", refStart);

		if (refEnd == -1 || (refEnd - refStart) < 3) { // Minimum one character
			throw new TechnicalException("Invalid propery reference defintion " + prop.getName());
		}
		String prefix = "";
		if (refStart > 0) {
			prefix = value.getValue().substring(0, refStart);
		}
		String suffix = "";
		if (refEnd < (value.getValue().length()) - 1) {
			suffix = value.getValue().substring(refEnd + 1);
		}

		String refPropName = value.getValue().substring(refStart + 2, refEnd);

		PersistentPropertyEnvBased<?,?> refProp = map.get(refPropName);

		if (refProp == null  || refProp.getValues().isEmpty()) {
			if (throwErrorWhenRefNotFound) {
				throw new TechnicalException("Refrenced property " + refPropName + " not found for " + prop.getName());
			} else {
				LOG.debug("Property reference not found " + refPropName);
				return;
			}
		}
		String refValue = refProp.getValues().get(0).getValue();
		if (refValue == null) {
			refValue = "";
		}
		// Check if the property value is a reference to another property
		if (refValue.indexOf("${") != -1) {
			resolveReference2(refProp, map, level + 1,throwErrorWhenRefNotFound);
		}

		String newValue = prefix + refValue + suffix;
		value.setValue(newValue);
		// Resolve again. May be there are multiple references in the same property
		resolveReference2(prop, map, level + 1,throwErrorWhenRefNotFound);
	}

	
	private void addProprtiesToMap2(Collection<? extends PersistentPropertyEnvBased<?,?>> properties, Map<String, PersistentPropertyEnvBased<?,?>> map) {
		if (properties != null) {
			for (PersistentPropertyEnvBased<?,?> prop : properties) {
				if (!map.containsKey(prop.getName())) {
					map.put(prop.getName(), prop);
				}
			}
		}
	}
	public void setKeypassPasswords2(Collection<? extends PersistentPropertyEnvBased<P,V>> properties, String keypassPassword, String envCode) {
		setKeypassPasswords2(properties, keypassPassword, envCode, true);
	}

	public void setKeypassPasswords2(Collection<? extends PersistentPropertyEnvBased<P,V>> properties, String keypassPassword, String envCode,
			boolean errorOnMissingKeePassEntry) {
		validateAccessPassword(keypassPassword);
		validateAndSetCredentialsResponse2(properties, errorOnMissingKeePassEntry, true);
	}
	private void validateAccessPassword(String password) {
		if (!acessPasswordsKey.equals(password)) {
			throw new AccessDeniedException("Access denied. Cannot get passwords");
		}
	}
	public void validateCredentialsEntriy(PersistentPropertyEnvBased<P,V> entry) {
		validateCredentialsEntries(Arrays.asList(entry));
	}
	public void validateCredentialsEntries(Collection<? extends PersistentPropertyEnvBased<P,V>> properties) {
		// Verify KeePass entries for KPP and KPU
		validateAndSetCredentialsResponse2(properties, true,false);
	}

	public void validateAndSetCredentialsResponse2(Collection<? extends PersistentPropertyEnvBased<P,V>> properties, boolean errorOnMissingKeePassEntry, boolean setValue) {
		
		CredentialsRequestBuilder<P,V> builder = new CredentialsRequestBuilder<P,V>();
		
		//build the credentials request
		List<CredentialsRequest> requests = builder.buildFromList2(properties);
		List<PersistentPropertyEnvBased<P,V>> credProps = builder.credentailsProperties2();
		Map<String, CredentialsResponse> responseMap = credentialsHandler.findCredentails(requests);		
		validateAndSetCredentialsResponse2(credProps, responseMap, errorOnMissingKeePassEntry, setValue);
	}
	public void validateAndSetCredentialsResponse2(List<PersistentPropertyEnvBased<P,V>> credProps, Map<String, CredentialsResponse> responseMap, boolean errorOnMissingKeePassEntry, boolean setValue) {
		
		Set<String> missingKeys = new TreeSet<>();
		StringBuilder errors = new StringBuilder();
		for (PersistentPropertyEnvBased<P,V> prop : credProps) {
			if (prop.getValues().isEmpty()) continue;
			if ( prop.getValues().size() > 1) {
				//there should always be one environment value
				throw new IllegalStateException("Property '"+prop.getName()+" has more than one value for credentials.");
			}
			for (PersistentPropertyEnvBasedValue<V> valueObj: prop.getValues()) {
				if (valueObj == null || StringUtils.isEmpty(valueObj.getValue())) {
					continue;
				}
				String envCode=valueObj.getEnvCode();
				String key = getEnvKeePassTitle(valueObj.getValue(), envCode);
				CredentialsResponse resp = responseMap.get(key);
				//it will always return a response for each request, therefore no question of nullpointerexeception
				CredentialsEntry entry= resp.getCredentialsEntry();
				if (entry == null) {
					if (resp.getError()== null) {
						missingKeys.add(key);
					}else {
						errors.append(errors.length() == 0 ? "" : ", ").append(resp.getError());
					}
					continue;
				}else if (setValue){
					ProviderCredentialsType type = ProviderCredentialsType.forCode(prop.getTypeCode());
					if (type.getType()==CredentialsType.Username) {
						valueObj.setValue(entry.getUsername());
					}else if (type.getType()==CredentialsType.Password) {
						valueObj.setValue(entry.getPassword());
					}else if (type.getType()==CredentialsType.SshKey) {
						valueObj.setValue(entry.getSshKey());
					}
				}
			}
		}
		if (errors.length() > 0) {
			throw new TechnicalException("Error getting credentails: "+errors.toString());
		}
		if (errorOnMissingKeePassEntry && missingKeys.size() > 0) {
			throw new TechnicalException("Credentials entries not found. Keys: " + missingKeys);
		}
	}
	public Set<String> getReferencedPropertyNames(Collection<? extends PersistentPropertyEnvBased<P,V>> props) {
		Set<String> propNames = new TreeSet<>();
		for (PersistentPropertyEnvBased<P,V> prop: props) {
			for (V value: prop.getValues()) {
				if (StringUtils.isEmpty(value.getValue())) {
					continue;
				}
				String[] refPropNames = StringUtils.substringsBetween(value.getValue(), "${", "}");
				if (refPropNames != null) {
					Collections.addAll(propNames, refPropNames);
				}
			}
		}
		
		return propNames;
	}
	public String[] getReferencedPropertyNames(String value) {
		String[] refPropNames = StringUtils.substringsBetween(value, "${", "}");
		return refPropNames;
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
