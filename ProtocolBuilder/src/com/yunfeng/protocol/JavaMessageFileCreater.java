package com.yunfeng.protocol;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.yunfeng.protocol.pojo.Class;
import com.yunfeng.protocol.pojo.Config;
import com.yunfeng.protocol.pojo.JavaFile;
import com.yunfeng.protocol.pojo.MemberValue;
import com.yunfeng.protocol.pojo.Method;
import com.yunfeng.protocol.pojo.MethodParam;
import com.yunfeng.protocol.pojo.Param;
import com.yunfeng.protocol.pojo.RequestMessage;
import com.yunfeng.protocol.pojo.ResponseMessage;
import com.yunfeng.protocol.pojo.Struct;
import com.yunfeng.protocol.util.ConstantUtil;

public class JavaMessageFileCreater {
	/**
	 * 客户端请求的消息集合
	 */
	private List<RequestMessage> requestMessageList;

	/**
	 * 服务器发送的消息集合
	 */
	private List<ResponseMessage> responseMessageList;

	/**
	 * 协议中的结构体集合
	 */
	private List<Struct> structList;

	/**
	 * 配置
	 */
	private Config config;

	/**
	 * 类列表
	 */
	private List<Class> classList = new ArrayList<Class>();

	public JavaMessageFileCreater(List<RequestMessage> requestMessageList,
			List<ResponseMessage> responseMessageList, List<Struct> structList,
			Config config) {
		this.requestMessageList = requestMessageList;
		this.responseMessageList = responseMessageList;
		this.structList = structList;
		this.config = config;
	}

