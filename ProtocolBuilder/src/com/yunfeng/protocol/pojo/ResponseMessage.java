package com.yunfeng.protocol.pojo;

import java.util.ArrayList;

/**
 * 服务器端发给客户端的消息
 */
public class ResponseMessage {
	private int id;
	private String name;
	private String method;
	private String desc;
	private String module;
	private String byteInt;
	private ArrayList<Param> paramList;

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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getByteInt() {
		return byteInt;
	}

	public void setByteInt(String byteInt) {
		this.byteInt = byteInt;
	}

	public ArrayList<Param> getParamList() {
		return paramList;
	}

	public void setParamList(ArrayList<Param> paramList) {
		this.paramList = paramList;
	}
}
