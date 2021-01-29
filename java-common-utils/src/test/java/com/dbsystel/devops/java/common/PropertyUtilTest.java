package com.dbsystel.devops.java.common;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.thetechnovator.common.java.StringProperties;
import com.thetechnovator.common.java.utils.PropertyUtil;

import junit.framework.Assert;

public class PropertyUtilTest {

	@Test
	public void testReplacePlaceholders() {
		String str = "abc-${TOKEN-1}-xyzaaa-${TOKEN-2}-ggfsss${TOKEN-3}ffffff#122#{TOKEN-4}";
		StringBuilder result = new StringBuilder();
		StringBuilder missingRefs = new StringBuilder();
		StringProperties props = new StringProperties();
		props.put("TOKEN-1", "TOKEN-1-VALUE");
		boolean retvalue = PropertyUtil.replacePlaceholders(str, props, result, missingRefs);
		Assert.assertFalse(retvalue);
		System.out.println("RESULT: "+result.toString()+", MISSING: "+missingRefs.toString());
		
		result = new StringBuilder();
		missingRefs = new StringBuilder();
		props.put("TOKEN-2", "TOKEN-2-VALUE");
		retvalue = PropertyUtil.replacePlaceholders(str, props, result, missingRefs);
		Assert.assertFalse(retvalue);
		System.out.println("RESULT: "+result.toString()+", MISSING: "+missingRefs.toString());
		
		result = new StringBuilder();
		missingRefs = new StringBuilder();
		props.put("TOKEN-3", "TOKEN-3-VALUE");
		props.put("TOKEN-4", "TOKEN-4-VALUE");
		retvalue = PropertyUtil.replacePlaceholders(str, props, result, missingRefs);
		Assert.assertTrue(retvalue);
		System.out.println("RESULT: "+result.toString()+", MISSING: "+missingRefs.toString());
		
		str="$SCRIPTDIR/#{component.definition.script}";
		missingRefs = new StringBuilder();
		props.put("component.definition.script", "TOKEN-3-VALUE");
		retvalue = PropertyUtil.replacePlaceholders(str, props, result, missingRefs);
	}
	@Test
	public void testFindProperties() {
		List<String> lines = new ArrayList<String>();
		lines.add("eev-environments=DEV*,INT*,WRT*"); //valid
		lines.add("ev:abc:def=DEV*,INT*,WRT*"); //invalid name: colon not allowed in name
		lines.add("xxxxxxxxxxxxxxxxx"); //invalid: no value
		lines.add("xxx#abc=xyz"); //invalid: no key value. value after hash is a comment
		lines.add("xxx.abc=xyz"); //valid. dot in name is allowed
		lines.add("xxx_abc=xyz"); //valid. underscore in name is allowed
		lines.add("xxx abc=xyz"); //invalid. space in name is not allowed
		lines.add("no-value="); //value. empty value
		
		StringProperties props = PropertyUtil.findProperties(lines);
		Assert.assertEquals(4, props.keySet().size());
		Assert.assertEquals("DEV*,INT*,WRT*", props.get("eev-environments"));
		Assert.assertEquals("xyz", props.get("xxx.abc"));
		Assert.assertEquals("xyz", props.get("xxx_abc"));
		Assert.assertEquals("", props.get("no-value"));
	}
}
