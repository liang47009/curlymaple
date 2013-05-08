package com.yunfeng.protocol;

import java.util.Iterator;
import java.util.List;

import com.yunfeng.protocol.pojo.Class;
import com.yunfeng.protocol.pojo.Config;
import com.yunfeng.protocol.pojo.JavaFile;
import com.yunfeng.protocol.pojo.MemberValue;
import com.yunfeng.protocol.pojo.Method;
import com.yunfeng.protocol.pojo.MethodParam;
import com.yunfeng.protocol.pojo.RequestMessage;
import com.yunfeng.protocol.pojo.ResponseMessage;
import com.yunfeng.protocol.util.ConstantUtil;

/**
 * 
 * @author ddwill
 * 
 */
public class JavaParseClassFileCreater {
	/**
	 * 客户端请求的消息集合
	 */
	private List<RequestMessage> requestMessageList;

	/**
	 * 服务器发送的消息集合
	 */
	private List<ResponseMessage> responseMessageList;

	/**
	 * 协议
	 */
	private Config config;

	public JavaParseClassFileCreater(List<RequestMessage> requestMessageList,
			List<ResponseMessage> responseMessageList, Config config) {
		this.requestMessageList = requestMessageList;
		this.setResponseMessageList(responseMessageList);
		this.config = config;
	}

	/**
	 * 生成协议解析类文件
	 */
	public void createProtocolPraseFile(List<Class> messageClassList,
			int command) {
		/**
		 * 生成javafile,向javafile中加入指定的类,向java类中加入成员变量和函数
		 */
		JavaFile javaFile;
		if (command != BuildProtocol.TEST_MESSAGE_COMMAND) {
			javaFile = new JavaFile(config.getProtocolName(),
					config.getModulePackage() + ".core",
					config.getJavaProjectPath());
		} else {
			javaFile = new JavaFile(config.getProtocolName(),
					config.getModulePackage() + ".core",
					config.getTestProjectPath());
		}
		addImport(javaFile, messageClassList);
		javaFile.write();
		addProtocolClass(javaFile.getStringBuffer());

		javaFile.createFile();
	}

	/**
	 * 加入需要导入的包
	 * 
	 * @param javaFile
	 * @param messageClassList
	 */
	private void addImport(JavaFile javaFile, List<Class> messageClassList) {
		javaFile.addImport("java.util.*");
		javaFile.addImport("javax.annotation.Resource");
		javaFile.addImport("org.springframework.context.support.AbstractXmlApplicationContext");
		javaFile.addImport("org.springframework.stereotype.Controller");
		// javaFile.addImport("com.yunfeng.game.core.MemoryData");
		for (Class messageClass : messageClassList) {
			javaFile.addImport(messageClass.getClassPath());
		}
	}

	private void addProtocolClass(StringBuffer stringBuffer) {
		Class protocolClass = new Class(config.getProtocolName(),
				Class.PUBLIC_LIMIT);
		protocolClass.setAnnotation("Controller");
		// 加入单例
		// Method constructMethod = protocolClass.createSingleton(true);
		MemberValue appCont = new MemberValue(MemberValue.PRIVATE_LIMIT, false,
				false, "AbstractXmlApplicationContext", "axac");

		protocolClass.addMemberValue(appCont);
		protocolClass.createGetterSetter(null);

		for (RequestMessage requestMessage : requestMessageList) {
			String reqNumberName = requestMessage.getModule() + "_"
					+ requestMessage.getName() + "_number";
			reqNumberName = reqNumberName.toUpperCase();

			MemberValue messageNum = new MemberValue(MemberValue.PRIVATE_LIMIT,
					true, true, "short", reqNumberName, requestMessage.getId()
							+ "");

			protocolClass.addMemberValue(messageNum);
		}

		MemberValue messageMap = new MemberValue(MemberValue.PRIVATE_LIMIT,
				true, true, "Map<Short, IC2SCommand>", "mm",
				"new TreeMap<Short, IC2SCommand>();");
		protocolClass.addMemberValue(messageMap);
		Method init = new Method(null, Method.PUBLIC_LIMIT, "init", "void", "");
		protocolClass.addMethod(init);
		protocolClass.addMethod(createParseMethod(init));

		protocolClass.write(stringBuffer, 0);

	}

	/**
	 * 创建解析函数 messageType:协议号 memoryData:socket缓存中的数据
	 * 
	 * @param initMethod
	 *            构造函数
	 * @return
	 */
	private Method createParseMethod(Method initMethod) {
		Method parseMethod = new Method(null, Method.PUBLIC_LIMIT, "parse",
				"IC2SCommand", "");
		parseMethod.setStaticMethod(true);
		parseMethod.addMethodParam(new MethodParam("short", "messageId"));
		// parseMethod.addMethodParam(new MethodParam("MemoryData",
		// "memoryData"));
		StringBuffer parseContent = new StringBuffer();

		// parseContent.append(parseMethod.contentFeed(0));
		// parseContent.append("short messageId =channelBuffer.readShort();");
		// parseContent.append(parseMethod.contentFeed(0));
		parseContent.append("return mm.get(messageId);");
		// parseContent.append("try{");
		// parseContent.append(parseMethod.contentFeed(1));
		// parseContent.append("switch(messageId){");

		StringBuilder sb = new StringBuilder();
		Iterator<RequestMessage> it = requestMessageList.iterator();
		for (; it.hasNext();) {
			RequestMessage requestMessage = it.next();
			// parseContent.append(parseMethod.contentFeed(2));
			String reqNumberName = requestMessage.getModule() + "_"
					+ requestMessage.getName() + "_number";
			reqNumberName = reqNumberName.toUpperCase();
			// parseContent.append("case ");
			// parseContent.append(reqNumberName);
			// parseContent.append(": return messageMap.get(");
			// parseContent.append(reqNumberName);
			// parseContent.append(");");

			sb.append("mm.put(");
			sb.append(reqNumberName);
			sb.append(", axac.getBean(");
			sb.append(ConstantUtil.getInstance().getStringHeadUp(
					requestMessage.getName()));
			sb.append(".class));");
			if (it.hasNext()) {
				sb.append(initMethod.contentFeed(0));
			}
		}
		initMethod.setContant(sb.toString());

		// parseContent.append(parseMethod.contentFeed(1));
		// parseContent.append("}");
		// parseContent.append(parseMethod.contentFeed(0));
		// parseContent.append("} catch (IndexOutOfBoundsException e) {");
		// parseContent.append(parseMethod.contentFeed(1));

		// parseContent.append("System.out.println(\"收到一条错误的消息\" + messageId);");
		// parseContent
		// .append("org.apache.log4j.Logger.getLogger(com.yunfeng.constant.ModuleName.SOCKET).info(\"协议解析出错 \" + messageId, e);");
		// parseContent.append(parseMethod.contentFeed(0));
		// parseContent.append("}");
		// parseContent.append(parseMethod.contentFeed(0));
		// parseContent
		// .append("org.apache.log4j.Logger.getLogger(com.yunfeng.constant.ModuleName.SOCKET).info(\"消息号错误 没找到对应的执行程序 \" + messageId);");
		// Logger.getLogger(ModuleName.SOCKET).info("消息号错误 没找到对应的执行程序 " +
		// messageId);
		// parseContent.append("return null;");
		parseMethod.setContant(parseContent.toString());
		return parseMethod;
	}

	public List<ResponseMessage> getResponseMessageList() {
		return responseMessageList;
	}

	public void setResponseMessageList(List<ResponseMessage> responseMessageList) {
		this.responseMessageList = responseMessageList;
	}
}
