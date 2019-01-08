package com.zbjdl.common.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.zbjdl.common.json.JSONUtils;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;


/**
 * 反射工具类
 * 
 */
public class ReflectionUtils {
	private static final String Setter_Method_Prefix = "set";
	private static final String Getter_Method_Prefix = "get";
	private static final int ZERO = 0;
	private static final int FIRST = 1;
	static Map<String, Class> baseClassMap = new HashMap<String, Class>();
	static {
		baseClassMap.put("boolean", java.lang.Boolean.class);
		baseClassMap.put("byte", java.lang.Byte.class);
		baseClassMap.put("int", java.lang.Integer.class);
		baseClassMap.put("long", java.lang.Long.class);
		baseClassMap.put("short", java.lang.Short.class);
		baseClassMap.put("double", java.lang.Double.class);
		baseClassMap.put("float", java.lang.Float.class);
		baseClassMap.put("char", java.lang.Character.class);
	}

	/**
	 * 执行目标对象的特定方法
	 * 
	 * @param target
	 * @param methodName
	 * @return oject
	 */
	public static Object executeMethod(Object target, String methodName) {
		return executeMethod(target, methodName, null, null);
	}

	/**
	 * 执行目标对象的特定方法
	 * 
	 * @param target
	 * @param methodName
	 * @param paramValue
	 * @param paramType
	 * @return Object
	 */
	public static Object executeMethod(Object target, String methodName,
			Object[] paramValue, Class[] paramType) {
		if (paramType == null)
			paramType = new Class[ZERO];
		return invokeMethod(target, methodName, paramValue, paramType);
	}

	/**
	 * 执行目标对象的field的set方法
	 * 
	 * @param object
	 * @param fieldName
	 * @param paramValue
	 * @param paramType
	 */
	public static void executeSetterMethodByField(Object object,
			String fieldName, Object[] paramValue, Class[] paramType) {
		checkExecuteMethodParameter(object, fieldName);
		String methodName = getMethodName(fieldName, Setter_Method_Prefix);
		invokeMethod(object, methodName, paramValue, paramType);
	}

	/**
	 * 执行目标对象的field的get方法
	 * 
	 * @param object
	 * @param fieldName
	 * @param paramType
	 * @return Object
	 */
	public static Object executeGetterMethodByField(Object object,
			String fieldName, Class[] paramType) {
		if (paramType == null)
			paramType = new Class[ZERO];
		return executeGetterMethodByField(object, fieldName, paramType, null);
	}

	/**
	 * 执行目标对象的field的get方法
	 * 
	 * @param object
	 * @param fieldName
	 * @param paramType
	 * @param paramValue
	 * @return Object
	 */
	public static Object executeGetterMethodByField(Object object,
			String fieldName, Class[] paramType, Object[] paramValue) {
		checkExecuteMethodParameter(object, fieldName);
		String methodName = getMethodName(fieldName, Getter_Method_Prefix);
		return invokeMethod(object, methodName, paramValue, paramType);
	}

	private static void checkExecuteMethodParameter(Object object,
			String fieldName) {
		CheckUtils.notNull(object, "arugment object");
		CheckUtils.notEmpty(fieldName, "arugment fieldName");
	}

	private static String getMethodName(String fieldName, String Method_Prefix) {
		String firstChar = fieldName.substring(ZERO, FIRST);
		String otherChar = fieldName.substring(FIRST);
		return Method_Prefix + firstChar.toUpperCase() + otherChar;
	}

	private static Object invokeMethod(Object object, String methodName,
			Object[] paramValue, Class[] paramType) {
		Method method = null;
		try {
			method = getMethod(object, methodName, paramType);
			if (method != null)
				return method.invoke(object, paramValue);
		} catch (SecurityException e) {
			throw new RuntimeException("Method: invokeMethod" + "occur error",
					e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Method: invokeMethod" + "occur error",
					e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Method: invokeMethod" + "occur error",
					e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Method: invokeMethod" + "occur error",
					e);
		}
		return null;
	}

	/**
	 * 获得目标对象的特定方法
	 * 
	 * @param target
	 * @param methodName
	 * @param paramType
	 * @return Method
	 */
	public static Method getMethod(Object target, String methodName,
			Class[] paramType) {
		if (paramType == null)
			paramType = new Class[ZERO];
		Method[] methods = target.getClass().getMethods();
		for (int index = 0; index < methods.length; ++index) {
			Method method = methods[index];
			if ((methodNameIsEquals(method, methodName))
					&& (methodParameterTypeAreEquals(method, paramType)))
				return method;
		}
		return null;
	}

