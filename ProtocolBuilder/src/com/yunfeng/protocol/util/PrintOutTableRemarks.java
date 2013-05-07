package com.yunfeng.protocol.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;

import com.yunfeng.protocol.pojo.Table;
import com.yunfeng.protocol.pojo.Table.Column;

public class PrintOutTableRemarks {
	public static void main(String[] args) throws Exception {
		printOutTableRemarks("xy");
		// printOutTableRemarks("xylogs");
	}

	private static void printOutTableRemarks(String database) throws Exception,
			IOException {
		JdbcUtil instance = JdbcUtil.getInstance();
		instance.setDatabase(database);
		instance.init();
		StringBuilder sb = new StringBuilder();
		for (Entry<String, Table> entry : instance.getTables().entrySet()) {
			sb.append("Table: " + entry.getKey() + ": \n");
			for (Column column : entry.getValue().getColumns()) {
				sb.append("\tColumn: " + column.getName() + ", Remarks: "
						+ column.getRemarks() + "\n");
			}
			sb.append("\n");
		}
		FileWriter fw = new FileWriter("table\\" + database.toUpperCase()
				+ "_TableRemarks.txt");
		fw.write(sb.toString().toCharArray());
		fw.close();
	}
}
