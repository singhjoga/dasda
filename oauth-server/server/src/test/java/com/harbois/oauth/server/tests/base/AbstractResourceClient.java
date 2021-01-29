package com.harbois.oauth.server.tests.base;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.harbois.oauth.server.api.v1.common.RestResponse;
import com.harbois.oauth.server.api.v1.common.RestResponse.AddResult;
import com.harbois.oauth.server.api.v1.common.RestResponse.GeneralErrorDetail;
import com.harbois.oauth.server.api.v1.common.RestResponse.ValidationErrorDetail;
import com.harbois.oauth.server.api.v1.common.RestResponse.WarningDetail;
import com.harbois.oauth.server.api.v1.common.exception.ErrorCodes;

public abstract class AbstractResourceClient extends AbstractClient {
	private static final Logger LOG = LoggerFactory.getLogger(AbstractResourceClient.class);
	public static final String REST_DATE_FORMAT = "yyyy-MM-dd";
	private static final SimpleDateFormat restDateFormat = new SimpleDateFormat(REST_DATE_FORMAT);

	@Autowired
	private AuthClient auth;

	@Autowired
	private MockMvc mvc;
	private boolean skipAuthentication; // used for testing security testing
	private String username=null;
	private String password=null;
	public AbstractResourceClient() {
		this(false);
	}

	public AbstractResourceClient(boolean skipAuthentication) {
		this.skipAuthentication = skipAuthentication;
	}

	protected String getAccessToken() throws Exception {
		if (username != null) {
			return auth.getAccessToken(username, password);
		}else {
			return auth.getAccessToken();
		}

	}

	private RestResponse getRestResponse(MvcResult result) throws RestException {
		String content = null;
		try {
			content = result.getResponse().getContentAsString();
			JsonNode node = asNode(content);
			if (node == null) {
				throw new RestException(result.getResolvedException());
			}
			JsonNode resultNode = node.findValue("result");
			JsonNode errorNode = node.findValue("errorDetail");
			JsonNode warningNode = node.findValue("warningDetail");
			if (resultNode == null && errorNode == null && warningNode == null && !isStatusOk(result.getResponse().getStatus())) {
				// not a valid RestResponse object
				throw new RestException(content);
			}
			RestResponse resp = new RestResponse();
			if (errorNode != null) {
				String code = findValueAsString(errorNode, "code");
				if (ErrorCodes.VALIDATION_ERROR.equals(code)) {
					resp.setErrorDetail(asObject(errorNode.toString(), ValidationErrorDetail.class));
				} else {
					resp.setErrorDetail(asObject(errorNode.toString(), GeneralErrorDetail.class));
				}
			}
			if (warningNode != null) {
				resp.setWarningDetail(asObject(warningNode.toString(), WarningDetail.class));
			}
			if (resultNode != null) {
				resp.setResult(resultNode.toString());
			}

			return resp;
		} catch (JsonMappingException e) {
			throw new RestException(e);
		} catch (UnsupportedEncodingException e) {
			throw new RestException(e);
		} catch (JsonParseException e) {
			throw new RestException(e);
		} catch (IOException e) {
			throw new RestException(e);
		}

	}

