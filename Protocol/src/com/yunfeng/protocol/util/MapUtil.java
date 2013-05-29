package com.yunfeng.protocol.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * map和其他类型转换
 */
public class MapUtil {
	private final static Map<String, Map<String, Field>> classes = new HashMap<String, Map<String, Field>>();

	/**
	 * 取出value的值放入object中
	 */
	public static void getValueIntoObject(Object value, Object object) {
		Map<String, Field> object_fields = getDeclaredFields(object.getClass());
		Map<String, Field> value_fields = getDeclaredFields(value.getClass());
		for (String name : object_fields.keySet()) {
			if (value_fields.containsKey(name)) {
				try {
					object_fields.get(name).set(object,
							value_fields.get(name).get(value));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 取出对象的值放入map中
	 */
	public static void getValueIntoMap(Object object, Map<String, Object> map) {
		try {
			Class<? extends Object> c = object.getClass();
			for (Field field : getDeclaredFields(c).values()) {
				map.put(field.getName(), field.get(object));
			}
		} catch (Exception e) {
			Log.error("", e);
		}
	}

	/**
	 * 根据map映射成对象
	 * 
	 * @param <T>
	 */
	public static <T> T getPojo(Class<T> c, Map<?, ?> map) {
		T object = null;
		try {
			object = c.newInstance();
			for (Field field : getDeclaredFields(c).values()) {
				if (map.get(field.getName()) != null) {
					Object translateType = ConstantUtil
							.translateObjectToDefaultType(field.getType(),
									map.get(field.getName()));
					field.set(object, translateType);

				}

			}
		} catch (Exception e) {
			Log.error("", e);
		}
		return object;
	}

	/**
	 * 取得类结构
	 */
	private static Map<String, Field> getDeclaredFields(
			Class<? extends Object> c) {
		Map<String, Field> map = null;
		String name = c.getName();
		if (classes.get(name) != null) {
			return classes.get(name);
		} else {
			map = new HashMap<String, Field>();
			classes.put(name, map);
			Field[] fields = c.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				map.put(field.getName(), field);
			}
			return map;
		}
	}

	/**
	 * 取得类结构
	 */
	public static Field getDeclaredField(Class<? extends Object> c, String name) {
		return MapUtil.getDeclaredFields(c).get(name);
	}
}
