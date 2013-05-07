package com.yunfeng.protocol.pojo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.yunfeng.protocol.util.ConstantUtil;

/**
 * java文件类
 * 
 */
public class JavaFile {
	private String name;
	private String packageName;
	private List<String> importList = new ArrayList<String>();
	// private List<Class> classList;
	private StringBuffer stringBuffer = new StringBuffer();
	private StringBuffer path = new StringBuffer();
	private StringBuffer filePathAndName = new StringBuffer();

	public JavaFile(String name, String packageName, String projectPath) {
		this.name = name;
		this.packageName = packageName;
		String s = packageName.replaceAll("[.]", "/");
		path.append(projectPath);
		path.append("/src/");
		path.append(s);
		path.append("/");

		filePathAndName.append(path);
		filePathAndName.append(name);
		filePathAndName.append(".java");
	}

	/**
	 * 创建文件
	 */
	public void createFile() {
		try {
			path.append(name);
			path.append(".java");
			ConstantUtil.getInstance().newFile(path.toString(),
					stringBuffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write() {
		stringBuffer.append("package ");
		stringBuffer.append(packageName);
		stringBuffer.append(";\n\n");

		for (String improtName : importList) {
			stringBuffer.append("import ");
			stringBuffer.append(improtName);
			stringBuffer.append(";\n");
		}

		stringBuffer.append("\n");
	}

	/**
	 * 增加文件导入
	 * 
	 * @param importName
	 */
	public void addImport(String importName) {
		this.importList.add(importName);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public List<String> getImportList() {
		return importList;
	}

	public void setImportList(List<String> importList) {
		this.importList = importList;
	}

	public StringBuffer getStringBuffer() {
		return stringBuffer;
	}

	public void setStringBuffer(StringBuffer stringBuffer) {
		this.stringBuffer = stringBuffer;
	}

	public StringBuffer getPath() {
		return path;
	}

	public String getPathName() {
		return path.toString() + name + ".java";
	}

	public void setPath(StringBuffer path) {
		this.path = path;
	}

	public StringBuffer getFilePathAndName() {
		return filePathAndName;
	}

	public void setFilePathAndName(StringBuffer filePathAndName) {
		this.filePathAndName = filePathAndName;
	}
}
