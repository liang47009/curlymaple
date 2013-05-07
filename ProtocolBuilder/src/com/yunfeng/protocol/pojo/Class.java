package com.yunfeng.protocol.pojo;

import java.util.ArrayList;
import java.util.List;

import com.yunfeng.protocol.util.ConstantUtil;

/**
 * 类
 */
public class Class {
	public static final int PUBLIC_LIMIT = 0;
	public static final int PROTECTED_LIMIT = 1;
	public static final int PRIVATE_LIMIT = 2;

	private boolean finalClass = false;
	private boolean staticClass = false;
	/**
	 * 类权限
	 */
	private int limit = PUBLIC_LIMIT;

	/**
	 * 类名
	 */
	private String name;
	/**
	 * 注解
	 */
	private String annotation;

	/**
	 * 成员变量列表
	 */
	private List<MemberValue> memberValueList = new ArrayList<MemberValue>();

	/**
	 * 方法列表
	 */
	private List<Method> methodList = new ArrayList<Method>();

	/**
	 * 类实现的接口
	 */
	private List<String> interfaceList = new ArrayList<String>();

	/**
	 * 内部类列表
	 */
	private List<Class> innerClassList = new ArrayList<Class>();

	/**
	 * 类包
	 */
	private String packageName;

	private String classPath;

	public Class(String name, int limit) {
		this.name = name;
		this.limit = limit;
	}

	/**
	 * 将类写入字符串中
	 * 
	 * @param s
	 * @param indentNum
	 *            缩进数
	 * @return
	 */
	public StringBuffer write(StringBuffer s, int indentNum) {
		if (null != annotation && !"".equals(annotation)) {
			s.append("@");
			s.append(annotation);
			s.append("\r\n");
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

		if (staticClass) {
			s.append("static ");
		}

		if (finalClass) {
			s.append("final ");
		}
		s.append("class ");
		s.append(name);
		s.append(" ");

		if (interfaceList.size() > 0) {
			s.append("implements ");
			s.append(interfaceList.get(0));
		}
		s.append("{\n");

		for (MemberValue memberValue : memberValueList) {
			memberValue.write(s, indentNum + 1);
		}

		s.append("\n");

		for (Method method : methodList) {
			method.write(s, indentNum + 1);
		}

		s.append("}");
		return s;
	}

	/**
	 * 给类增加成员变量
	 * 
	 * @param memberValue
	 */
	public void addMemberValue(MemberValue memberValue) {
		memberValueList.add(memberValue);
	}

	/**
	 * 给类增加函数
	 * 
	 * @param method
	 */
	public void addMethod(Method method) {
		methodList.add(method);
	}

	/**
	 * 增加构造函数
	 */
	public Method addConstructMethod(int limit) {
		Method constructMethod = new Method(null, limit, name, "", "");
		addMethod(constructMethod);
		return constructMethod;
	}

	/**
	 * 增加接口
	 * 
	 * @param interfaceName
	 */
	public void addInterface(String interfaceName) {
		interfaceList.add(interfaceName);
	}

	/**
	 * 生成getter和setter方法
	 */
	public void createGetterSetter() {
		for (MemberValue memberValue : memberValueList) {
			if (!memberValue.isFinalValue() && !memberValue.isStaticValue()) {
				// 成员变量生成getter和setter方法
				createGetter(memberValue);
				createSetter(memberValue);
			} else if (memberValue.isFinalValue() && !memberValue.isStaticValue()) {
				createGetter(memberValue);
			}
		}
	}

	/**
	 * 给类增加单例模式 isAll true 全单例，false半单例
	 */
	public Method createSingleton(boolean isAll) {
		MemberValue instanceValue = new MemberValue(Method.PRIVATE_LIMIT, true,
				false, name, "instance", "new " + name + "()");
		this.addMemberValue(instanceValue);
		Method getInstanceMethod = new Method(Method.PUBLIC_LIMIT,
				"getInstance", name, "return instance;", true, false);
		this.addMethod(getInstanceMethod);
		if (isAll) {
			Method constructMethod = this
					.addConstructMethod(Method.PRIVATE_LIMIT);

			return constructMethod;
		} else {
			Method constructMethod = this
					.addConstructMethod(Method.PUBLIC_LIMIT);

			return constructMethod;
		}

	}

	private void createGetter(MemberValue memberValue) {
		String methodContent = "return " + memberValue.getName() + ";";
		Method method = new Method(null, PUBLIC_LIMIT, ConstantUtil
				.getInstance().getFieldGetterMethodName(memberValue.getName()),
				memberValue.getType(), methodContent);
		addMethod(method);
	}

	private void createSetter(MemberValue memberValue) {
		String methodContent = "this." + memberValue.getName() + " = "
				+ memberValue.getName() + ";";
		Method method = new Method("Resource", PUBLIC_LIMIT, ConstantUtil
				.getInstance().getFieldSetterMethodName(memberValue.getName()),
				"void", methodContent);
		method.addMethodParam(new MethodParam(memberValue.getType(),
				memberValue.getName()));
		addMethod(method);
	}

	public boolean isFinalClass() {
		return finalClass;
	}

	public void setFinalClass(boolean finalClass) {
		this.finalClass = finalClass;
	}

	public boolean isStaticClass() {
		return staticClass;
	}

	public void setStaticClass(boolean staticClass) {
		this.staticClass = staticClass;
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

	public List<MemberValue> getMenberValueList() {
		return memberValueList;
	}

	public void setMenberValueList(List<MemberValue> menberValueList) {
		this.memberValueList = menberValueList;
	}

	public List<Method> getMethodList() {
		return methodList;
	}

	public void setMethodList(List<Method> methodList) {
		this.methodList = methodList;
	}

	public List<Class> getInnerClassList() {
		return innerClassList;
	}

	public void setInnerClassList(List<Class> innerClassList) {
		this.innerClassList = innerClassList;
	}

	public List<MemberValue> getMemberValueList() {
		return memberValueList;
	}

	public void setMemberValueList(List<MemberValue> memberValueList) {
		this.memberValueList = memberValueList;
	}

	public List<String> getInterfaceList() {
		return interfaceList;
	}

	public void setInterfaceList(List<String> interfaceList) {
		this.interfaceList = interfaceList;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getClassPath() {
		return classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	public String getAnnotation() {
		return annotation;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}
}
