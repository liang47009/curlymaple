package com.yunfeng.protocol.pojo;

/**
 * 协议参数
 */
public class Param {
	private int id;
	private String name;
	private String type;
	private String desc;
	private String struct;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getStruct() {
		return struct;
	}

	public void setStruct(String struct) {
		this.struct = struct;
	}

	/**
	 * 方法内容换行
	 * 
	 * @return 换行字符串
	 */
	public StringBuffer contentFeed(int feedNum) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		for (int i = 0; i < 2 + feedNum; i++) {
			sb.append("\t");
		}
		return sb;
	}
}