	/**
	 * 生成消息文件
	 */
	public List<Class> createMessageFile(int command, int messageId) {
		List<String> structNames = new ArrayList<String>();
		// 生成客户端到服务器端的消息文件
		for (RequestMessage requestMessage : requestMessageList) {
			String name;
			String packageName;
			JavaFile file;

			String module = requestMessage.getModule();
			if (command == BuildProtocol.TEST_MESSAGE_COMMAND) {
				packageName = "com.yunfeng.game.message";
				name = ConstantUtil.getInstance().getStringHeadUp(module)
						+ ConstantUtil.getInstance().getStringHeadUp(
								requestMessage.getName());
				file = new JavaFile(name, packageName,
						config.getTestProjectPath());
			} else {
				packageName = "com.yunfeng.game.module." + module + ".message";
				name = ConstantUtil.getInstance().getStringHeadUp(
						requestMessage.getName());
				file = new JavaFile(name, packageName,
						config.getJavaProjectPath());
			}

			file.addImport("java.util.List");
			file.addImport("com.yunfeng.game.core.IC2SCommand");
			file.addImport("com.yunfeng.game.core.MemoryData");
			file.addImport("org.jboss.netty.buffer.ChannelBuffer");
			file.addImport("com.yunfeng.protocol.netty.BufferReader");
			file.addImport("javax.annotation.Resource");
			file.addImport("org.springframework.stereotype.Controller");
			file.write();

			Class requestMessageClass = new Class(name, Class.PUBLIC_LIMIT);
			requestMessageClass.setAnnotation("Controller");
			requestMessageClass.addInterface("IC2SCommand");
			requestMessageClass.setClassPath(packageName + "." + name);
			requestMessage.setClassPath(requestMessageClass.getClassPath());

			String serviceName = module + "Service";
			module = ConstantUtil.getInstance().getStringHeadUp(module);

			requestMessageClass
					.addMemberValue(new MemberValue(MemberValue.PRIVATE_LIMIT,
							module + "Service", serviceName));

			// if(requestMessage.getParamList()!=null){
			// addC2SMessageParamToClass(requestMessageClass,requestMessage);
			// }
			// createC2SConstructMethod(requestMessageClass,
			// requestMessage.getParamList());
			requestMessageClass.createGetterSetter();
			Method method = new Method(null, Method.PUBLIC_LIMIT, "execute",
					"void", "");
			ArrayList<Param> members = requestMessage.getParamList();
			String className = requestMessageClass.getName();
			String methodName = ConstantUtil.getInstance().getStringHeadDown(className.substring(0, className.indexOf("_")));
			serviceName = serviceName + "." + methodName;
			createAreaMember(method, members, serviceName);
			requestMessageClass.addMethod(method);

			if (command == BuildProtocol.CREATE_NEW_MESSAGE_COMMAND
					&& !ConstantUtil.getInstance().checkFileExite(
							file.getPathName())) {
				System.out.println("生成C2S协议：" + className);
				// ConstantUtil.getInstance().createdir(file.getPath().toString());
				requestMessageClass.write(file.getStringBuffer(), 0);
				file.createFile();
			} else if (command == BuildProtocol.MODIFY_MESSAGE_COMMAND
					&& messageId == requestMessage.getId()) {
				System.out.println("修改协议：" + className);
				saveOldExcuteMethod(file, requestMessageClass);
				// ConstantUtil.getInstance().createdir(file.getPath().toString());
				requestMessageClass.write(file.getStringBuffer(), 0);
				file.createFile();
				structNames = getRelateStructName(requestMessage);
			} else if (command == BuildProtocol.TEST_MESSAGE_COMMAND) {
				System.out.println("生成C2S协议：" + className);
				// ConstantUtil.getInstance().createdir(file.getPath().toString());
				requestMessageClass.write(file.getStringBuffer(), 0);
				file.createFile();
			}
			// 加入类列表
			classList.add(requestMessageClass);
		}

		// 生成服务器端到客户端的消息文件
		for (ResponseMessage responseMessage : responseMessageList) {
			String name;
			String packageName;
			JavaFile file;

			if (command == BuildProtocol.TEST_MESSAGE_COMMAND) {
				packageName = "com.yunfeng.game.message";
				name = ConstantUtil.getInstance().getStringHeadUp(
						responseMessage.getModule())
						+ ConstantUtil.getInstance().getStringHeadUp(
								responseMessage.getName());
				file = new JavaFile(name, packageName,
						config.getTestProjectPath());
			} else {
				packageName = "com.yunfeng.game.module."
						+ responseMessage.getModule() + ".message";
				name = ConstantUtil.getInstance().getStringHeadUp(
						responseMessage.getName());
				file = new JavaFile(name, packageName,
						config.getJavaProjectPath());
			}
			file.addImport("java.util.*");
			file.addImport("com.yunfeng.game.core.*");
			file.addImport("org.jboss.netty.buffer.*");
			file.addImport("com.yunfeng.protocol.netty.*");
			file.addImport("org.jboss.netty.channel.Channel");
			file.write();

			Class responseMessageClass = new Class(name, Class.PUBLIC_LIMIT);

			responseMessageClass.addInterface("IS2CCommand");

			MemberValue messageIdMember = new MemberValue(
					MemberValue.PRIVATE_LIMIT, false, true, "short",
					"messageId", responseMessage.getId() + "");
			responseMessageClass.addMemberValue(messageIdMember);
			if (responseMessage.getParamList() != null) {
				addS2CMessageParamToClass(responseMessageClass, responseMessage);

				if (responseMessage.getParamList().size() > 0) {
					createConstructMethod(responseMessageClass,
							responseMessage.getParamList());
				}
			}

			// 加入单例
			responseMessageClass.createSingleton(true);

			createSendMethod(responseMessageClass,
					responseMessage.getParamList(),
					responseMessage.getByteInt());
			// responseMessageClass.createGetterSetter();
			responseMessageClass.setClassPath(packageName + "." + name);
			responseMessageClass.write(file.getStringBuffer(), 0);

			if (command == BuildProtocol.CREATE_NEW_MESSAGE_COMMAND
					&& !ConstantUtil.getInstance().checkFileExite(
							file.getPathName())) {
				System.out.println("生成S2C协议：" + responseMessageClass.getName());
				// ConstantUtil.getInstance().createdir(file.getPath().toString());
				file.createFile();
			} else if (command == BuildProtocol.MODIFY_MESSAGE_COMMAND
					&& messageId == responseMessage.getId()) {
				System.out.println("修改协议：" + responseMessageClass.getName());
				// ConstantUtil.getInstance().createdir(file.getPath().toString());
				file.createFile();
				structNames = getRelateStructName(responseMessage);
			} else if (command == BuildProtocol.TEST_MESSAGE_COMMAND) {
				System.out.println("生成S2C协议：" + responseMessageClass.getName());
				// ConstantUtil.getInstance().createdir(file.getPath().toString());
				file.createFile();
			} else if (command == BuildProtocol.REFRESH_MESSAGE_COMMAND) {
				file.createFile();
			}
		}

		// 生成结构体文件
		for (Struct struct : structList) {
			String name;
			String packageName;
			JavaFile file;

			if (command == BuildProtocol.TEST_MESSAGE_COMMAND) {
				packageName = "com.yunfeng.game.message";
				name = convertStructClass(struct.getName());
				file = new JavaFile(name, packageName,
						config.getTestProjectPath());
			} else {
				packageName = "com.yunfeng.game.module." + struct.getModule()
						+ ".message";
				name = convertStructClass(struct.getName());
				file = new JavaFile(name, packageName,
						config.getJavaProjectPath());
			}

			file.addImport("org.jboss.netty.buffer.ChannelBuffer");
			file.addImport("com.yunfeng.game.core.*");
			file.addImport("java.util.*");
			file.write();
			Class structClass = new Class(name, Class.PUBLIC_LIMIT);
			structClass.addInterface("ISendable");
			createStructConstructMethod(structClass, struct.getParamList());
			createConstructMethod(structClass, struct.getParamList());
			addStructParamToClass(structClass, struct);
			structClass.createGetterSetter();
			structClass.setClassPath(packageName + "." + name);
			addStructSendMethod(structClass, struct.getByteInt());

			structClass.write(file.getStringBuffer(), 0);

			if (command == BuildProtocol.CREATE_NEW_MESSAGE_COMMAND
					&& !ConstantUtil.getInstance().checkFileExite(
							file.getPathName())) {
				System.out.println("生成结构体：" + structClass.getName());
				// ConstantUtil.getInstance().createdir(file.getPath().toString());
				file.createFile();
			} else if (command == BuildProtocol.MODIFY_MESSAGE_COMMAND) {
				for (String structName : structNames) {
					if (structName.equals(struct.getName())) {
						// ConstantUtil.getInstance().createdir(file.getPath().toString());
						file.createFile();
					}
				}

			} else if (command == BuildProtocol.TEST_MESSAGE_COMMAND) {
				// ConstantUtil.getInstance().createdir(file.getPath().toString());
				file.createFile();
			}
		}
		return classList;
	}

