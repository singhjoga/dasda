package com.harbois.komrade.auth.parsing;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.substringBefore;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.harbois.komrade.Constants;
import com.harbois.komrade.auth.AuthUtil;
import com.harbois.komrade.auth.domain.EntityAction;
import com.harbois.komrade.auth.domain.EntityPermission;
import com.harbois.komrade.auth.domain.RolePermissionEntity;
import com.harbois.komrade.auth.domain.ÊntityDefinition;
import com.thetechnovator.common.java.StringProperties;
import com.thetechnovator.common.java.utils.PropertyUtil;
import com.thetechnovator.common.java.utils.StringUtil;

public class RolePermissionParser {
	private static Logger LOG = LoggerFactory.getLogger(RolePermissionParser.class);
	public Map<String, RolePermissionEntity> parse(ClassPathResource res, List<ÊntityDefinition> allEntities) throws IOException, ParseException{
		InputStream is = res.getInputStream();
		return parse(is,allEntities);
	}
	public Map<String, RolePermissionEntity> parse(InputStream input, List<ÊntityDefinition> allEntities) throws IOException, ParseException{
		List<String> lines = IOUtils.readLines(input,Constants.UTF8);
		StringProperties variables = PropertyUtil.findProperties(lines);
		Map<String, List<RoleParseResult>> parseResult=new TreeMap<>();
		Map<String, ÊntityDefinition> entitiesMap = allEntities.stream().collect(Collectors.toMap(p->p.getName(), p->p));
		ParseContext ctx = new ParseContext();
		ctx.variables=variables;
		ctx.entityMap=entitiesMap;
		
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

			RoleParseResult roleParseResult = parse(line,ctx);
			if (roleParseResult == null) {
				continue;
			}
			if (!parseResult.containsKey(roleParseResult.roleName)) {
				List<RoleParseResult> roleParseList = new ArrayList<>();
				parseResult.put(roleParseResult.roleName, roleParseList);
			}
			parseResult.get(roleParseResult.roleName).add(roleParseResult);
		}
		
