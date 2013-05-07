package com.yunfeng.protocol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.Element;

import com.yunfeng.protocol.pojo.Class;
import com.yunfeng.protocol.pojo.Config;
import com.yunfeng.protocol.pojo.Protocol;
import com.yunfeng.protocol.pojo.RequestMessage;
import com.yunfeng.protocol.pojo.ResponseMessage;
import com.yunfeng.protocol.pojo.Struct;
import com.yunfeng.protocol.util.ConstantUtil;
import com.yunfeng.protocol.util.DocumentController;

/**
 * 协议生成器主类
 */
public class BuildProtocol {
	public static final String path = "protocol/tempetProtocol.xml";

	public static final String configPath = "protocol/config.properties";

	public static final int CREATE_NEW_MESSAGE_COMMAND = 0;
	public static final int MODIFY_MESSAGE_COMMAND = 1;
	public static final int TEST_MESSAGE_COMMAND = 2;
	public static final int REFRESH_MESSAGE_COMMAND = 3;

	/**
	 * 客户端请求的消息集合
	 */
	private List<RequestMessage> requestMessageList = new ArrayList<RequestMessage>();

	/**
	 * 服务器发送的消息集合
	 */
	private List<ResponseMessage> responseMessageList = new ArrayList<ResponseMessage>();

	/**
	 * 协议中的结构体集合
	 */
	private List<Struct> structList = new ArrayList<Struct>();

	private List<Protocol> protocolList = new ArrayList<Protocol>();

	/**
	 * 配置
	 */
	private Config config = new Config();

	public static final void main(String[] args) {
		System.out.println("欢迎使用协议解析器。");
		BuildProtocol buildProtocol = new BuildProtocol();
		buildProtocol.readProtocolDocuments();
		buildProtocol.protocolNameChange();
		buildProtocol.readEnterCommand();
	}