	/**
	 * 根据ClassName获得Class对象
	 * 
	 * @param className
	 * @return
	 */
	public static Class classForName(String className) {
		try {
			return Resources.classForName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Method: classForName " + "occur error",
					e);
		}
	}

	private static boolean methodNameIsEquals(Method method, String methodName) {
		return method.getName().equals(methodName);
	}

	private static boolean methodParameterTypeAreEquals(Method method,
			Class[] types) {
		Class[] parameterTypes = method.getParameterTypes();
		if ((parameterTypes == null) && (types == null))
			return true;
		if (parameterTypes.length != types.length)
			return false;
		for (int index = 0; index < parameterTypes.length; ++index) {
			Class parameterType = parameterTypes[index];
			Class type = types[index];
			if (!(parameterType.equals(type)))
				return false;
		}
		return true;
	}

	/**
	 * 取得泛型参类型
	 * 
	 * @param clazz
	 *            泛型类
	 * @param index
	 *            泛型参数索引
	 * @return 泛型类型
	 */
	public static Class getGenericClass(Class clazz, int index) {
		Type genType = clazz.getGenericSuperclass();
		if (genType instanceof ParameterizedType) {
			Type[] params = ((ParameterizedType) genType)
					.getActualTypeArguments();

			if ((params != null) && (params.length > index)) {
				return (Class) params[index];
			}
		}
		return null;
	}

	/**
	 * 取得第一个泛型类型
	 * 
	 * @param clazz
	 *            泛型类
	 * @return 泛型类型
	 */
	public static Class getGenericClass(Class clazz) {
		return getGenericClass(clazz, 0);
	}

	/**
	 * <pre>
	 * 将字符串值反射为指定类型的对象数组
	 * 参数类型数组和参数值数组需一一匹配
	 * 数组的参数值用json格式表示
	 * @param paramTypes参数类型
	 * @param paramValues参数值
	 * @return
	 * </pre>
	 */
	public static Object[] convertStringToObject(Class[] paramTypes,
			String[] paramValues) {
		Object[] values = new Object[paramTypes.length];
		for (int i = 0; i < paramTypes.length; i++) {
			Class clazz = paramTypes[i];
			Object value = null;
			// 参数是数组时的转换方式
			if (clazz.isArray()) {
				if (baseClassMap.keySet().contains(
						clazz.getComponentType().toString())) {
					value = JSONUtils.jsonToArray(paramValues[i], baseClassMap
							.get(clazz.getComponentType().toString()));
				} else {
					value = JSONUtils.jsonToArray(paramValues[i],
							clazz.getComponentType());
				}
			} else {// 参数是非数组的转换方式
				value = ConvertUtils.convert(clazz, paramValues[i]);
			}
			values[i] = value;
		}
		return values;
	}

	/**
	 * <pre>
	 * 动态构造包含某个方法的接口类
	 *  备注:返回值类型，参数类型，异常类型只支持在当前classLoader中包含的值，当前classLoader中没有的类型暂不支持
	 * @param classFullName
	 *            接口类全名,如:com.demo.DemoFacade
	 * @param methodName
	 *            方法名,如:execute
	 * @param returnValueClassName
	 *            方法返回值的类型,如:java.long.String
	 * @param paramClassNames
	 *            方法参数类型数组,如[java.lang.String,int,java.math.BigDecimal,java.util.Date,boolean,java.lang.Long[]]
	 * @param exceptionClassNames
	 *            方法可能抛出的异常,如:java.lang.Exception
	 * @return
	 * @throws NotFoundException
	 * @throws CannotCompileException
	 * </pre>
	 */
	public static Class makeInterface(String classFullName, String methodName,
			String returnValueClassName, String[] paramClassNames,
			String[] exceptionClassNames) throws NotFoundException,
			CannotCompileException {

		ClassPool pool = ClassPool.getDefault();
		pool.makePackage(pool.getClassLoader(),
				classFullName.substring(0, classFullName.lastIndexOf(".")));
		CtClass ctClass = pool.makeInterface(classFullName);
		CtClass returnValueClass = returnValueClassName != null ? pool
				.get(returnValueClassName) : CtClass.voidType;
		CtClass[] paramClass = paramClassNames != null ? pool
				.get(paramClassNames) : null;
		CtClass[] exceptionClass = exceptionClassNames != null ? pool
				.get(exceptionClassNames) : null;

		CtMethod ctMethod = CtNewMethod.abstractMethod(returnValueClass,
				methodName, paramClass, exceptionClass, ctClass);
		ctClass.addMethod(ctMethod);

		return ctClass.toClass();

	}


}
