package com.geariot.platform.freelycar_wechat.utils;


import java.util.Collection;

import net.sf.json.util.PropertyFilter;

public class JsonPropertyFilter implements PropertyFilter {
	
	private Class<?>[] filterProperties = null;
	private Class<?>[] collectionProperties = null;
	private boolean filterCollection = false;
	
	public JsonPropertyFilter(Class<?>... filterProperties){
		this.filterProperties = filterProperties;
	}
	
	public void setColletionProperties(Class<?>... collectionProperties){
		this.filterCollection = true;
		this.collectionProperties = collectionProperties;
	}
	
	@Override
	public boolean apply(Object source, String name, Object value) {
		if(value == null){
			return true;
		}
		if(filterCollection && Collection.class.isAssignableFrom(value.getClass())){
			Collection val = (Collection) value;
			if(val.size() != 0){
				Object arg = val.iterator().next();
				for(Class<?> c : collectionProperties){
					if(c.isAssignableFrom(arg.getClass())){
						return true;
					}
				}
			}
		}
		else {
			for(Class<?> c : filterProperties){
				if(c.isAssignableFrom(value.getClass())){
					return true;
				}
			}
		}
		return false;
	}

}
