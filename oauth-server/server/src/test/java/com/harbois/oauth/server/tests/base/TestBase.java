package com.harbois.oauth.server.tests.base;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.harbois.oauth.server.api.v1.common.RestResponse;
import com.harbois.oauth.server.api.v1.common.RestResponse.AddResult;
import com.harbois.oauth.server.api.v1.common.RestResponse.ValidationError;
import com.harbois.oauth.server.api.v1.common.RestResponse.ValidationErrorDetail;
@ActiveProfiles("testing")
//@AutoConfigureMockMvc(print=MockMvcPrint.NONE)
@AutoConfigureMockMvc()
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestBase{
	private static final Logger LOG = LoggerFactory.getLogger(TestBase.class);
	
	@Autowired
	private AuthClient auth;
	@Autowired
	private BCryptPasswordEncoder passEncoder;
	
	@Autowired
	private MockMvc mvc;
	private ObjectMapper mapper=new ObjectMapper();
	protected String getAccessToken() throws Exception {
		return auth.getAccessToken();
	}
	
	protected void assertRestException(Exception e) {
		Assert.assertEquals(e.getClass().getSimpleName(),RestException.class.getSimpleName());
	}
	
	public boolean hasValidationError(ValidationErrorDetail errorDetail, String code, String field) {
		for (ValidationError error: errorDetail.getErrors()) {
			if (error.getCode().equalsIgnoreCase(code) && error.getField().equalsIgnoreCase(field)) {
				return true;
			}
		}
		
		return false;
	}
	public void comparePass(String expectedClearText, String actualEncoded) {
		//secret should match the encrypted secret
		Assert.assertTrue(passEncoder.matches(expectedClearText, actualEncoded));
	}
	
	public String successAdd(RestResponse resp) throws RestException{
		success(resp);
		return resp.resultAs(AddResult.class).getId();
	}
	public void success(RestResponse resp) throws RestException{
		if (resp.hasErrors()) {
			String errorStr;
			try {
				errorStr = mapper.writeValueAsString(resp.getErrorDetail());
				throw new RestException("Errors Found: "+errorStr);
			} catch (JsonProcessingException e) {
				throw new RestException(e);
			}
		}
	}
}
