package com.thetechnovator.common.java.utils;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.substringBefore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thetechnovator.common.java.StringProperties;

public class PropertyUtil {
	private static final Logger LOG = LoggerFactory.getLogger(PropertyUtil.class);
	//private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{([^}]+)\\}");
	private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("(\\$|\\#)\\{([^}]+)\\}");
	private static final Pattern NAME_PATTERN=Pattern.compile("^[A-Za-z0-9+_.-]+$");
	public static Properties copyProperties(Properties source) {
		Properties copy = new Properties();
		copyProperties(source, copy);
		return copy;
	}

	public static void copyProperties(Properties source, Properties target) {
		for (Object keyObj : source.keySet()) {
			target.put(keyObj, source.get(keyObj));
		}
	}
	public static Properties toLowerCaseProperties(Properties source) {
		Properties copy = new Properties();
		for (Object keyObj : source.keySet()) {
			copy.put(((String)keyObj).toLowerCase(), source.get(keyObj));
		}
		
		return copy;
	}
	public static String asString(Properties source) {
		StringBuilder sb = new StringBuilder();
		for (Object keyObj : source.keySet()) {
			if (sb.length() != 0) {
				sb.append(", ");
			}
			sb.append(keyObj).append("=").append(source.getProperty((String)keyObj));
		}
		
		return sb.toString();
	}
	public static String asString(Map<?, ?> source) {
		StringBuilder sb = new StringBuilder();
		for (Object keyObj : source.keySet()) {
			if (sb.length() != 0) {
				sb.append(", ");
			}
			sb.append(keyObj).append("=").append(source.get((String)keyObj));
		}
		
		return sb.toString();
	}
	public static String replacePlaceholders(String rawString, StringProperties properties) {
		return replacePlaceholders(rawString, properties,false);
	}
	public static String replacePlaceholders(String rawString, StringProperties properties, boolean ignoreMissingRefs) {
		StringBuilder result = new StringBuilder();
		StringBuilder missingRefs = new StringBuilder();
		if (!replacePlaceholders(rawString, properties, result, missingRefs)) {
			if (ignoreMissingRefs) {
				return result.toString();
			}else {
				throw new IllegalStateException("Property references not found for: "+missingRefs.toString());
			}
		}
		return result.toString();
	}
	public static boolean replacePlaceholders(String rawString, StringProperties properties, StringBuilder result, StringBuilder missingRefs) {
		if (!replacePlaceholders(rawString, properties, result, missingRefs, "${", "}")) {
			return false;
		}
		rawString = result.toString();
		result.delete(0, result.length());
		return replacePlaceholders(rawString, properties, result, missingRefs, "#{", "}");
	}
	public static boolean replacePlaceholders(String rawString, StringProperties properties, StringBuilder result, StringBuilder missingRefs, String startTag, String endTag) {
		String resultStr=rawString;
		String[] refKeys = StringUtils.substringsBetween(resultStr, startTag, endTag);
		if (refKeys==null || refKeys.length==0) {
			//nothing to replace
			result.append(resultStr);
			return true;
		}
		boolean retValue = true;
		for (String refKey: refKeys) {
			String refValue = properties.get(refKey);
			if (refValue == null) {
				if (missingRefs != null) {
					missingRefs.append(missingRefs.length()==0?"":",").append(refKey);
				}
				retValue=false;
			}else {
				resultStr = StringUtils.replace(resultStr,startTag+refKey+endTag,refValue);
			}
		}
		result.append(resultStr);
		return retValue;
	}
	public static void writeProperties(File file, Properties props) throws IOException{
			OutputStream os = new FileOutputStream(file);
			props.store(os, "UTF-8");
			os.close();
			LOG.info("Properties written to "+file.getAbsolutePath());
	}
	public static Properties laodFromFile(File propertyFile) {
		Properties props = new Properties();
		try {
			FileInputStream fis = new FileInputStream(propertyFile);
			props.load(fis);
			fis.close();
			return props;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		
	}
	public static String findConfigProperty(String name, Properties sessionProps, Properties systemProperties,
			Properties componentProperties, String componentType) {
		// First find in the given properties. Most of the time it will be Maven User
		// Properties (command line props)
		if (sessionProps != null) {
			String value = sessionProps.getProperty(name);
			if (value != null && !value.equals("null")) {
				return value;
			}
		}
		// 2nd find component specific. Not yet implemented
		if (componentProperties != null) {
			String value = componentProperties.getProperty(name);
			if (value != null && !value.equals("null")) {
				return value;
			}
		}
		// 3rd try component type specific
		if (componentType != null) {
			String propName = componentType.toLowerCase() + "." + name;
			String value = systemProperties.getProperty(propName);
			if (value != null && !value.equals("null")) {
				return value;
			}
		}
		// component type specific not found. Find default. It must be then set
		String propName = "default." + name;
		String value = systemProperties.getProperty(propName);
		if (value != null && !value.equals("null")) {
			return value;
		}else {
			if (value == null) {
				throw new IllegalStateException("Property "+name+" not defined");
			}else {
				// 'null' strung value
				return null;
			}
		}
	}
	public static StringProperties findProperties(List<String> lines) {
		StringProperties props = new StringProperties();
		for (String line: lines) {
			line=line.trim();
			if (line.length()==0 || line.startsWith("#")) {
				continue;
			}
			//remove the inline comments
			String realLine = substringBefore(line, "#");
			if (isEmpty(realLine)) {
				realLine=line; //no inline comments
			}
			Entry<String, String> entry = getKeyValueEntry(realLine);
			if (entry != null) {
				props.put(entry.getKey(), entry.getValue());
			}
		}
		
		return props;
	}
	public static boolean isValidPropertyEntry(String line) {
		Entry<String, String> entry = getKeyValueEntry(line);
		return entry != null;
	}
	private static Entry<String, String> getKeyValueEntry(String line) {
		String key = StringUtils.substringBefore(line, "=");
		if (StringUtils.isEmpty(key)) {
			return null;
		}
		if (key.length()==line.length()) {
			//no value
			return null;
		}
		//check if the key name is a valid property name
		if (!isValidName(key)) {
			return null;
		}
		String value = StringUtils.substringAfter(line, "=");
		
		return new AbstractMap.SimpleEntry<String, String>(key, value);
	}
	public static boolean isValidName(String name) {
		Matcher matcher = NAME_PATTERN.matcher(name);
		return matcher.matches();
	}
	/**
	 * @param name - is a regex i.e. for start with use '^xxxx.*', for contain use '.*xxxxx.*'
	 * @param properties
	 * @return
	 */
	public static Properties filterProperties(String name, Properties properties) {
		Properties retValue = new Properties();
		for (Entry<Object, Object> entry: properties.entrySet()) {
			if (((String)entry.getKey()).matches(name)) {
				retValue.put(entry.getKey(), entry.getValue());
			}
		}

		return retValue;
	}
	public static void printProperties(Properties props) {
		for (Object objKey: props.keySet()) {
			String value = props.getProperty((String)objKey);
			LOG.debug("PROP: "+objKey+"="+value);
		}
	}
}
