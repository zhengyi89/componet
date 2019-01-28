package com.zbjdl.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.FatalBeanException;

import com.google.common.collect.Maps;

/**
 * BeanUtils工具类
 * 
 *
 */
public class ChanPayBeanUtil {
	/**
	 * 将bean转换成map
	 * @param javaBean
	 * @return
	 */
	public static final Map<String, Object> convertBean2Map(Object javaBean) {
		Map<String, Object> ret = Maps.newHashMap();
		
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(javaBean.getClass());
		} catch (IntrospectionException e) {
			e.printStackTrace();
			return null;
		}
		
		PropertyDescriptor[] targetPds = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor targetPd : targetPds) {
			Method readMethod = targetPd.getReadMethod();
			if (readMethod != null && !readMethod.getName().equals("getClass")) {
				try {
					if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
						readMethod.setAccessible(true);
					}
					Object value = readMethod.invoke(javaBean);

					ret.put(targetPd.getName(), value);
				} catch (Throwable ex) {
					throw new FatalBeanException("Could not read property '" + targetPd.getName() + "' from bean", ex);
				}
			}
		}
        return ret;
    }
	
	/**
	 * 将map转换成bean
	 * @param clazz
	 * @param map
	 * @return
	 */
	public static final <T> T convertMap2Bean(Class<T> clazz, Map<String, Object> map){
		T t = null;
		try {
			t = clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(clazz);
		} catch (IntrospectionException e) {
			e.printStackTrace();
			return null;
		}
		
		PropertyDescriptor[] targetPds = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor targetPd : targetPds) {
			Method writeMethod = targetPd.getWriteMethod();
			if(writeMethod != null && map.containsKey(targetPd.getName())){
				try {
					if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
						writeMethod.setAccessible(true);
					}
					writeMethod.invoke(t, map.get(targetPd.getName()));
				} catch (Throwable ex) {
					throw new FatalBeanException("Could not write property '" + targetPd.getName() + "' from bean", ex);
				}
			}
		}
		
		return t;
	}
	
	/**
	 * 将map转成字符串
	 * @param map
	 * @return
	 */
	public static final String convertMap2Json(Map<String, Object> map){
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writeValueAsString(map);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
