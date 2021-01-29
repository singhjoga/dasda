package com.thetechnovator.common.java.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class EncryptionKeyGenerator {
	private static final String KEY_FILE = "com/dbsystel/devops/java/common/utils/secret.key";
	
	public static void main(String args[]) throws NoSuchAlgorithmException, IOException {

		if (args.length < 1) {
			System.out.println("usage: java EncryptionUtil basedir");
			System.exit(0);
		}

		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		SecureRandom random = new SecureRandom(); // cryptograph. secure random
		keyGen.init(random);
		SecretKey secretKey = keyGen.generateKey();
		File file = new File(args[0]+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+KEY_FILE);
		file.getParentFile().mkdirs();
		FileOutputStream output = new FileOutputStream(file);
		output.write(secretKey.getEncoded());
		output.close();

	}
}
