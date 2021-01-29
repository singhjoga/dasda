package com.harbois.komrade.v1._controllers;

import javax.crypto.BadPaddingException;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController{
	private static final Logger LOG = LoggerFactory.getLogger(TestController.class);
	@RequestMapping(method=RequestMethod.GET,value="/test")
	public ResponseEntity<?> findBySystemReferenceType() {
		String pass ="itispassword";
		String salt=DatatypeConverter.printHexBinary(pass.substring(0, 9).getBytes());
		TextEncryptor encryptor = Encryptors.queryableText(pass, salt);
		String testStr="teststring";
		String encrypted=encryptor.encrypt(testStr);
		String decrypted=encryptor.decrypt(encrypted);
		LOG.info("salet="+salt);
		LOG.info("original="+testStr+", encrypted="+encrypted+", decrypted="+decrypted);
		try  {
			TextEncryptor encryptor2 = Encryptors.queryableText(pass, "1222");
			decrypted=encryptor2.decrypt(encrypted);
		}catch (IllegalStateException e) {
			LOG.info("wrong salt");
			if (e.getCause() instanceof BadPaddingException) {
				LOG.error("Bad key");
			}else {
				e.printStackTrace();
			}
		}
		try  {
			TextEncryptor encryptor2 = Encryptors.queryableText("wrongpass", salt);
			decrypted=encryptor2.decrypt(encrypted);
		}catch (IllegalStateException e) {
			LOG.info("wrong pass");
			if (e.getCause() instanceof BadPaddingException) {
				LOG.error("Bad key");
			}else {
				e.printStackTrace();
			}
		}
		
		LOG.info("original="+testStr+", encrypted="+encrypted+", decrypted="+decrypted);
		return ResponseEntity.ok("");
	}
}
