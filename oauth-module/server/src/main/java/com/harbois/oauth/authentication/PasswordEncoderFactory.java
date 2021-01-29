package com.harbois.oauth.authentication;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.Md4PasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

@SuppressWarnings("deprecation")
public class PasswordEncoderFactory {
	private static final Logger LOG = LoggerFactory.getLogger(PasswordEncoderFactory.class);
	public static PasswordEncoder createPasswordEncoder(PasswordEncoderSetting setting) {
		if (setting.getType() == PasswordEncoderType.Bcrypt) {
			return createBCryptEncoder(setting.getParamValue());
		}else if (setting.getType() == PasswordEncoderType.NoOp) {
			return NoOpPasswordEncoder.getInstance();
		}else if (setting.getType() == PasswordEncoderType.Pbkdf2) {
			return createPbkdf2Encoder(setting.getParamValue());
		}else if (setting.getType() == PasswordEncoderType.SCrypt) {
			return new SCryptPasswordEncoder();
		}else if (setting.getType() == PasswordEncoderType.MD4) {
			return new Md4PasswordEncoder();
		}else if (setting.getType() == PasswordEncoderType.SHA256) {
			return createStandardEncoder(setting.getParamValue());
		}else {
			LOG.error("Password encoder "+setting.getType().name()+" not supported. NoOp will be used");
			return NoOpPasswordEncoder.getInstance();
		}
	}
	
	private static PasswordEncoder createBCryptEncoder(String paramValue) {
		if (StringUtils.isEmpty(paramValue)) {
			return new BCryptPasswordEncoder();
		}else {
			if (!NumberUtils.isCreatable(paramValue)) {
				LOG.error("Non numeric value for BCrypt Length parameter. Default will be used");
				return new BCryptPasswordEncoder();
			}
			return new BCryptPasswordEncoder(Integer.valueOf(paramValue));
		}
	}
	private static PasswordEncoder createPbkdf2Encoder(String paramValue) {
		if (StringUtils.isEmpty(paramValue)) {
			return new Pbkdf2PasswordEncoder();
		}else {
			return new Pbkdf2PasswordEncoder(paramValue);
		}
	}
	private static PasswordEncoder createStandardEncoder(String paramValue) {
		if (StringUtils.isEmpty(paramValue)) {
			return new StandardPasswordEncoder();
		}else {
			return new StandardPasswordEncoder(paramValue);
		}
	}
}