	/**
	 * 增加局部变量
	 * 
	 * @param method
	 * @param members
	 * @param serviceName 服务的方法名
	 */
	private void createAreaMember(Method method, ArrayList<Param> members, String serviceName) {
		ArrayList<MethodParam> paramList = new ArrayList<MethodParam>();
		paramList.add(new MethodParam("ChannelBuffer", "channelBuffer"));
		paramList.add(new MethodParam("MemoryData", "memoryData"));
		method.setParamList(paramList);

		if (members != null) {
			StringBuffer sb = new StringBuffer();
			StringBuffer temp = new StringBuffer();
			boolean hasSize = false;
			temp.append(serviceName);
			temp.append("(memoryData, ");
			Iterator<Param> it = members.iterator();
			for (;it.hasNext();) {
				Param param = it.next();
				String type = param.getType();
				if (type.equals("list")) {
					if (!hasSize) {
						sb.append("short size = BufferReader.readShort(channelBuffer);");
						hasSize = true;
					} else {
						sb.append(convertType(type, null));
						sb.append(" size = BufferReader.readShort(channelBuffer);");
					}
					sb.append(param.contentFeed(0));
					sb.append("List<ISendable> ");
					sb.append(param.getName());
					sb.append(" = ");
					sb.append("new ArrayList<ISendable>();");
					sb.append(param.contentFeed(0));
					sb.append("for(int i= 0;i<size;i++){");
					sb.append(param.contentFeed(1));
					sb.append(param.getName());
					sb.append(".add(new ");
					sb.append(convertStructClass(param.getStruct()));
					sb.append("(channelBuffer));");
					sb.append(param.contentFeed(0));
					sb.append("}");
				} else if (type.equals("struct")) {

				} else {
					sb.append(convertType(type, null));
					sb.append(" ");
					sb.append(param.getName());
					sb.append(" = BufferReader.read");
					sb.append(ConstantUtil.getInstance().getStringHeadUp(type));
					sb.append("(channelBuffer);");
					sb.append(param.contentFeed(0));
				}
				if (it.hasNext()) {
					temp.append(", ");
				}
				temp.append(param.getName());
			}
			temp.append(");");
			sb.append(temp);
			method.setContant(sb.toString());
		}
	}

