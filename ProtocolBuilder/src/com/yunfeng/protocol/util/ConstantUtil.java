package com.yunfeng.protocol.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Element;

public class ConstantUtil {
	private static ConstantUtil instance = new ConstantUtil();

	private ConstantUtil() {

	}

	public static final ConstantUtil getInstance() {
		return instance;
	}

	public <T> List<T> readPojoByElement(Class T, Element rootElement,
			Class subElementClass) {
		List<T> objectList = new ArrayList<T>();
		for (Iterator<Element> elementIterator = rootElement
				.elementIterator(getClassShortName(T)); elementIterator
				.hasNext();) {
			try {
				Element pojoElement = (Element) elementIterator.next();
				T object = (T) T.newInstance();
				for (Iterator<Attribute> attributeIterator = pojoElement
						.attributeIterator(); attributeIterator.hasNext();) {
					Attribute attribute = (Attribute) attributeIterator.next();
					injectValueByString(attribute.getName(),
							attribute.getValue(), object, T);
				}
				if (subElementClass != null) {
					List subObjectList = readPojoByElement(subElementClass,
							pojoElement, null);
					injectValue(getClassListName(subElementClass),
							subObjectList, object, T);
				}
				objectList.add(object);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return objectList;
	}

	public <T> List<T> readPojoByElement(Class T, Element rootElement) {
		List<T> objectList = new ArrayList<T>();
		for (Iterator<Element> elementIterator = rootElement
				.elementIterator(getClassShortName(T)); elementIterator
				.hasNext();) {
			try {
				Element pojoElement = (Element) elementIterator.next();
				T object = (T) T.newInstance();
				for (Iterator<Attribute> attributeIterator = pojoElement
						.attributeIterator(); attributeIterator.hasNext();) {
					Attribute attribute = (Attribute) attributeIterator.next();
					injectValueByString(attribute.getName(),
							attribute.getValue(), object, T);
				}

				if (pojoElement.elements().size() != 0) {
					for (Element element : (List<Element>) pojoElement
							.elements()) {
						String getterMethodName = getFieldGetterMethodName(element
								.getName() + "List");
						String paramClassName = T
								.getMethod(getterMethodName, null)
								.toGenericString().split("<")[1].split(">")[0];
						Class paramClass = Class.forName(paramClassName);
						List paramList = readPojoByElement(paramClass,
								pojoElement);
						String setterMethodName = getFieldSetterMethodName(element
								.getName() + "List");
						Method setterMethod = T.getMethod(setterMethodName, T
								.getMethod(getterMethodName, null)
								.getReturnType());
						setterMethod.invoke(object, paramList);
					}
				}
				objectList.add(object);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return objectList;
	}

	public void injectValueByString(String injectFieldName,
			String injectFieldValue, Object injectObject, Class injectClass) {
		try {
			Method getterMethod = injectClass.getMethod(
					getFieldGetterMethodName(injectFieldName), null);
			Class<?> returnType = getterMethod.getReturnType();
			Object injectFieldObject = translateStringToDefaultType(returnType,
					injectFieldValue);
			Method setterMethod = injectClass.getMethod(
					getFieldSetterMethodName(injectFieldName), returnType);
			setterMethod.invoke(injectObject, injectFieldObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void injectValue(String injectFieldName, Object injectFieldValue,
			Object injectObject, Class injectClass) {
		try {
			Method getterMethod = injectClass.getMethod(
					getFieldGetterMethodName(injectFieldName), null);
			Class<?> returnType = getterMethod.getReturnType();
			Method setterMethod = injectClass.getMethod(
					getFieldSetterMethodName(injectFieldName), returnType);
			setterMethod.invoke(injectObject, injectFieldValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getFieldGetterMethodName(String fieldName) {
		return "get" + getStringHeadUp(fieldName);
	}

	public String getFieldSetterMethodName(String fieldName) {
		return "set" + getStringHeadUp(fieldName);
	}

	public String getStringHeadUp(String s) {
		char[] charArray = s.toCharArray();
		char headChar = charArray[0];
		if (headChar >= 'a' && headChar <= 'z') {
			headChar -= 32;
		}
		charArray[0] = headChar;
		return new String(charArray);
	}

	public String getStringHeadDown(String s) {
		char[] charArray = s.toCharArray();
		char headChar = charArray[0];
		if (headChar >= 'A' && headChar <= 'Z') {
			headChar += 32;
		}
		charArray[0] = headChar;
		return new String(charArray);
	}

	public Object translateStringToDefaultType(Class<?> type, String s) {
		if (type.equals(int.class)) {
			return Integer.parseInt(s);
		} else if (type.equals(double.class)) {
			return Double.parseDouble(s);
		} else if (type.equals(float.class)) {
			return Float.parseFloat(s);
		} else if (type.equals(short.class)) {
			return Short.parseShort(s);
		} else if (type.equals(long.class)) {
			return Long.parseLong(s);
		} else if (type.equals(boolean.class)) {
			return Boolean.parseBoolean(s);
		} else if (type.equals(byte.class)) {
			return Byte.parseByte(s);
		} else if (type.equals(String.class)) {
			return s.equals("") ? null : s;
		}
		return null;
	}

	public String getClassListName(Class c) {
		return getClassShortName(c) + "List";
	}

	public String getClassShortName(Class beanClass) {
		String[] classNameStrings = beanClass.getName().split("\\.");
		String name = classNameStrings[classNameStrings.length - 1];
		return getStringHeadDown(name);
	}

	/**
	 * 新建文件
	 * 
	 * @param filePathAndName
	 * @param fileContent
	 */
	public void newFile(String filePathAndName, String fileContent)
			throws IOException {
		System.out.println(filePathAndName);
		String filePath = filePathAndName;
		File myFilePath = new File(filePath.replace("%20", " "));
		if (!myFilePath.exists()) {
			String dirPath = filePath.substring(0, filePath.lastIndexOf("/"));
			createdir(dirPath);
			myFilePath.createNewFile();
		}
		FileWriter resultFile = new FileWriter(myFilePath);
		PrintWriter myFile = new PrintWriter(resultFile);
		String strContent = fileContent;
		myFile.println(strContent);
		resultFile.close();
	}

	/**
	 * 查看文件是否存在
	 * 
	 * @param filePathAndName
	 * @return
	 */
	public boolean checkFileExite(String filePathAndName) {
		String filePath = filePathAndName;
		File myFilePath = new File(filePath);
		if (myFilePath.exists()) {
			return true;
		}
		return false;
	}

	/**
	 * 解析缩进
	 * 
	 * @param indentNum
	 *            缩进数
	 */
	public StringBuffer parseIndent(int indentNum) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < indentNum; i++) {
			sb.append("\t");
		}
		return sb;
	}

	/**
	 * 创建目录
	 * 
	 * @param filePath
	 */
	public void createdir(String filePath) {
		File dirFile = new File(filePath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
	}

	public String readFile(String filePathAndName) {
		File file = new File(filePathAndName);
		StringBuffer stringBuffer = new StringBuffer();
		try {
			if (file.exists()) {
				BufferedReader bufferedReader = new BufferedReader(
						new FileReader(file));

				int i;
				while ((i = bufferedReader.read()) != -1) {
					stringBuffer.append((char) i);
				}

			} else {
				return null;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuffer.toString();
	}
}
