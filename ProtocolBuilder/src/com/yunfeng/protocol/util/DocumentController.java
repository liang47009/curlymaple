package com.yunfeng.protocol.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * 读取配置文件
 */
public class DocumentController {
	public static Document readAbstractPathDocument(String path) {
		Document document = null;
		URL url = Thread.currentThread().getContextClassLoader()
				.getResource("");
		SAXReader reader = new SAXReader();
		try {
			document = reader.read(new File(url.getPath() + path));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}

	public static Document readRealPathDocument(String path) {
		Document document = null;
		// URL url = Thread.currentThread().getContextClassLoader()
		// .getResource("");
		SAXReader reader = new SAXReader();
		try {
			document = reader.read(new File(path));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}

	public static void writeDocument(String path, Document doc) {
		try {
			URL url = Thread.currentThread().getContextClassLoader()
					.getResource("");
			XMLWriter output = new XMLWriter(new FileOutputStream(url.getPath()
					+ path));

			output.write(doc);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
