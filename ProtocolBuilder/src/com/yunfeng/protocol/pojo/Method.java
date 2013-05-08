package com.yunfeng.protocol.pojo;

import java.util.ArrayList;
import java.util.List;

import com.yunfeng.protocol.util.ConstantUtil;

/**
 * 方法类
 */
public class Method {
	public static final int PUBLIC_LIMIT = 0;
	public static final int PROTECTED_LIMIT = 1;
	public static final int PRIVATE_LIMIT = 2;

	/**
	 * 权限,public protected,private
	 */
	private int limit;
	/**
	 * 方法名
	 */
	private String name;
	/**
	 * 返回值
	 */
	private String returnType;
	/**
	 * 注解
	 */
	private String annotation;
	/**
	 * 方法内容
	 */
	private String contant;

	/**
	 * 方法参数列表
	 */
	private List<MethodParam> paramList = new ArrayList<MethodParam>();

	private boolean finalMethod = false;

	private boolean staticMethod = false;

	private boolean synchronizeMethod = false;

	/**
	 * 缩进数
	 */
	private int indentNum;

	public Method(String annotation, int limit, String name, String returnType,
			String contant) {
		this.annotation = annotation;
		this.limit = limit;
		this.name = name;
		this.returnType = returnType;
		this.contant = contant;
	}

	public Method(int limit, String name, String returnType, String contant,
			boolean staticMethod, boolean finalMethod) {
		this.limit = limit;
		this.name = name;
		this.returnType = returnType;
		this.contant = contant;
		this.staticMethod = staticMethod;
		this.finalMethod = finalMethod;
	}

	/**
	 * 将方法写入到字符串中
	 * 
	 * @param s
	 * @param indentNum
	 *            缩进数
	 * @return
	 */
	public StringBuffer write(StringBuffer s, int indentNum) {
		this.indentNum = indentNum;

		s.append(ConstantUtil.getInstance().parseIndent(indentNum));
		if (null != annotation && !"".equals(annotation)) {
			s.append("@");
			s.append(annotation);
			s.append(" ");
		}
		switch (limit) {
		case PUBLIC_LIMIT:
			s.append("public ");
			break;
		case PRIVATE_LIMIT:
			s.append("private ");
			break;
		case PROTECTED_LIMIT:
			s.append("protected ");
			break;
		}

		if (synchronizeMethod) {
			s.append("synchronized ");
		}
		if (staticMethod) {
			s.append("static ");
		}
		if (finalMethod) {
			s.append("final ");
		}
		s.append(returnType);
		if (!"".endsWith(returnType)) {
			s.append(" ");
		}
		s.append(name);
		s.append("(");

		for (int i = 0; i < paramList.size(); i++) {
			s.append(paramList.get(i).getType());
			s.append(" ");
			s.append(paramList.get(i).getName());
			if (i != paramList.size() - 1) {
				s.append(", ");
			}
		}

		s.append(") {\n");
		s.append(ConstantUtil.getInstance().parseIndent(indentNum + 1));
		s.append(contant);
		s.append("\n");
		s.append(ConstantUtil.getInstance().parseIndent(indentNum));
		s.append("}\n");

		return s;
	}

	public boolean isSynchronizeMethod() {
		return synchronizeMethod;
	}

	public void setSynchronizeMethod(boolean synchronizeMethod) {
		this.synchronizeMethod = synchronizeMethod;
	}

	/**
	 * 方法内容换行
	 * 
	 * @return 换行字符串
	 */
	public StringBuffer contentFeed(int feedNum) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		for (int i = 0; i < indentNum + 2 + feedNum; i++) {
			sb.append("\t");
		}
		return sb;
	}

	public void addMethodParam(MethodParam methodParam) {
		paramList.add(methodParam);
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

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getContant() {
		return contant;
	}

	public void setContant(String contant) {
		this.contant = contant;
	}

	public boolean isFinalMethod() {
		return finalMethod;
	}

	public void setFinalMethod(boolean finalMethod) {
		this.finalMethod = finalMethod;
	}

	public boolean isStaticMethod() {
		return staticMethod;
	}

	public void setStaticMethod(boolean staticMethod) {
		this.staticMethod = staticMethod;
	}

	public List<MethodParam> getParamList() {
		return paramList;
	}

	public void setParamList(List<MethodParam> paramList) {
		this.paramList = paramList;
	}

	public String getAnnotation() {
		return annotation;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}
}
