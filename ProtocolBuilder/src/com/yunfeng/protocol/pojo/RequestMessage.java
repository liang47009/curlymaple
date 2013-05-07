package com.yunfeng.protocol.pojo;

import java.util.ArrayList;

/**
 * 客户端发给服务器的消息
 * 
 */
public class RequestMessage {
	private int id;
	private String name;
	private String method;
	private String module;
	private String desc;
	private boolean created;
	private ArrayList<Param> paramList;
	private String classPath;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public ArrayList<Param> getParamList() {
		return paramList;
	}

	public void setParamList(ArrayList<Param> paramList) {
		this.paramList = paramList;
	}

	public boolean getCreated() {
		return created;
	}

	public void setCreated(boolean created) {
		this.created = created;
	}

	public String getClassPath() {
		return classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}
}
