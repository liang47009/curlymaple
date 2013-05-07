package com.yunfeng.protocol.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.yunfeng.protocol.pojo.Table;

/**
 * 数据库连接操作工具
 */
public class JdbcUtil {
	private String database;
	private static JdbcUtil instance = new JdbcUtil();
	private final Map<String, Table> tables = new HashMap<String, Table>();// 所有表的结构

	public static final JdbcUtil getInstance() {
		return instance;
	}

	protected JdbcUtil() {

	}

	/**
	 * 载入表格信息
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://192.168.1.208:3306/" + database
				+ "?useUnicode=true&amp;characterEncoding=utf-8";
		Connection connection = DriverManager.getConnection(url, "xy", "xy");
		DatabaseMetaData dmd = connection.getMetaData();
		ResultSet result = dmd.getTables(null, null, null, null);

		while (result.next()) {
			Table table = new Table();
			table.setName(result.getString("TABLE_NAME"));
			table.setRemarks(result.getString("REMARKS"));
			table.init(dmd);
			tables.put(table.getName(), table);
		}
		result.close();
		connection.close();
	}

	public final Table getTable(String name) {
		return tables.get(name);
	}

	public Map<String, Table> getTables() {
		return tables;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getDatabase() {
		return database;
	}

}
