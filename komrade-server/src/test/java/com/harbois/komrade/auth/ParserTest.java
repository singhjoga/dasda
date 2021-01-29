package com.harbois.komrade.auth;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.harbois.komrade.auth.domain.ÊntityDefinition;
import com.harbois.komrade.auth.parsing.EntityActionParser;
import com.harbois.komrade.auth.parsing.ParseException;
import com.harbois.komrade.auth.parsing.RolePermissionParser;
import com.harbois.komrade.auth.domain.RolePermissionEntity;


public class ParserTest {

	@Test
	public void testEntityActionParser() throws IOException, ParseException {
		ClassPathResource res = new ClassPathResource("entity-actions.def");
		EntityActionParser parser = new EntityActionParser();
		List<ÊntityDefinition> entities = parser.parse(res);
		Assert.assertEquals(11, entities.size());
		res = new ClassPathResource("role-permissions.def");
		RolePermissionParser roleParser = new RolePermissionParser();
		Map<String, RolePermissionEntity> rolePermission =roleParser.parse(res, entities);
		Assert.assertEquals(4, rolePermission.size());
	}
}