	public RestResponse createResource(String uri, Object resource) throws RestException {
		LOG.info("Adding resource to " + uri);
		ResultActions actions;
		try {
			actions = mvc.perform(postAuthenticated(uri) //
					.content(asJsonString(resource)));

			MvcResult result = actions.andReturn();
			RestResponse resp = getRestResponse(result);
			if (resp.getResult() != null) {
				AddResult obj = asObject(resp.getResult().toString(), AddResult.class);
				resp.setResult(obj);
			}
			if (!resp.hasErrors()) {
				actions.andExpect(status().isCreated()).andExpect(header().exists("location")); //
			}

			return resp;
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	public RestResponse updateResource(String uri, Object resource) throws RestException {
		LOG.info("Updating resource to " + uri);
		try {
			MockHttpServletRequestBuilder builder = patchAuthenticated(uri);
			String content = asJsonString(resource);
			builder.content(content);
			ResultActions actions = mvc.perform(builder);
			return getOkResponse(actions);
		} catch (Exception e) {
			throw new RestException(e.getMessage(), e);
		}

	}

	public RestResponse uploadFile(String uri, MockMultipartFile file) throws RestException {
		LOG.info("Uploading file resource to " + uri);
		try {
			MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(uri).file(file);
			builder.header("Authorization", "Bearer " + getAccessToken(), getAccessToken()); //
			ResultActions actions = mvc.perform(builder);
			return getOkResponse(actions);
		} catch (Exception e) {
			throw new RestException(e);
		}

	}

	public byte[] downloadFile(String uri) throws RestException {
		LOG.info("Downloading file from " + uri);
		try {
			ResultActions actions = mvc.perform(getAuthenticated(uri).contentType(MediaType.APPLICATION_OCTET_STREAM));
			MvcResult result = actions.andReturn();

			if (result.getResolvedException() == null) {
				actions.andExpect(status().isOk());
			} else {
				Exception e = result.getResolvedException();
				throw new RestException(e.getMessage(), e);
			}

			return result.getResponse().getContentAsByteArray();
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	public RestResponse deleteResource(String uri) throws RestException {
		LOG.info("Deleting resource to " + uri);
		try {
			ResultActions actions = mvc.perform(deleteAuthenticated(uri));
			return getOkResponse(actions);
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	public <T> RestResponse getResourceAsList(String uri, Class<T> clz) throws RestException {
		RestResponse resp = getResponse(uri);
		LOG.debug("RESULT=" + resp.getResult());
		try {
			List<T> obj = asObjectList(resp.getResult().toString(), clz);
			resp.setResult(obj);
			return resp;
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	public <T> RestResponse getResource(String uri, Class<T> clz) throws RestException {
		RestResponse resp = getResponse(uri);
		LOG.debug("RESULT=" + resp.getResult());
		try {
			T obj = asObject(resp.getResult().toString(), clz);
			resp.setResult(obj);
			return resp;
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	public RestResponse getResponse(String uri) throws RestException {
		LOG.info("Getting resource " + uri);
		try {
			ResultActions actions = mvc.perform(getAuthenticated(uri));
			return getOkResponse(actions);
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	public RestResponse getOkResponse(ResultActions actions) throws RestException {

		MvcResult result = actions.andReturn();
		RestResponse resp = getRestResponse(result);
		try {
			if (!resp.hasErrors()) {
				actions.andExpect(status().isOk());
			}
			return resp;
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	public <T> List<T> getResourceAsListOld(String uri, Class<T> clz) throws RestException {
		String jsonString = getResourceOld(uri);
		try {
			return asObjectList(jsonString, clz);
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	public <T> T getResourceOld(String uri, Class<T> clz) throws RestException {
		String jsonString = getResourceOld(uri);
		try {
			return asObject(jsonString, clz);
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	public String getResourceOld(String uri) throws RestException {
		LOG.info("Getting resource " + uri);
		try {
			ResultActions actions = mvc.perform(getAuthenticated(uri));

			MvcResult result = actions.andReturn();

			if (result.getResolvedException() == null) {
				actions.andExpect(status().isOk());
			} else {
				Exception e = result.getResolvedException();
				throw new RestException(e.getMessage(), e);
			}

			String jsonString = result.getResponse().getContentAsString();
			LOG.debug(jsonString);

			return jsonString;
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	public MockHttpServletRequestBuilder deleteAuthenticated(String uri) throws RestException {
		MockHttpServletRequestBuilder builder = delete(uri);
		try {
			if (!skipAuthentication) {
				builder.header("Authorization", "Bearer " + getAccessToken(), getAccessToken()) //
						.contentType(MediaType.APPLICATION_JSON);
			}
			return builder;
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	public MockHttpServletRequestBuilder patchAuthenticated(String uri) throws RestException {
		MockHttpServletRequestBuilder builder = patch(uri);
		try {
			if (!skipAuthentication) {
				builder.header("Authorization", "Bearer " + getAccessToken(), getAccessToken()) //
						.contentType(MediaType.APPLICATION_JSON);
			}
			return builder;
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	public MockHttpServletRequestBuilder postAuthenticated(String uri) throws RestException {
		MockHttpServletRequestBuilder builder = post(uri);
		try {
			if (!skipAuthentication) {
				builder.header("Authorization", "Bearer " + getAccessToken(), getAccessToken()) //
						.contentType(MediaType.APPLICATION_JSON);
			}
			return builder;
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	public MockHttpServletRequestBuilder postAuthenticated(String uri, Object resource) throws RestException {
		MockHttpServletRequestBuilder builder = postAuthenticated(uri);
		try {
			builder.content(asJsonString(resource));

			return builder;
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	public MockHttpServletRequestBuilder getAuthenticated(String uri) throws RestException {
		MockHttpServletRequestBuilder builder = get(uri);
		try {
			if (!skipAuthentication) {
				builder.header("Authorization", "Bearer " + getAccessToken(), getAccessToken()) //
						.contentType(MediaType.APPLICATION_JSON);
			}
			return builder;
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	public String buildQueryString(String paramNames, Object... values) {
		String[] params = StringUtils.split(paramNames, ",");

		if (params.length != values.length) {
			throw new IllegalStateException("No. of params does not match with the no. of values");
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < params.length; i++) {
			if (values[i] == null) {
				continue;
			}
			if (sb.length() > 0) {
				sb.append("&");
			}
			String value = null;
			if (values[i] instanceof Date) {
				value = restDateFormat.format((Date) values[i]);
			} else {
				value = values[i].toString();
			}
			sb.append(params[i].trim()).append("=").append(value);
		}

		return sb.toString();
	}

	public boolean isSkipAuthentication() {
		return skipAuthentication;
	}

	public void setSkipAuthentication(boolean skipAuthentication) {
		this.skipAuthentication = skipAuthentication;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
