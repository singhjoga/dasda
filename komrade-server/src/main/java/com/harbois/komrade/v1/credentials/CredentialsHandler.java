package com.harbois.komrade.v1.credentials;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.harbois.komrade.v1.credentials.provider.CredentialsEntry;
import com.harbois.komrade.v1.credentials.provider.CredentialsException;
import com.harbois.komrade.v1.credentials.provider.CredentialsManager;
import com.harbois.komrade.v1.credentials.provider.CredentialsProvider;
import com.harbois.komrade.v1.credentials.provider.CredentialsType;
import com.harbois.komrade.v1.credentials.provider.ProviderCredentialsType;
import com.thetechnovator.common.java.utils.PropertyUtil;

@Component
public class CredentialsHandler {
	private static final Logger LOG = LoggerFactory.getLogger(CredentialsHandler.class);
	private static final String CREDENTIALS_PROVIDER_SEPARATOR = "::";
	private List<CredentialsManager> managers = new ArrayList<CredentialsManager>();
	private Map<ProviderCredentialsType, CredentialsManager> typeManagerMap = new HashMap<>();

	//@Autowired
	//@Qualifier("AppConfigProperties")
	private Properties allConfigProps;

	@Value("${system.default.credentials.provider:}")
	private String defaultCredentialsProvider;

	private CredentialsManager defaultCredentailsMgr;

	public void init() throws CredentialsException {
		ServiceLoader<CredentialsProvider> loader = ServiceLoader.load(CredentialsProvider.class);
		Iterator<CredentialsProvider> itr = loader.iterator();
		while (itr.hasNext()) {
			CredentialsProvider p = itr.next();
			LOG.info("Initializing credentials provider: " + p.getClass().getName());
			CredentialsManager mgr = p.create();
			String propPrefix = mgr.propertiesPrefix();
			Properties props = PropertyUtil.filterProperties("^" + propPrefix + ".*", allConfigProps);
			try {
				mgr.init(props);
			} catch (CredentialsException e) {
				throw new IllegalStateException("Cannot start Credentails Manager: " + e.getMessage(), e);
			}
			managers.add(mgr);
			if (defaultCredentialsProvider.equalsIgnoreCase(mgr.name()) || defaultCredentialsProvider.equalsIgnoreCase(mgr.shortName())) {
				defaultCredentailsMgr = mgr;
			}
			for (ProviderCredentialsType type : mgr.supportedCredentails()) {
				typeManagerMap.put(type, mgr);
			}
		}

		// If default provider not found, raise an error
		if (defaultCredentailsMgr == null) {
			throw new CredentialsException("Default credentials provider not found: " + defaultCredentialsProvider);
		}
	}

	public void destroy() {
		for (CredentialsManager mgr : managers) {
			try {
				mgr.destroy();
			} catch (CredentialsException e) {
				LOG.error("Error destroying credentials manager: " + mgr.getClass().getName(), e);
			}
		}
	}

	public Map<String, CredentialsResponse> findCredentails(Collection<CredentialsRequest> requests) {
		Map<String, CredentialsResponse> result = new TreeMap<String, CredentialsResponse>();
		for (CredentialsRequest req : requests) {
			CredentialsResponse resp = new CredentialsResponse();
			result.put(req.getKey(), resp);
			String key = req.getKey();
			// Get the first type. Here assumption is that all types for the same key have
			// the same provider
			ProviderCredentialsType type = req.getRequiredTypes().iterator().next();
			CredentialsManager mgr = typeManagerMap.get(type);
			if (mgr == null) {
				// Check if the type is general credentials type;
				CredentialsType genType = CredentialsType.forCode(type.getCode());
				if (genType == null) {
					resp.setError("No registered credentails provider for type " + type.getCode());
					continue;
				}
				// Check if the key is prefixed with provider name/code
				if (key.indexOf(CREDENTIALS_PROVIDER_SEPARATOR) == -1) {
					// it is not prefixed, use the default credentails provider
					mgr = defaultCredentailsMgr;
				} else {
					String providerCode = StringUtils.substringBefore(key, CREDENTIALS_PROVIDER_SEPARATOR);
					key = StringUtils.substringAfter(key, CREDENTIALS_PROVIDER_SEPARATOR);
					// Find the matching manager
					for (CredentialsManager tempMgr : managers) {
						if (tempMgr.shortName().equalsIgnoreCase(providerCode) || tempMgr.name().equalsIgnoreCase(providerCode)) {
							mgr = tempMgr;
						}
					}
					if (mgr == null) {
						resp.setError("Credentials provider name/short '" + providerCode + "' does not match with any of the available providers "
								+ getProviderNames());
						continue;
					}
				}
			}
			try {
				CredentialsEntry cred = mgr.getCredetails(key, req.getEnvCode());
				resp.setCredentialsEntry(cred);
			} catch (CredentialsException e) {
				LOG.error(e.getMessage(), e);
				resp.setError("Error finding credentials " + e.getMessage());
			}

		}
		return result;
	}

	private String getProviderNames() {
		StringBuilder sb = new StringBuilder();
		for (CredentialsManager tempMgr : managers) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(tempMgr.shortName() + " or " + tempMgr.name());
		}

		return sb.toString();
	}
}
