package com.example.demo;

import java.lang.reflect.Field;

/**
 * Created by HMa on 2018/7/23.
 */
public class ReflexTest {

	public void setProperty(Object o,String propertyName,Object value) throws NoSuchFieldException,SecurityException,IllegalArgumentException,IllegalAccessException {
		Class c=o.getClass();
		Field field=c.getDeclaredField(propertyName);
		field.setAccessible(true);
		field.set(o,value);//修改这个对象的某个属性。
	}
}
