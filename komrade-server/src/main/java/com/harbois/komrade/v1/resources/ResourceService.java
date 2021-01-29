package com.harbois.komrade.v1.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harbois.komrade.v1.common.services.BaseEntityService;
import com.harbois.komrade.v1.resources.domain.Resource;
import com.harbois.komrade.v1.resources.repository.EndpointRepository;
import com.harbois.komrade.v1.resources.repository.HostRepository;
import com.harbois.komrade.v1.resources.repository.ResourceRepository;

@Service
public class ResourceService extends BaseEntityService<Resource, String> {

	private static final Logger LOG = LoggerFactory.getLogger(ResourceService.class);

	private ResourceRepository repo;
	@Autowired
	private HostRepository hostRepo;
	@Autowired
	private EndpointRepository endpointRepo;
	
	@Autowired
	public ResourceService(ResourceRepository repo) {
		super(repo, Resource.class, String.class);
		this.repo=repo;
	}
/*

	public Resource add(Resource obj) {
		validate(obj, true);
		if (obj.getIsDisabled() == null) {
			obj.setIsDisabled(false);
		}
		setCommonData(obj, true);
		repo.save(obj);

		return obj;
	}

	public void update(Long id, Resource obj) {
		Resource saved = get(id, false,false); // make sure it exists
		obj.setId(id);
		validate(obj, false);
		ReflectUtil.copyNonNullProperties(obj, saved, "name", "description", "teamCode", "domainCode", "isDisabled");
		setCommonData(saved, false);
		repo.save(obj);
	}

	public void updateProperty(Long resourceId, ResourceProperty prop) {
		Resource res = get(resourceId);
		ResourceProperty savedProp=null;
		
		if (prop.getId() !=null) {
			savedProp=propRepo.findById(prop.getId()).orElse(null);
			if (savedProp == null) {
				throw new ResourceNotFoundException("ResourceProperty",prop.getId());
			}
		}else {
			savedProp = propRepo.findByResourceIdAndName(resourceId, prop.getName());
		}
		boolean isAdd=savedProp==null;
		Long propDefId=null;
		if (isAdd) {
			RequestValidationUtil.assertNotNull(prop.getPropertyDefId(), "propertyDefId");
			if (prop.getResourceId() == null) {
				prop.setResourceId(resourceId);
			}else {
				if (prop.getResourceId() != resourceId) {
					throw new BadRequestException("Value of 'resourceId' property should be the same as the Id of resource");
				}
			}
			propDefId=prop.getPropertyDefId();
		}else {
			//if update propertyDefId cannot be updated
			if (prop.getPropertyDefId() != null && !savedProp.getPropertyDefId().equals(prop.getPropertyDefId())) {
				throw new BadRequestException("Value of 'propertyDefId' cannot be changed");
			}
			propDefId=savedProp.getPropertyDefId();
		}
		//now basic request is valid. We can get the PropertyDefinition and further validate it
		ResourceDefinitionProperty propDef = typePropRepo.findById(propDefId).orElse(null);
		if (propDef ==null) {
			throw new BadRequestException("Property defition not found for 'propertyDefId' "+propDefId);
		}
		
		//verify the name. Name can change only when the instances of the same property can be more than 1
		if (propDef.getMaxCount() <= 1 && !propDef.getName().equals(prop.getName())) {
			throw new BadRequestException("Property name cannot be changed from '"+propDef.getName()+"' to '"+prop.getName()+"'. Names of properties having multiple instances can only be changed");
		}
		
	}
	
	public void delete(Long id) {
		repo.deleteById(id);
	}

	public List<Resource> search(String typeCode, String teamCode, String domainCode) {
		return repo.search(typeCode, teamCode, domainCode);
	}

	private void validate(Resource obj, boolean isAdd) {
		if (isAdd && obj.getId() != null) {
			RequestValidationUtil.assertNull(obj.getId(), "id");
		}
		if (isAdd) {
			RequestValidationUtil.assertNotEmpty(obj.getDescription(), "description");
			RequestValidationUtil.assertNotNull(obj.getName(), "name");
			RequestValidationUtil.assertNotNull(obj.getTypeCode(), "typeCode");
			RequestValidationUtil.assertNotNull(obj.getTeamCode(), "teamCode");
			RequestValidationUtil.assertNotNull(obj.getDomainCode(), "domainCode");
		}
	}
	public Resource get(Long id) {
		return get(id, false, false);
	}
	public Resource get(Long id, boolean returnProperties) {
		return get(id, false, returnProperties,false);
	}
	public Resource get(Long id, boolean returnProperties,boolean returnPropDef) {
		return get(id, false, returnProperties,returnPropDef);
	}

	public Resource get(Long id, boolean optional, boolean returnProperties,boolean returnPropDef) {
		Resource obj = repo.findById(id).orElse(null);
		if (!optional && obj == null) {
			throw new ResourceNotFoundException("Resource", id);
		}
		if (returnProperties) {
			List<ResourceProperty> allProps = getProperties(obj,returnPropDef);
			List<ResourcePropertyWithoutEnv> resProps = filterResourceLevelProperties(allProps);
			List<ResourceProperty> resEnvProps = filterEnvLevelProperties(allProps);
			*/
/*			ResourceWithProps resWithProps = new ResourceWithProps();
			try {
				PropertyUtils.copyProperties(resWithProps, obj);
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				throw new IllegalStateException(e);
			}
			resWithProps.setEnvProperties(resEnvProps);
			resWithProps.setProperties(resProps);
			obj = resWithProps;
			*/
	/*
			obj.setEnvProperties(resEnvProps);
			obj.setProperties(resProps);
		}
		return obj;
	}

	private List<ResourceProperty> getProperties(Resource res, boolean returnPropDef) {
		// get the list of saved properties
		List<ResourceProperty> result = propRepo.findByResourceId(res.getId());
		// we will be making temporary changes to return the result. Detach entities
		// from persistent context
		detachAll(result);
		// set the values
		for (ResourceProperty prop : result) {
			List<ResourcePropertyValue> values = valueRepo.findByPropertyId(prop.getId());
			detachAll(values);
			prop.setValues(values);
		}
		Map<Long, ResourceProperty> map = result.stream().collect(Collectors.toMap(ResourceProperty::getPropertyDefId, Function.identity()));
		// now get the properties needed for this resource type and insert/update
		// missing
		List<ResourceDefinitionProperty> typeProps = typePropRepo.findByResourceTypeCodeOrderByDisplayOrder(res.getTypeCode());
		if (returnPropDef) {
			for (ResourceDefinitionProperty typeProp : typeProps) {
				ResourceProperty prop;
				if (map.containsKey(typeProp.getId())) {
					prop = map.get(typeProp.getId());
				} else {
					// add the missing
					prop = new ResourceProperty();
					prop.setPropertyDefId(typeProp.getId());
					prop.setName(typeProp.getName());
					prop.setDescription(typeProp.getDisplayName());
					result.add(prop);
				}
				prop.setTypeCode(typeProp.getTypeCode());
				prop.setPropertyDef(typeProp);
			}
			Collections.sort(result, new ResourcePropertyComparator());
		}
		return result;
	}

	private List<ResourcePropertyWithoutEnv> filterResourceLevelProperties(List<ResourceProperty> allProps) {
		List<ResourcePropertyWithoutEnv> result = new ArrayList<>();
		for (ResourceProperty p : allProps) {
			if (!p.getPropertyDef().isEnvironmentRequired()) {
				// environment is not needed
				ResourcePropertyWithoutEnv s = new ResourcePropertyWithoutEnv();
				try {
					PropertyUtils.copyProperties(s, p);
				} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
					throw new IllegalStateException(e);
				}
				if (p.getValues() != null && !p.getValues().isEmpty()) {
					// value is the first element
					s.setValue(p.getValues().get(0).getValue());
				}
				result.add(s);
			}
		}

		return result;
	}

	private List<ResourceProperty> filterEnvLevelProperties(List<ResourceProperty> allProps) {
		return allProps.stream().filter(p -> p.getPropertyDef().isEnvironmentRequired()).collect(Collectors.toList());
	}

	private static class ResourcePropertyComparator implements Comparator<ResourceProperty> {

		@Override
		public int compare(ResourceProperty o1, ResourceProperty o2) {
			return o1.getPropertyDef().getDisplayOrder().compareTo(o2.getPropertyDef().getDisplayOrder());
		}

	}
	*/
}
