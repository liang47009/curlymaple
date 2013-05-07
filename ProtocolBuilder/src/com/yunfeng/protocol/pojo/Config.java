package com.yunfeng.protocol.pojo;

public class Config {
	/**
	 * 协议名称
	 */
	private String protocolName;

	/**
	 * 生成java文件的包路径
	 */
	private String modulePackage;

	private String javaProjectPath;

	private String xmlPath;

	private String testProjectPath;

	public String getTestProjectPath() {
		return testProjectPath;
	}

	public void setTestProjectPath(String testProjectPath) {
		this.testProjectPath = testProjectPath;
	}

	public String getJavaProjectPath() {
		return javaProjectPath;
	}

	public void setJavaProjectPath(String javaProjectPath) {
		this.javaProjectPath = javaProjectPath;
	}

	public String getProtocolName() {
		return protocolName;
	}

	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}

	public String getModulePackage() {
		return modulePackage;
	}

	public void setModulePackage(String modulePackage) {
		this.modulePackage = modulePackage;
	}

	public String getXmlPath() {
		return xmlPath;
	}

	public void setXmlPath(String xmlPath) {
		this.xmlPath = xmlPath;
	}
}
