package com.yunfeng.protocol.util;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 提示信息类
 * 
 * @author xialiangliang
 * 
 */
public class PromptMessage implements Serializable {
	/* serail version uid */
	private static final long serialVersionUID = 4841183980762719179L;
	private static PromptMessage instance = null;
	private long fileTime;
	private Properties messages = null;
	private File file = null;

	public static final PromptMessage getInstance() {
		if (instance == null) {
			instance = new PromptMessage();
		}
		return instance;
	}

	public static void main(String[] args) {
		PromptMessage.getInstance().init("prompt.properties");
		String result = PromptMessage.getInstance().getProperty(
				"role_show_increment_experience", "12");
		System.out.println(result);
	}

	/**
	 * 初始化
	 */
	public void init(String filePath) {
		try {
			URL url = Thread.currentThread().getContextClassLoader()
					.getResource(filePath);
			file = new File(url.getFile());
			messages = new Properties();
			messages.load(ClassLoader.getSystemResourceAsStream(file.getName()));
			fileTime = file.lastModified();
		} catch (IOException e) {
			Log.error("Reload properties error!", e);
		}
	}

	/**
	 * parse the format as ${..} variables
	 * 
	 * @param field
	 * @return list
	 */
	private List<String> parseVaraibles(String field) {
		List<String> variables = new ArrayList<String>();

		if (StringUtils.isBlank(field)) {
			return null;
		}

		int len = field.length();
		char keyChar;
		boolean iskey = false;
		StringBuffer tempStr = new StringBuffer();
		for (int i = 0; i < len; i++) {
			keyChar = field.charAt(i);

			if (keyChar == '$') {
				if ((++i < len) && (field.charAt(i) == '{')) {
					iskey = true;
					tempStr.setLength(0);
				}
			} else if (keyChar == '}') {
				if (tempStr.length() > 0) {
					variables.add(tempStr.toString());
				}
				iskey = false;
			} else {
				if (iskey) {
					tempStr.append(keyChar);
				}
			}
		}
		return variables;
	}

	private void reloadOnchange() {
		try {
			long fileLastModifytime = file.lastModified();
			if (fileLastModifytime != fileTime) {
				if (messages != null) {
					messages.clear();
					messages.load(Thread.currentThread()
							.getContextClassLoader()
							.getResourceAsStream("prompt.properties"));
					fileTime = file.lastModified();
				}
			}
		} catch (IOException e) {
			Log.error("Reload properties error!", e);
		}
	}

	/**
	 * 获取消息内容
	 * 
	 * @param key
	 * @param valueArgs
	 * @return
	 */
	public String getProperty(String key, String... valueArgs) {
		if (valueArgs == null) {
			return null;
		}
		reloadOnchange();
		if (messages == null) {
			return null;
		}
		String value = messages.getProperty(key);
		if (value == null) {
			return null;
		}
		try {
			value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.error("Properties file change code error!", e);
		}
		if (value.indexOf("${") != -1) {
			List<String> variables = parseVaraibles(value);
			if (variables != null) {
				int variabesNum = variables.size();
				for (int i = 0; i < variabesNum; i++) {
					String variableKey = variables.get(i);
					if (variableKey != null && !variableKey.equals(key)) {
						String args = "";
						if (valueArgs.length >= variabesNum) {
							args = valueArgs[i];
						}
						value = StringUtils.replace(value, "${" + variableKey
								+ "}", args == null ? "" : args);
					}
				}
			}
		}
		return value;
	}

}
