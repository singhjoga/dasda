package com.harbois.common.tests.base;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

public abstract class AbstractClient {
	private static final Logger LOG = LoggerFactory.getLogger(AbstractClient.class);
	private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
	ObjectMapper mapper;
	ObjectWriter ow;

	public AbstractClient() {
		mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		mapper.setDateFormat(df);
		ow = mapper.writer().withDefaultPrettyPrinter();
	}

	protected String asJsonString(Object obj) throws JsonProcessingException {
		if (obj == null) return "";
		String str = ow.writeValueAsString(obj);
		//LOG.info(str);
		return str;
	}

	public <T> T asObject(String jsonString, Class<T> returnType)
			throws JsonParseException, JsonMappingException, IOException {
		T obj = mapper.readValue(jsonString, returnType);

		return obj;
	}
	public <T> List<T> asObjectList(String jsonString, Class<T> returnType)
			throws JsonParseException, JsonMappingException, IOException {
		JsonNode nodes = asNode(jsonString);
		List<T> list = new ArrayList<T>();
		Iterator<JsonNode> itr = nodes.elements();
		while (itr.hasNext()) {
			JsonNode node = itr.next();
			T obj = asObject(node.toString(), returnType);
			list.add(obj);
		}
		return list;
	}	
	
	public JsonNode asNode(String jsonString) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readTree(jsonString);
	}
	
	public String findValueAsString(JsonNode parentNode, String fields) {
		JsonNode fieldNode = parentNode.findValue(fields);
		if (fieldNode==null) return null;
		return fieldNode.asText();
	}
	
	public boolean isStatusOk(int status) {
		if (status >= 200 && status < 300) {
			return true;
		}else {
			return false;
		}
	}
}