	/**
	 * 在客户端到服务端消息类中增加消息参数
	 * 
	 * @param messageClass
	 * @param paramList
	 */
	private void addC2SMessageParamToClass(Class messageClass,
			RequestMessage requestMessage) {
		MemberValue paramValue;
		for (Param param : requestMessage.getParamList()) {
			if (!checkParamType(param)) {
				System.out.println("参数类型错误：Id = " + requestMessage.getId()
						+ ",name = " + requestMessage.getName()
						+ ",paramName = " + param.getName());
				continue;
			}

			if (param.getType().equals("list")) {
				paramValue = new MemberValue(MemberValue.PRIVATE_LIMIT,
						convertType(param.getType(), param.getStruct()),
						param.getName(), "new ArrayList()");
			} else {
				paramValue = new MemberValue(MemberValue.PRIVATE_LIMIT,
						convertType(param.getType(), param.getStruct()),
						param.getName());
			}
			messageClass.addMemberValue(paramValue);
		}
	}

	private boolean checkParamType(Param param) {
		if (!param.getType().equals("string")
				&& !param.getType().equals("String")
				&& !param.getType().equals("int")
				&& !param.getType().equals("double")
				&& !param.getType().equals("boolean")
				&& !param.getType().equals("list")
				&& !param.getType().equals("short")) {
			return false;
		}
		return true;
	}

	/**
	 * 在服务器端到客户端的消息类中增加消息参数
	 * 
	 * @param messageClass
	 * @param paramList
	 */
	private void addS2CMessageParamToClass(Class messageClass,
			ResponseMessage responseMessage) {
		MemberValue paramValue;

		for (Param param : responseMessage.getParamList()) {
			if (!checkParamType(param)) {
				System.out.println("参数类型错误：Id = " + responseMessage.getId()
						+ ",name = " + responseMessage.getName()
						+ ",paramName = " + param.getName());
				continue;
			}
			paramValue = new MemberValue(MemberValue.PRIVATE_LIMIT,
					convertC2SType(param.getType(), param.getStruct()),
					param.getName());
			messageClass.addMemberValue(paramValue);
		}
	}

	/**
	 * 在结构体类中增加参数
	 * 
	 * @param structClass
	 * @param paramList
	 */
	private void addStructParamToClass(Class structClass, Struct struct) {
		if (struct.getParamList() != null) {
			for (Param param : struct.getParamList()) {
				MemberValue paramValue;
				if (!checkParamType(param)) {
					System.out.println("参数类型错误：Id = " + struct.getId()
							+ ",name = " + struct.getName() + ",paramName = "
							+ param.getName());
					continue;
				}
				if (param.getType().equals("list")) {
					paramValue = new MemberValue(MemberValue.PRIVATE_LIMIT,
							convertType(param.getType(), param.getStruct()),
							param.getName(), "new ArrayList()");
				} else {
					paramValue = new MemberValue(MemberValue.PRIVATE_LIMIT,
							convertType(param.getType(), param.getStruct()),
							param.getName());
				}
				structClass.addMemberValue(paramValue);
			}
		}

	}

