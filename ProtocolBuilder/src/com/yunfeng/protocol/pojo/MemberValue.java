package com.yunfeng.protocol.pojo;

import com.yunfeng.protocol.util.ConstantUtil;

/**
 * 成员变量
 */
public class MemberValue {
	public static final int PUBLIC_LIMIT = 0;
	public static final int PROTECTED_LIMIT = 1;
	public static final int PRIVATE_LIMIT = 2;

	private int limit = 0;// 权限
	private String name;
	private String type;
	private boolean staticValue = false;
	private boolean finalValue = false;
	private String defaultValue;

	public MemberValue(int limit, boolean isStatic, boolean isFinal,
			String type, String name) {
		this.limit = limit;
		this.staticValue = isStatic;
		this.finalValue = isFinal;
		this.type = type;
		this.name = name;
	}

	public MemberValue(int limit, boolean isStatic, boolean isFinal,
			String type, String name, String defaultValue) {
		this.limit = limit;
		this.staticValue = isStatic;
		this.finalValue = isFinal;
		this.type = type;
		this.name = name;
		this.defaultValue = defaultValue;
	}

	public MemberValue(int limit, String type, String name) {
		this.limit = limit;
		this.type = type;
		this.name = name;
	}

	public MemberValue(int limit, String type, String name, String defaultValue) {
		this.limit = limit;
		this.type = type;
		this.name = name;
		this.defaultValue = defaultValue;
	}

	public StringBuffer write(StringBuffer s, int indentNum) {

		s.append(ConstantUtil.getInstance().parseIndent(indentNum));

		switch (limit) {
		case PUBLIC_LIMIT:
			s.append("public ");
			break;
		case PROTECTED_LIMIT:
			break;
		case PRIVATE_LIMIT:
			s.append("private ");
			break;
		}
		if (staticValue) {
			s.append("static ");
		}
		if (finalValue) {
			s.append("final ");
		}
		s.append(type);
		s.append(" ");
		s.append(name);
		// s.append(" ");
		if (defaultValue != null) {
			s.append(" ");
			s.append("=");
			s.append(" ");
			s.append(defaultValue);
		}
		s.append(";\n");
		return s;
	}

	public boolean isStaticValue() {
		return staticValue;
	}

	public void setStaticValue(boolean staticValue) {
		this.staticValue = staticValue;
	}

	public boolean isFinalValue() {
		return finalValue;
	}

	public void setFinalValue(boolean finalValue) {
		this.finalValue = finalValue;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
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
}
