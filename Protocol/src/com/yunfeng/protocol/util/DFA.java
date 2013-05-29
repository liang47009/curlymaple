package com.yunfeng.protocol.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author xialiangliang
 * 
 */
public class DFA {
	/**
	 * 根节点
	 */
	private TreeNode rootNode = new TreeNode();

	/**
	 * 关键词缓存
	 */
	private ByteBuffer keywordBuffer = ByteBuffer.allocate(1024);

	/**
	 * 关键词编码
	 */
	private String charset = "utf8";

	/**
	 * 初始化DFA数据
	 * 
	 * @param filePath
	 *            敏感词文件路径(每行一个敏感词)
	 */
	public void init(String filePath) {
		try {
			List<String> keywordList = new ArrayList<>();
			URI uri = ClassLoader.getSystemResource(filePath).toURI();
			File inputFile = new File(uri);
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			String line = br.readLine();
			while (null != line) {
				keywordList.add(line);
				line = br.readLine();
			}
			createKeywordTree(keywordList);
		} catch (Exception e) {
			Log.error("", e);
		}
	}

	/**
	 * 创建DFA
	 * 
	 * @param keywordList
	 * @throws UnsupportedEncodingException
	 */
	private void createKeywordTree(List<String> keywordList)
			throws UnsupportedEncodingException {
		for (String keyword : keywordList) {
			if (keyword == null)
				continue;
			keyword = keyword.trim();
			byte[] bytes = keyword.getBytes(charset);

			TreeNode tempNode = rootNode;
			// 循环每个字节
			for (int i = 0; i < bytes.length; i++) {
				int index = bytes[i] & 0xff; // 字符转换成数字
				TreeNode node = tempNode.getSubNode(index);

				if (node == null) { // 没初始化
					node = new TreeNode();
					tempNode.setSubNode(index, node);
				}

				tempNode = node;

				if (i == bytes.length - 1) {
					tempNode.setKeywordEnd(true); // 关键词结束， 设置结束标志
					Log.debug("DFA:{}" + keyword);
				}
			}// end for
		}// end for
	}

	/**
	 * 搜索关键字
	 * 
	 * @param text
	 *            需要检查的文本
	 * @return 返回检查后的文本
	 * @throws UnsupportedEncodingException
	 */
	public String searchKeyword(String text)
			throws UnsupportedEncodingException {
		return searchKeyword(text.getBytes(charset));
	}

	/**
	 * 搜索关键字
	 * 
	 * @param bytes
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String searchKeyword(byte[] bytes)
			throws UnsupportedEncodingException {
		// StringBuilder words = new StringBuilder();
		if (bytes == null || bytes.length == 0) {
			return "";
		}
		TreeNode tempNode = rootNode;
		int rollback = 0; // 回滚数
		int position = 0; // 当前比较的位置

		String temp = new String(bytes);
		// byte[] bytesTemp = temp.getBytes();

		while (position < bytes.length) {
			int index = bytes[position] & 0xFF;
			keywordBuffer.put(bytes[position]); // 写关键词缓存
			tempNode = tempNode.getSubNode(index);
			// 当前位置的匹配结束
			if (tempNode == null) {
				position = position - rollback; // 回退 并测试下一个字节
				rollback = 0;
				tempNode = rootNode; // 状态机复位
				keywordBuffer.clear(); // 清空
			} else if (tempNode.isKeywordEnd()) { // 是结束点 记录关键词
				keywordBuffer.flip();
				Charset forName = Charset.forName(charset);
				String keyword = forName.decode(keywordBuffer).toString();
				keywordBuffer.limit(keywordBuffer.capacity());
				temp = temp.replaceAll(keyword, "*");
				// logger.debug("Find keyword:{}" + keyword);
				// byte[] bytes2 = keyword.getBytes();
				// int length = bytes2.length;
				// int j = position - length + 1;
				// for (int i = j; i <= position; i++) {
				// bytesTemp[i] = 42;
				// }
				// if (words.length() == 0) {
				// words.append(keyword);
				// } else {
				// words.append(":").append(keyword);
				// }
				rollback = 1; // 遇到结束点 rollback 置为1
			} else {
				rollback++; // 非结束点 回退数加1
			}
			position++;
		}
		keywordBuffer.clear();
		return temp;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

}

class TreeNode {
	private static final int NODE_LEN = 256;

	/**
	 * true 关键词的终结 ； false 继续
	 */
	private boolean end = false;

	private List<TreeNode> subNodes = new ArrayList<TreeNode>(NODE_LEN);

	public TreeNode() {
		for (int i = 0; i < NODE_LEN; i++) {
			subNodes.add(i, null);
		}
	}

	/**
	 * 向指定位置添加节点树
	 * 
	 * @param index
	 * @param node
	 */
	public void setSubNode(int index, TreeNode node) {
		subNodes.set(index, node);
	}

	public TreeNode getSubNode(int index) {
		return subNodes.get(index);
	}

	public boolean isKeywordEnd() {
		return end;
	}

	public void setKeywordEnd(boolean end) {
		this.end = end;
	}
}