	/**
	 * 读取命令
	 */
	public void readEnterCommand() {
		try {
			System.out
					.println("请输入命令号:(1.生成所有新协议，2.修改旧协议，3.生成测试文件，4.刷新所有S2C文件  9.退出）   ");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			int command = Integer.parseInt(br.readLine());
			int messageId = 0;
			if (command == 2 || command == 3) {
				System.out.println("请输入协议号:");
				messageId = Integer.parseInt(br.readLine());
			}
			switch (command) {
			case 1:
				createNewProtocals();
				break;
			case 2:
				modifyProtocal(messageId);
				break;
			case 3:
				createTestProtocals();
				break;
			case 4:
				refreshS2C();
				break;

			case 9:
				break;
			default:
				System.out.println("你输入的命令号错误！");
			}
			System.out.println("谢谢使用！");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 生成新协议
	 */
	public void createNewProtocals() {
		createJavaProtocolFiles(CREATE_NEW_MESSAGE_COMMAND, 0);
	}

	/**
	 * 修改协议
	 * 
	 * @param messageId
	 */
	public void modifyProtocal(int messageId) {
		createJavaProtocolFiles(MODIFY_MESSAGE_COMMAND, messageId);
		System.out.println("你要修改的协议号：(按q退出）");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			String in = br.readLine();
			if (in.equals("q") || in.equals("Q")) {
				return;
			} else {
				modifyProtocal(Integer.parseInt(in));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成测试消息
	 */
	public void createTestProtocals() {
		overTurnMessage();
		createJavaProtocolFiles(TEST_MESSAGE_COMMAND, 0);
	}

	public void refreshS2C() {
		createJavaProtocolFiles(REFRESH_MESSAGE_COMMAND, 0);
	}

	/**
	 * 将response消息转换成
	 */
	public void overTurnMessage() {
		ArrayList<RequestMessage> testRequestList = new ArrayList<RequestMessage>();
		ArrayList<ResponseMessage> testResponseList = new ArrayList<ResponseMessage>();
		for (RequestMessage requestMessage : requestMessageList) {
			ResponseMessage responseMessage = new ResponseMessage();
			responseMessage.setId(requestMessage.getId());
			responseMessage.setModule(requestMessage.getModule());
			responseMessage.setName(requestMessage.getName());
			responseMessage.setParamList(requestMessage.getParamList());
			testResponseList.add(responseMessage);
		}

		for (ResponseMessage responseMessage : responseMessageList) {
			RequestMessage requestMessage = new RequestMessage();
			requestMessage.setId(responseMessage.getId());
			requestMessage.setModule(responseMessage.getModule());
			requestMessage.setName(responseMessage.getName());
			requestMessage.setParamList(responseMessage.getParamList());
			testRequestList.add(requestMessage);
		}
		requestMessageList = testRequestList;
		responseMessageList = testResponseList;
	}

	/**
	 * 解析出协议文件中的消息对象和结构体对象,
	 */
	public void readProtocolDocuments() {
		getConfig();
		new ArrayList<String>();
		File xmlPathFile = new File(this.getClass().getClassLoader()
				.getResource(config.getXmlPath()).getPath().replace("%20", " "));
		File[] files = xmlPathFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			String fileName = files[i].getName();
			String[] fileNames = fileName.split("[.]");
			if (fileNames[fileNames.length - 1].equals("xml")) {
				// fileList.add(files[i].getPath());
				readMessage(files[i].getPath());
			}
		}
		checkId();
	}

	private void readMessage(String path) {
		Document document = DocumentController.readRealPathDocument(path);

		for (Iterator<Element> elementIterator = document.getRootElement()
				.elementIterator("protocol"); elementIterator.hasNext();) {
			Element pojoElement = (Element) elementIterator.next();

			String module = pojoElement.attributeValue("module");

			ArrayList requestList = (ArrayList) ConstantUtil.getInstance()
					.readPojoByElement(RequestMessage.class, pojoElement);
			ArrayList responseList = (ArrayList) ConstantUtil.getInstance()
					.readPojoByElement(ResponseMessage.class, pojoElement);
			ArrayList structList = (ArrayList) ConstantUtil.getInstance()
					.readPojoByElement(Struct.class, pojoElement);

			for (RequestMessage requestMessage : (ArrayList<RequestMessage>) requestList) {
				requestMessage.setModule(module);
			}

			for (ResponseMessage responseMessage : (ArrayList<ResponseMessage>) responseList) {
				responseMessage.setModule(module);
			}

			for (Struct struct : (ArrayList<Struct>) structList) {
				struct.setModule(module);
			}

			requestMessageList.addAll(requestList);
			responseMessageList.addAll(responseList);
			this.structList.addAll(structList);
		}
	}

	private boolean checkId() {
		boolean checkSuccess = true;
		for (RequestMessage requestMessage : requestMessageList) {
			if (!checkIdByMessageId(requestMessage.getId())) {
				System.err.println("ID号冲突错误: id = " + requestMessage.getId()
						+ ",name = " + requestMessage.getName());
				checkSuccess = false;
			}
		}

		for (ResponseMessage responseMessage : responseMessageList) {
			if (!checkIdByMessageId(responseMessage.getId())) {
				System.out.println("ID号冲突错误: id = " + responseMessage.getId()
						+ ",name = " + responseMessage.getName());
				checkSuccess = false;
			}
		}
		return checkSuccess;
	}

	private boolean checkIdByMessageId(int id) {
		boolean sameId = false;
		for (RequestMessage requestMessage : requestMessageList) {
			if (requestMessage.getId() == id && sameId == false) {
				sameId = true;
			} else if (requestMessage.getId() == id && sameId == true) {
				return false;
			}
		}

		for (ResponseMessage responseMessage : responseMessageList) {
			if (responseMessage.getId() == id && sameId == false) {
				sameId = true;
			} else if (responseMessage.getId() == id && sameId == true) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 获得协议配置
	 * 
	 * @param rootElement
	 */
	public void getConfig() {
		Properties configProperty = new Properties();
		try {
			String url = this.getClass().getClassLoader()
					.getResource(configPath).getPath().replace("%20", " ");
			FileInputStream inputFile = new FileInputStream(url);
			configProperty.load(inputFile);
		} catch (IOException e) {
			System.err.println("找不到配置文件:" + configPath);
		}

		config.setJavaProjectPath(configProperty.getProperty("javaProjectPath"));
		config.setModulePackage(configProperty.getProperty("modulePackage"));
		config.setXmlPath(configProperty.getProperty("xmlPath"));
		config.setProtocolName(ConstantUtil.getInstance().getStringHeadUp(
				configProperty.getProperty("protocolName")));
		config.setTestProjectPath(configProperty.getProperty("testProjectPath"));
	}

	/**
	 * 对消息编号
	 */
	public void numberMessage() {
		int index = 0;

		for (RequestMessage requestMessage : requestMessageList) {
			requestMessage.setId(index);
			index++;
		}

		for (ResponseMessage responseMessage : responseMessageList) {
			responseMessage.setId(index);
			index++;
		}

		for (Struct struct : structList) {
			struct.setId(index);
			index++;
		}
	}

	/**
	 * 给消息名后增加标识:_C2S,客户端发给服务器;_S2C,服务器发送给客户端
	 */
	public void protocolNameChange() {
		for (RequestMessage requestMessage : requestMessageList) {
			requestMessage.setName(requestMessage.getName() + "_C2S");
		}

		for (ResponseMessage responseMessage : responseMessageList) {
			responseMessage.setName(responseMessage.getName() + "_S2C");
		}
	}

	/**
	 * 生成java协议文件
	 */
	public void createJavaProtocolFiles(int command, int messageId) {
		JavaMessageFileCreater messageFileCreater = new JavaMessageFileCreater(
				requestMessageList, responseMessageList, structList, config);
		List<Class> C2S_messageClassList = messageFileCreater
				.createMessageFile(command, messageId);

		JavaParseClassFileCreater javaParseClassFileCreater = new JavaParseClassFileCreater(
				requestMessageList, responseMessageList, config);
		javaParseClassFileCreater.createProtocolPraseFile(C2S_messageClassList,
				command);
	}

	public List<Protocol> getProtocolList() {
		return protocolList;
	}

	public void setProtocolList(List<Protocol> protocolList) {
		this.protocolList = protocolList;
	}
}