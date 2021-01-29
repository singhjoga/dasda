package com.harbois.oauth.tests.funtionaltests;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.junit.Assert;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.harbois.common.tests.base.IdentifiableClient;
import com.harbois.common.tests.base.RestException;
import com.harbois.common.tests.base.TestBase;
import com.harbois.oauth.api.v1.common.RestResponse;
import com.harbois.oauth.api.v1.common.UserContext;
import com.harbois.oauth.api.v1.common.domain.IdentifiableEntity;
import com.harbois.oauth.tests.cleints.TestData;

public class BaseIdentifiableTest<T extends IdentifiableEntity<ID>, ID extends Serializable> extends TestBase implements ApplicationContextAware {
	private IdentifiableClient<T, ID> client;
	private Class<T> clz;
	private Class<?> clientClz;
	private ApplicationContext ctx;

	public BaseIdentifiableTest(Class<T> clz, Class<?> clientClz) {
		super();
		this.clientClz = clientClz;
		this.clz = clz;
	}

	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		this.ctx = ctx;
	}

	@PostConstruct
	public void initBase() {
		client = (IdentifiableClient<T, ID>) ctx.getBean(clientClz);
	}

	protected void testUserFunctions(String username, String password, boolean canAdd, boolean canUpdate, boolean canRead, //
			boolean canDelete) throws Exception {
		List<T> initial = client.findAll().resultAsList(clz);
		String clientId = uuid();
		ID id = null;
		try {
			id = addWithUser(username, password, clientId);
			postSuccessCheck(canAdd, Action.Add, username);
		} catch (RestException e) {
			postFailureCheck(canAdd, Action.Add, username, e);
			id = addWithUser(TestData.USERNAME_SYSADMIN, TestData.USER_PASSWORD, clientId);
		}
		// get
		try {
			getAllWithUser(username, password);
			postSuccessCheck(canRead, Action.GetAll, username);
		} catch (RestException e) {
			postFailureCheck(canRead, Action.GetAll, username, e);
			// read with sysadmin
			getAllWithUser(TestData.USERNAME_SYSADMIN, TestData.USER_PASSWORD);
		}

		T obj = null;
		try {
			obj = getWithUser(username, password, id);
			postSuccessCheck(canRead, Action.GetOne, username);
		} catch (RestException e) {
			postFailureCheck(canRead, Action.GetOne, username, e);
			// read with sysadmin
			obj = getWithUser(TestData.USERNAME_SYSADMIN, TestData.USER_PASSWORD, id);
		}

		// test update
		try {
			updateWithUser(username, password, id, obj);
			postSuccessCheck(canUpdate, Action.Update, username);
		} catch (RestException e) {
			postFailureCheck(canUpdate, Action.Update, username, e);
			// read with sysadmin
			updateWithUser(TestData.USERNAME_SYSADMIN, TestData.USER_PASSWORD, id, obj);
		}

		//test delete
		try {
			deleteWithUser(username, password, id);
			postSuccessCheck(canUpdate, Action.Delete, username);
		} catch (RestException e) {
			postFailureCheck(canUpdate, Action.Delete, username, e);
			// read with sysadmin
			deleteWithUser(TestData.USERNAME_SYSADMIN, TestData.USER_PASSWORD, id);
		}
	}

	private void postSuccessCheck(boolean canDoIt, Action action, String username) {
		if (!canDoIt) {
			Assert.fail("User " + username + " should NOT be allowed to " + action.name()+" the "+clz.getSimpleName()+System.lineSeparator()+"User Roles: "+UserContext.getInstance().getRoles());
		}
	}

	private void postFailureCheck(boolean canDoIt, Action action, String username, Exception e) {
		if (canDoIt) {
			if (e.getMessage().contains("access_denied")) {
				Assert.fail("User " + username + " should be allowed to " + action.name()+" the "+clz.getSimpleName()+System.lineSeparator()+"User Roles: "+UserContext.getInstance().getRoles());
			}else {
				Assert.fail("Exception: "+e.getMessage());
			}
		} else {
			Assert.assertTrue(e.getMessage().contains("access_denied"));
		}
	}

	private ID addWithUser(String username, String password, String clientId) throws Exception {
		client.setUsername(username);
		client.setPassword(password);
		T obj = client.createTestObject(clientId);
		return client.addAndGetId(obj);
	}

	private T getWithUser(String username, String password, ID id) throws Exception {
		client.setUsername(username);
		client.setPassword(password);
		return client.get(id).resultAs(clz);
	}

	private void updateWithUser(String username, String password, ID id, T obj) throws Exception {
		client.setUsername(username);
		client.setPassword(password);
		client.beforeUpdate(obj);
		RestResponse resp=client.update(id, obj);
		if (resp.hasErrors()) {
			throw new RestException(resp.getErrorDetail().getMessage());
		}
	}

	private void deleteWithUser(String username, String password, ID id) throws Exception {
		client.setUsername(username);
		client.setPassword(password);
		client.delete(id);
	}

	private List<T> getAllWithUser(String username, String password) throws Exception {
		client.setUsername(username);
		client.setPassword(password);
		return client.findAll().resultAsList(clz);
	}
	private String uuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	private static enum Action {
		Add, Update, Delete, GetOne, GetAll;
	}
}
