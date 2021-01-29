package com.harbois.oauth.server.tests.base;

import java.io.Serializable;

import com.harbois.oauth.server.api.v1.common.RestResponse;
import com.harbois.oauth.server.api.v1.common.RestResponse.AddResult;
import com.harbois.oauth.server.api.v1.common.domain.IdentifiableEntity;

public abstract class IdentifiableClient<T extends IdentifiableEntity<ID>, ID extends Serializable> extends AbstractResourceClient{
	private String url;
	private Class<T> entityClass;
	private Class<ID> idClass;

	public IdentifiableClient(String url, Class<T> entityClass, Class<ID> idClass) {
		super();
		this.url = url;
		this.entityClass = entityClass;
		this.idClass = idClass;
	}
	public abstract T createTestObject(String clientId)  throws Exception;
	public void beforeUpdate(T obj) {
		//nothing here. subclasses can overwrite to clean the object
	}
	public RestResponse add(T obj)  throws Exception{
		RestResponse resp = createResource(url, obj);
		return resp;
	}
	public RestResponse get(ID id) throws Exception{
		return getResource(getUrl(id), entityClass);
	}
	public RestResponse findAll() throws Exception{
		return getResourceAsList(url, entityClass);
	}
	public RestResponse update(ID id, T obj) throws Exception{
		return updateResource(getUrl(id), obj);
	}
	public RestResponse delete(ID id) throws Exception{
		return deleteResource(getUrl(id));
	}
	public String getUrl(ID id) {
		return url+"/"+id.toString();
	}
	public ID addAndGetId(T obj)  throws Exception{
		RestResponse resp = add(obj);
		if (resp.hasErrors()) {
			throw new RestException("Err in Add: "+mapper.writeValueAsString(resp.getErrorDetail()));
		}
		String id =resp.resultAs(AddResult.class).getId();
		if (idClass.equals(String.class)) {
			return (ID)id;
		}else if (idClass.equals(Long.class)) {
			return (ID)Long.valueOf(id);
		}else if (idClass.equals(Long.class)) {
			return (ID)Long.valueOf(id);
		}else {
			throw new IllegalArgumentException("ID Class not supported yet: "+idClass.getName());
		}
	}
}
