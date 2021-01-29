package com.harbois.komrade.v1.credentials.provider;

import java.util.HashMap;
import java.util.Map;

public enum ProviderCredentialsType {
	KeePassUsername("KPU", CredentialsType.Username),//
	KeePassPassword("KPP",CredentialsType.Password),//
	KeePassSshKey("KPK",CredentialsType.SshKey), //
	PleasantUsername("PSU", CredentialsType.Username),//
	PleasantPassword("PSP",CredentialsType.Password),//
	PleasantSshKey("PSK",CredentialsType.SshKey), //
	Username("UID", CredentialsType.Username),//
	Password("PWD",CredentialsType.Password),//
	SshKey("SSK",CredentialsType.SshKey);
	
	private String code;
	private CredentialsType type;
	private static Map<String, ProviderCredentialsType> map=new HashMap<String, ProviderCredentialsType>();

	static {
		map.put(KeePassUsername.code, KeePassUsername);
		map.put(KeePassPassword.code, KeePassPassword);
		map.put(KeePassSshKey.code, KeePassSshKey);
		map.put(PleasantUsername.code, PleasantUsername);
		map.put(PleasantPassword.code, PleasantPassword);
		map.put(PleasantSshKey.code, PleasantSshKey);
		map.put(Username.code, Username);
		map.put(Password.code, Password);
		map.put(SshKey.code, SshKey);
	}
	private ProviderCredentialsType(String code, CredentialsType type) {
		this.code = code;
		this.type = type;
	}

	public String getCode() {
		return code;
	}
	
	public CredentialsType getType() {
		return type;
	}

	public static ProviderCredentialsType forCode(String typeCode) {
		return map.get(typeCode);
	}
	public static boolean isCredentialsType(String typeCode) {
		return map.containsKey(typeCode);
	}
}