		return createResult(parseResult, ctx);
	}
	private Map<String, RolePermissionEntity> createResult(Map<String, List<RoleParseResult>> parseResult, ParseContext ctx) {
		Map<String, RolePermissionEntity> result = new TreeMap<String, RolePermissionEntity>();
		//for each role, get the included and excluded entity and their actions
		for (String roleName: parseResult.keySet()) {
			List<RoleParseResult> roleParseEntries = parseResult.get(roleName);
		
			//first combine the list of not included entities and actions, so that it could be excluded frm end result
			Set<String> allNotAllowedEntities=new TreeSet<>();
			Map<String,Set<String>> allNotAllowedActions = new TreeMap<>();
			List<EntityPermission> allEntities = new ArrayList<>();
			for (RoleParseResult roleParseEntry: roleParseEntries) {
				allNotAllowedEntities.addAll(roleParseEntry.notAllowedEntities);
				allEntities.addAll(roleParseEntry.allowedEntities);
				for (EntityPermission roleEntry: roleParseEntry.allowedEntities) {
					Set<String> actions = allNotAllowedActions.get(roleEntry.getEntityName());
					if (actions == null) {
						actions = new TreeSet<>();
						allNotAllowedActions.put(roleEntry.getEntityName(), actions);
					}
					actions.addAll(roleParseEntry.notAllowedActions);
				}
			}
			//now generate the result
			RolePermissionEntity rolePermEntry = new RolePermissionEntity();
//			rolePermEntry.setNotAllowedActions(allNotAllowedActions);
//			rolePermEntry.setNotAllowedEntities(allNotAllowedEntities);
			for (EntityPermission EntityPermission: allEntities) {
				String entityName=EntityPermission.getEntityName();
			
				if (rolePermEntry.getAllowedEntities().containsKey(entityName)) {
					AuthUtil.merge(EntityPermission, rolePermEntry.getAllowedEntities().get(entityName));
				}else {
					rolePermEntry.getAllowedEntities().put(entityName, EntityPermission);
				}
			}
			result.put(roleName, rolePermEntry);
		}
		
		return result;
	}
	public RoleParseResult parse(String line, ParseContext ctx) throws ParseException {
		/*
		 * 	rel-coordinator:${rel-coordinator-entities}:*(environment=${dev-environments})
		 */
		//ignore any inline comments i.e. contents after #
		String realLine = substringBefore(line, "#");
		if (isEmpty(realLine)) {
			realLine=line; //no inline comments
		}
		
		String[] parts = StringUtils.split(line,":");
		if (parts.length != 3) {
			//only three parts are allowed; role name, entity name and actions
			throw new IllegalArgumentException("Not a valid entity definition line: "+line);
		}
		String roleName=parts[0].trim();
		String entityNameStr=parts[1].trim();
		String actionsPartStr=parts[2].trim();
		if (isEmpty(entityNameStr)) {
			//at least one action should be defined
			throw new IllegalArgumentException("Not a valid entity definition line. At least one entity name should be defined : "+line);
		}
		if (isEmpty(actionsPartStr)) {
			//at least one action should be defined
			throw new IllegalArgumentException("Not a valid entity definition line. At least one action should be defined : "+line);
		}

		List<EntityParseResult> entitiesParseResults = parseEntities(entityNameStr, ctx);
		List<ActionParseResult> actionsParseResults = parseActions(actionsPartStr, ctx);
		List<EntityPermission> allowedEntities= new ArrayList<>();
		Set<String> notAllowedEntities = new TreeSet<>();
		Set<String> notAllowedActions = new TreeSet<>();
		for (int i=entitiesParseResults.size()-1;i>=0;i--) {
			EntityParseResult entityParseEntry=entitiesParseResults.get(i);
			if (entityParseEntry.not) {
				//entity is excluded
				notAllowedEntities.addAll(Arrays.asList(entityParseEntry.entities));
				//remove it so that no need to check it later
				entitiesParseResults.remove(i);
			}
		}
		for (int i=actionsParseResults.size()-1;i>=0;i--) {
			ActionParseResult actionParseEntry=actionsParseResults.get(i);
			if (actionParseEntry.not) {
				//entity is excluded
				notAllowedActions.addAll(Arrays.asList(actionParseEntry.actions));
				//remove it so that no need to check it later
				actionsParseResults.remove(i);
			}
		}
		//now only allowed entities and actions are left
		for (EntityParseResult entityParseEntry: entitiesParseResults)
		{			
			for (String entity: entityParseEntry.entities) {
				if (notAllowedEntities.contains(entity)) {
					//ignore
					continue;
				}
				EntityPermission EntityPermission = new EntityPermission(); 
				EntityPermission.setEntityName(entity);
				allowedEntities.add(EntityPermission);
				ÊntityDefinition entityDef= ctx.entityMap.get(entity);
				if (entityDef==null) {
					throw new IllegalArgumentException("Not a valid entity '"+entity+"' in definition line: "+line);
				}
				for (ActionParseResult actionParseEntry: actionsParseResults) {
					for (String action: actionParseEntry.actions) {
						if (action.equals("*")) {
							addValidActions(EntityPermission, entityDef, actionParseEntry.constraints,notAllowedActions, entityDef.getActions().keySet().toArray(new String[0]));
						}else {
							addValidActions(EntityPermission, entityDef, actionParseEntry.constraints,notAllowedActions,action);
						}
					}
				}
			}
		}
		RoleParseResult result = new RoleParseResult();
		result.roleName=roleName;
		result.allowedEntities=allowedEntities;
		result.notAllowedActions=notAllowedActions;
		result.notAllowedEntities=notAllowedEntities;
		
		return result;
		
	}
	private void addValidActions(EntityPermission EntityPermission, ÊntityDefinition entityDef,StringProperties actionConstraints, Set<String> notAllowedActions, String...actions) {
		//add the action only if it is a valid action for this entity
		Map<String,Set<String>> entityDefActions=entityDef.getActions();
		for (String action: actions) {
			if (notAllowedActions.contains(action)) {
				continue;
			}
			if (entityDefActions.containsKey(action)) {
				EntityAction entityAction= EntityPermission.getActionsMap().get(action);
				if (entityAction == null) {
					entityAction = new EntityAction();
					entityAction.setName(action);
					EntityPermission.getActionsMap().put(action, entityAction);
				}
				//add the valid constraints
				Set<String> entityActionConstraints = entityDefActions.get(action);
				for (String constraint: actionConstraints.keySet()) {
					if (entityActionConstraints.contains(constraint)) {
						entityAction.getConstraintObj().put(constraint, actionConstraints.get(constraint));
					}
				}
			}
		}
	}
	
	private List<EntityParseResult> parseEntities(String inStr, ParseContext ctx) throws ParseException{
		String str=StringUtil.removeAllWhitespaces(inStr);
		//entites are comma splitted
		String[] parts = StringUtils.split(str,",");
		List<EntityParseResult> resultList = new ArrayList<>();
		for (String part: parts) {
			EntityParseResult result = new EntityParseResult();
			//part can start with a negative
			boolean not=part.startsWith("!");
			if (not) {
				part = StringUtils.substringAfter(part, "!");
			}
			//part can be a variable
			String[] entities=null;
			if (part.startsWith("${")) {
				part = PropertyUtil.replacePlaceholders(part, ctx.variables);
			}
			if (part.equals("*")) {
				entities=ctx.entityMap.keySet().toArray(new String[0]);
			}else {
				entities=StringUtils.split(part,",");		
			}

			result.not=not;
			result.entities=entities;
			resultList.add(result);
		}

		return resultList;
	}
	private List<ActionParseResult> parseActions(String inStr, ParseContext ctx) throws ParseException{
		//remove all whitespace to make it easy later and avoid trimming
		String str=StringUtil.removeAllWhitespaces(inStr);
		//entites are comma splitted
		String[] parts = StringUtils.split(str,",");
		List<ActionParseResult> resultList = new ArrayList<>();
		for (String part: parts) {
			//part can start with a negative
			boolean not=part.startsWith("!");
			if (not) {
				part = StringUtils.substringAfter(part, "!");
			}
			//part can be a variable
			if (part.startsWith("${")) {
				part = PropertyUtil.replacePlaceholders(part, ctx.variables);
			}
			StringProperties constraints = new StringProperties();
			String constraintsStr=StringUtils.substringBetween(part, "(",")");
			if (StringUtils.isNotEmpty(constraintsStr)) {
				part = StringUtils.substringBefore(part, "(");
				String constraintsAry[] = StringUtils.split(constraintsStr,";"); //constraint separator is semi-colon
				for (String constStr: constraintsAry) {
					String name=StringUtils.substringBefore(constStr, "=");
					String value=StringUtils.substringAfter(constStr, "=");
					if (StringUtils.isEmpty(name) || StringUtils.isEmpty(value)) {
						throw new ParseException("Not a valid variable definition. "+constStr);
					}
					boolean allNotValue=value.startsWith("!${"); //it is variable with Not 
					//if value is a variable, resolve it.
					value = PropertyUtil.replacePlaceholders(value, ctx.variables);
					if (allNotValue) {
						//put NOT in front of all values
						value = StringUtils.join(StringUtils.split(value,","),",!");
					}
					constraints.put(name, value);
				}
				
			}			
			String[] actions=StringUtils.split(part,",");
			ActionParseResult result = new ActionParseResult();
			result.not=not;
			result.constraints=constraints;
			result.actions=actions;
			
			resultList.add(result);
		}

		return resultList;
	}

	
	private void assertThat(boolean condition, String msg) throws ParseException {
		if (!condition) {
			throw new ParseException(msg);
		}
	}
	private static class RoleParseResult {
		String roleName;
		List<EntityPermission> allowedEntities;
		Set<String> notAllowedEntities;
		Set<String> notAllowedActions;
	}
	private static class EntityParseResult {
		boolean not=false;
		String[] entities;
	}
	private static class ActionParseResult {
		boolean not=false;
		String[] actions;
		StringProperties constraints;
	}
	private static class ParseContext {
		Map<String, ÊntityDefinition> entityMap;
		StringProperties variables;
	}
}
