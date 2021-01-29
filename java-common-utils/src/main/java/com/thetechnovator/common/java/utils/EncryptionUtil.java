package com.thetechnovator.common.java.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncryptionUtil {
	private static SecretKeySpec skeySpec;
	private static final Logger LOG = LoggerFactory.getLogger(EncryptionUtil.class);
	private static final String KEY_FILE = "/com/dbsystel/devops/java/common/utils/secret.key";
	static {
		try {
			InputStream input = EncryptionUtil.class.getResourceAsStream(KEY_FILE);
			byte[] all = new byte[1024];
			int len =input.read(all);
			byte[] in = Arrays.copyOf(all, len);
			skeySpec = new SecretKeySpec(in, "AES");
			input.close();
		} catch (FileNotFoundException e) {
			throw new IllegalStateException(e);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public static byte[] encrypt(String input) {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES");

			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			return cipher.doFinal(input.getBytes());
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		} catch (NoSuchPaddingException e) {
			throw new IllegalStateException(e);
		} catch (InvalidKeyException e) {
			throw new IllegalStateException(e);
		} catch (IllegalBlockSizeException e) {
			throw new IllegalStateException(e);
		} catch (BadPaddingException e) {
			throw new IllegalStateException(e);
		}

	}
	public static String decrypt(String input) {
		return decrypt(input.getBytes());
	}
	public static String decrypt(byte[] input) {
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			return new String(cipher.doFinal(input));
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		} catch (NoSuchPaddingException e) {
			throw new IllegalStateException(e);
		} catch (InvalidKeyException e) {
			throw new IllegalStateException(e);
		} catch (IllegalBlockSizeException e) {
			LOG.error("Ignoring decryption. Encrypted value is not a valid AES encrypted: "+input);
			return input.toString();
		} catch (BadPaddingException e) {
			throw new IllegalStateException(e);
		}
	}
}
