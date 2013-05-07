package com.yunfeng.protocol.pojo;

import java.util.List;

public class Protocol {
	private String module;
	private boolean javaCreated;
	private boolean asCreated;

	private List<RequestMessage> requestMessageList;
	private List<ResponseMessage> responseMessageList;
	private List<Struct> structList;

	public boolean getJavaCreated() {
		return javaCreated;
	}

	public void setJavaCreated(boolean javaCreated) {
		this.javaCreated = javaCreated;
	}

	public boolean getAsCreated() {
		return asCreated;
	}

	public void setAsCreated(boolean asCreated) {
		this.asCreated = asCreated;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public List<RequestMessage> getRequestMessageList() {
		return requestMessageList;
	}

	public void setRequestMessageList(List<RequestMessage> requestMessageList) {
		this.requestMessageList = requestMessageList;
	}

	public List<ResponseMessage> getResponseMessageList() {
		return responseMessageList;
	}

	public void setResponseMessageList(List<ResponseMessage> responseMessageList) {
		this.responseMessageList = responseMessageList;
	}

	public List<Struct> getStructList() {
		return structList;
	}

	public void setStructList(List<Struct> structList) {
		this.structList = structList;
	}
}
