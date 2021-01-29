package com.harbois.komrade.auth.parsing;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.split;
import static org.apache.commons.lang3.StringUtils.substringBefore;
import static org.apache.commons.lang3.StringUtils.substringBetween;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import com.harbois.komrade.Constants;
import com.harbois.komrade.auth.domain.ÊntityDefinition;
import com.thetechnovator.common.java.StringProperties;
import com.thetechnovator.common.java.utils.PropertyUtil;
import com.thetechnovator.common.java.utils.StringUtil;

public class EntityActionParser {
	public List<ÊntityDefinition> parse(ClassPathResource res) throws IOException{
		InputStream is = res.getInputStream();
		return parse(is);
	}
	public List<ÊntityDefinition> parse(InputStream input) throws IOException{
		List<ÊntityDefinition> result = new ArrayList<ÊntityDefinition>();
		List<String> lines = IOUtils.readLines(input,Constants.UTF8);
		StringProperties variables = PropertyUtil.findProperties(lines);
		for (String line: lines) {
			line = line.trim();
			if (line.length()==0 || line.startsWith("#")) {
				//empty or comment line
				continue;
			}
			//ignore property def line
			if (PropertyUtil.isValidPropertyEntry(line)) {
				continue;
			}
			ÊntityDefinition entity = parse(line,variables);
			result.add(entity);
		}
		
		return result;
	}
	
	public ÊntityDefinition parse(String line, StringProperties variables) {
	/*
	 * parse a line in the form of:
	 * component:${crud}[component-type],deploy[environment,component-type]
	 * where:
	 *  'component' is the entity
	 *  '${crud} is a variable name holidind the value for action(s)
	 *  '[component-type]' is a constraint variable for actions in 'crud' variable
	 *  'deploy' is another action having 'environment' and 'component-type' as constraint variables
	 */
		//ignore any inline comments i.e. contents after #
		String realLine = substringBefore(line, "#");
		if (isEmpty(realLine)) {
			realLine=line; //no inline comments
		}
		
		String[] parts = StringUtils.split(line,":");
		if (parts.length != 2) {
			//only two parts are allowed; entity name and actions
			throw new IllegalArgumentException("Not a valid entity definition line: "+line);
		}
		String entityName=parts[0];
		String actionsPartStr=parts[1];
		if (isEmpty(actionsPartStr)) {
			//at least one action should be defined
			throw new IllegalArgumentException("Not a valid entity definition line. At least one action should be defined : "+line);
		}
		//get the list of actions
		List<String> actionParts=StringUtil.splitIgnoreRoundBracket(actionsPartStr);
		Map<String, Set<String>> result = new TreeMap<String, Set<String>>();
		
		for (String actionPart: actionParts) {
			actionPart=actionPart.trim();
			Set<String> constrainVariables = Collections.emptySet();
			if (actionPart.contains("(")) {
				String constraintVariablesStr = substringBetween(actionPart, "(", ")");
				if (isEmpty(constraintVariablesStr)) {
					throw new IllegalArgumentException("Not a valid entity definition line. Constraint variables not propertly defined : "+line);
				}
				constrainVariables = new TreeSet<>(Arrays.asList(split(constraintVariablesStr,",")));
				actionPart = substringBefore(actionPart, "(");
			}
			//replace any property placeholders
			String actionsStr = PropertyUtil.replacePlaceholders(actionPart, variables);
			//because of the property placeholders, now the actions could be a comma delimited string
			for (String action: split(actionsStr,",")) {
				action=action.trim();
				if (!PropertyUtil.isValidName(action)) {
					throw new IllegalArgumentException("Not a valid entity definition line.Not a valid action name '"+action+"' in "+line);
				}
				result.put(action, constrainVariables);
			}
		}
		
		ÊntityDefinition entity = new ÊntityDefinition();
		entity.setActions(result);
		entity.setName(entityName);
		
		return entity;
	}

}
