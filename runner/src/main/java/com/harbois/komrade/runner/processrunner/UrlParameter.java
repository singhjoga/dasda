package com.harbois.komrade.runner.processrunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;

public class UrlParameter extends CommandParameter<URL>{

	public UrlParameter(String prefix, URL value) {
		super(prefix, value);
	}
	public UrlParameter(URL value) {
		super("", value);
	}
	@Override
	public String toDisplayString() {
		String userInfo=null;
		if (StringUtils.isNotEmpty(value.getUserInfo())) {
			//only the username
			userInfo=StringUtils.substringBefore(value.getUserInfo(), ":");
			if (StringUtils.isEmpty(userInfo)) {
				//there is not password in original value
				userInfo=value.getUserInfo();
			}else{
				userInfo=userInfo+":******";
			}
		}
		try {
			URI url = new URI(value.getProtocol(), userInfo, value.getHost(), value.getPort(), value.getPath(), value.getQuery(), value.getRef());
			return url.toString();
		} catch (URISyntaxException e) {
			throw new IllegalStateException(e);
		}
	}
}