	/**
	 * 创建客户端发到服务器消息的构造方法
	 * 
	 * @param messageClass
	 * @param paramList
	 */
	private void createC2SConstructMethod(Class messageClass,
			List<Param> paramList) {
		Method constructMethod = messageClass
				.addConstructMethod(Method.PUBLIC_LIMIT);
		constructMethod.addMethodParam(new MethodParam("ChannelBuffer",
				"channelBuffer"));
		constructMethod.addMethodParam(new MethodParam("MemoryData",
				"memoryData"));

		StringBuffer contentString = new StringBuffer();

		contentString.append("this.memoryData = memoryData;");
		contentString.append(constructMethod.contentFeed(0));

		if (paramList == null) {
			constructMethod.setContant(contentString.toString());
			return;
		}

		boolean hasSize = false;
		for (Param param : paramList) {
			contentString.append(constructMethod.contentFeed(0));

			if (param.getType().equals("list")) {
				if (!hasSize) {
					contentString
							.append("short size = BufferReader.readShort(channelBuffer);");
					hasSize = true;
				} else {
					contentString
							.append("size = BufferReader.readShort(channelBuffer);");
				}

				contentString.append(constructMethod.contentFeed(0));
				contentString.append("for(int i= 0;i<size;i++){");
				contentString.append(constructMethod.contentFeed(1));
				contentString.append(param.getName());
				contentString.append(".add(new ");
				contentString.append(convertStructClass(param.getStruct()));
				contentString.append("(channelBuffer));");
				contentString.append(constructMethod.contentFeed(0));
				contentString.append("}");
			} else if (param.getType().equals("struct")) {

			} else {
				contentString.append(param.getName());
				contentString.append(" = BufferReader.read");
				contentString.append(ConstantUtil.getInstance()
						.getStringHeadUp(param.getType()));
				contentString.append("(channelBuffer);");
			}
		}
		constructMethod.setContant(contentString.toString());
	}

	/**
	 * 创建消息的构造方法
	 * 
	 * @param messageClass
	 * @param paramList
	 */
	private void createConstructMethod(Class messageClass, List<Param> paramList) {
		Method constructMethod = messageClass
				.addConstructMethod(Method.PUBLIC_LIMIT);

		StringBuffer contentString = new StringBuffer();
		if (paramList != null) {
			for (Param param : paramList) {
				constructMethod.addMethodParam(new MethodParam(convertC2SType(
						param.getType(), param.getStruct()), param.getName()));
				contentString.append(constructMethod.contentFeed(0));

				contentString.append("this.");
				contentString.append(param.getName());
				contentString.append(" = ");
				contentString.append(param.getName());
				contentString.append(";");
			}
		}
		constructMethod.setContant(contentString.toString());
	}

	/**
	 * 创建结构体构造函数
	 * 
	 * @param messageClass
	 * @param paramList
	 */
	private void createStructConstructMethod(Class structClass,
			List<Param> paramList) {
		Method constructMethod = structClass
				.addConstructMethod(Method.PUBLIC_LIMIT);
		constructMethod.addMethodParam(new MethodParam("ChannelBuffer",
				"channelBuffer"));
		StringBuffer contentString = new StringBuffer();
		boolean hasSize = false;
		if (paramList != null) {
			for (Param param : paramList) {
				contentString.append(constructMethod.contentFeed(0));
				if (param.getType() == null) {
					System.out.println("$$" + structClass.getName());
				}

				if (param.getType().equals("list")) {
					if (!hasSize) {
						contentString
								.append("short size = BufferReader.readShort(channelBuffer);");
						hasSize = true;
					} else {
						contentString
								.append("size = BufferReader.readShort(channelBuffer);");
					}
					contentString.append(constructMethod.contentFeed(0));
					contentString.append("for(int i= 0;i<size;i++){");
					contentString.append(constructMethod.contentFeed(1));
					contentString.append(param.getName());
					contentString.append(".add(new ");
					contentString.append(convertStructClass(param.getStruct()));
					contentString.append("(channelBuffer));");
					contentString.append(constructMethod.contentFeed(0));
					contentString.append("}");
				} else {
					contentString.append(param.getName());
					contentString.append(" = BufferReader.read");
					contentString.append(ConstantUtil.getInstance()
							.getStringHeadUp(param.getType()));
					contentString.append("(channelBuffer);");
				}
			}
		}
		constructMethod.setContant(contentString.toString());
	}

