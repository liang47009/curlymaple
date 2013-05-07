package com.yunfeng.protocol.pojo;

import java.util.ArrayList;

/**
 * 协议结构体
 * 
 */
public class Struct {
	private int id;
	private String name;
	private ArrayList<Param> paramList;
	private String module;
	private String byteInt;

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