	/**
	 * 创建发送消息的方法
	 * 
	 * @param messageClass
	 * @param paramList
	 */
	private void createSendMethod(Class messageClass, List<Param> paramList,
			String byteInt) {
		StringBuffer methodContent = new StringBuffer();
		Method sendMethod = new Method(null, Method.PUBLIC_LIMIT, "send",
				"void", "");
		messageClass.addMethod(sendMethod);
		sendMethod.addMethodParam(new MethodParam("Channel", "channel"));

		// 单例用的发送方法
		Method sendSingleMethod = new Method(null, Method.PUBLIC_LIMIT, "send",
				"void", "");
		if (paramList != null && paramList.size() > 0) {
			messageClass.addMethod(sendSingleMethod);
		}
		sendSingleMethod.addMethodParam(new MethodParam("Channel", "channel"));

		methodContent
				.append("ChannelBuffer channelBuffer = ChannelBuffers.dynamicBuffer(channel.getConfig().getBufferFactory());");
		methodContent.append(sendMethod.contentFeed(0));

		methodContent.append("int length = 0;");
		methodContent.append(sendMethod.contentFeed(0));

		methodContent
				.append("length += BufferWriter.writeShort(channelBuffer, messageId);");

		boolean byteInteB = false;
		if (byteInt != null && byteInt.equals("true")) {
			byteInteB = true;
		}

		String byteIntParams = "";

		if (paramList != null) {
			for (Param param : paramList) {
				String type = ConstantUtil.getInstance().getStringHeadUp(
						param.getType());
				sendSingleMethod.addMethodParam(new MethodParam(convertC2SType(
						param.getType(), param.getStruct()), param.getName()));
				if (type.equals("Int") && byteInteB) {
					byteIntParams += param.getName() + ",";
				} else {
					methodContent.append(sendMethod.contentFeed(0));
					methodContent.append("length += BufferWriter.write");
					methodContent.append(type);
					methodContent.append("(channelBuffer,");
					methodContent.append(param.getName());
					methodContent.append(");");
				}
			}
		}

		if (byteInteB && !byteIntParams.equals("")) {
			byteIntParams = byteIntParams.substring(0,
					byteIntParams.length() - 1);

			methodContent.append(sendMethod.contentFeed(0));
			methodContent
					.append("byte[] bytes = CompactByteArray.getInstance().writeInts("
							+ byteIntParams + ");");

			methodContent.append(sendMethod.contentFeed(0));
			methodContent.append("length += BufferWriter.write");
			methodContent.append("Bytes");
			methodContent.append("(channelBuffer,");
			methodContent.append("bytes");
			methodContent.append(");");
		}

		methodContent.append(sendMethod.contentFeed(0));
		methodContent
				.append("channelBuffer = ChannelBuffers.copiedBuffer(channelBuffer.array(), 0, length);");
		methodContent.append(sendMethod.contentFeed(0));
		methodContent.append("channel.write(channelBuffer);");
		methodContent.append(sendMethod.contentFeed(0));

		sendMethod.setContant(methodContent.toString());
		sendSingleMethod.setContant(methodContent.toString());
	}

	private void addStructSendMethod(Class structClass, String byteInt) {
		StringBuffer methodContent = new StringBuffer();
		Method sendMethod = new Method(null, Method.PUBLIC_LIMIT, "send",
				"int", "");
		structClass.addMethod(sendMethod);
		sendMethod.addMethodParam(new MethodParam("ChannelBuffer",
				"channelBuffer"));

		methodContent.append(sendMethod.contentFeed(0));
		methodContent.append("int length=0;");

		boolean byteInteB = false;
		if (byteInt != null && byteInt.equals("true")) {
			byteInteB = true;
		}

		String byteIntParams = "";

		for (MemberValue memberValue : structClass.getMemberValueList()) {
			String type = ConstantUtil.getInstance().getStringHeadUp(
					memberValue.getType());
			if (type.equals("Int") && byteInteB) {
				byteIntParams += memberValue.getName() + ",";
			} else {
				methodContent.append(sendMethod.contentFeed(0));
				methodContent.append("length += BufferWriter.write");
				methodContent.append(ConstantUtil.getInstance()
						.getStringHeadUp(convertType(memberValue.getType())));
				methodContent.append("(channelBuffer,");
				methodContent.append(memberValue.getName());
				methodContent.append(");");
			}
		}

		if (byteInteB && !byteIntParams.equals("")) {
			byteIntParams = byteIntParams.substring(0,
					byteIntParams.length() - 1);

			methodContent.append(sendMethod.contentFeed(0));
			methodContent
					.append("byte[] bytes = CompactByteArray.getInstance().writeInts("
							+ byteIntParams + ");");

			methodContent.append(sendMethod.contentFeed(0));
			methodContent.append("length += BufferWriter.write");
			methodContent.append("Bytes");
			methodContent.append("(channelBuffer,");
			methodContent.append("bytes");
			methodContent.append(");");
		}

		methodContent.append(sendMethod.contentFeed(0));
		methodContent.append("return length;");
		sendMethod.setContant(methodContent.toString());
	}

	/**
	 * 对类型做转换,stirng 转成String,list转成List<Xxx>
	 * 
	 * @param paramType
	 * @param structName
	 * @return
	 */
	private String convertType(String paramType, String structName) {
		if (paramType.equals("string")) {
			paramType = "String";
		} else if (paramType.equals("list")) {
			structName = ConstantUtil.getInstance().getStringHeadUp(structName);
			paramType = "List<" + structName + "_Struct>";
		}

		return paramType;
	}

	private String convertC2SType(String paramType, String structName) {
		if (paramType.equals("string")) {
			paramType = "String";
		} else if (paramType.equals("list")) {
			structName = ConstantUtil.getInstance().getStringHeadUp(structName);
			paramType = "List<ISendable>";
		}

		return paramType;
	}

	private String convertStructClass(String structName) {
		return ConstantUtil.getInstance().getStringHeadUp(structName)
				+ "_Struct";
	}

	private String convertType(String type) {
		return type.split("<")[0];
	}

	private void saveOldExcuteMethod(JavaFile file, Class javaClass) {
		String methodContent = readExcuteMethod(file.getFilePathAndName()
				.toString());
		if (methodContent != null) {
			for (Method method : javaClass.getMethodList()) {
				if (method.getName().equals("excute")) {
					method.setContant(methodContent);
				}
			}
		}

	}

	private String readExcuteMethod(String filePathAndName) {
		String s = ConstantUtil.getInstance().readFile(filePathAndName);
		if (s == null) {
			return null;
		}
		String methodContent = "";

		int methodStartIndex = s.indexOf("public void excute()");
		int methodContentStartIndex = s.indexOf("{", methodStartIndex);
		int loopIndex = methodContentStartIndex;
		int leftBraceNum = 0;
		int rightBraceNum = 0;
		while (loopIndex != s.length() - 1) {
			char c = s.charAt(loopIndex);
			if (c == '{') {
				leftBraceNum++;
			} else if (c == '}') {
				if (leftBraceNum == rightBraceNum) {
					break;
				}
				rightBraceNum++;
			}

			loopIndex++;
		}

		if (loopIndex == s.length() - 1) {
			System.out.println("execute..方法缺少括号");
			return null;
		}

		methodContent = s.substring(methodContentStartIndex + 2, loopIndex - 3);

		return methodContent;
	}

	private List<String> getRelateStructName(ResponseMessage responseMessage) {
		List<String> structs = new ArrayList<String>();
		if (responseMessage.getParamList() == null)
			return structs;
		for (Param param : responseMessage.getParamList()) {
			if (param.getStruct() != null && !param.getStruct().equals("")) {
				structs.add(param.getStruct());
				structs.addAll(getRelateStructName(param.getStruct()));
			}
		}
		return structs;
	}

	private List<String> getRelateStructName(RequestMessage requestMessage) {
		List<String> structs = new ArrayList<String>();
		if (requestMessage.getParamList() == null)
			return structs;
		for (Param param : requestMessage.getParamList()) {
			if (param.getStruct() != null && !param.getStruct().equals("")) {
				structs.add(param.getStruct());
				structs.addAll(getRelateStructName(param.getStruct()));
			}
		}
		return structs;
	}

	private List<String> getRelateStructName(String structName) {
		List<String> structs = new ArrayList<String>();
		for (Struct struct : structList) {
			if (struct.getName().equals(structName)) {
				for (Param param : struct.getParamList()) {
					if (param.getStruct() != null
							&& !param.getStruct().equals("")) {
						structs.add(param.getStruct());
						structs.addAll(getRelateStructName(param.getStruct()));
					}
				}
			}
		}
		return structs;
	}
}
