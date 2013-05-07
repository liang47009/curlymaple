package com.yunfeng.protocol.pojo;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 表结构
 */
public class Table {
	private String name;// 表名字
	private String remarks;// 表的解释性注释
	private List<Column> columns = new ArrayList<Column>();// 字段
	private List<Index> indexes = new ArrayList<Index>();// 索引，唯一索引，不包括主键索引

	/**
	 * 初始化字段集合和索引集合
	 */
	public void init(DatabaseMetaData dmd) {
		try {
			ResultSet result = dmd.getColumns(null, null, name, null);
			while (result.next()) {
				Column column = new Column();
				column.setName(result.getString("COLUMN_NAME"));
				column.setRemarks(result.getString("REMARKS"));
				this.columns.add(column);
			}
			result.close();
			result = dmd.getIndexInfo(null, null, name, true, false);
			while (result.next()) {
				String name = result.getString("COLUMN_NAME");
				if (!name.equals("id")) {
					Index index = new Index();
					index.setColumn_name(name);
					index.setIndex_name(result.getString("INDEX_NAME"));
					this.indexes.add(index);
				}
			}
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public List<Index> getIndexes() {
		return indexes;
	}

	public void setIndexes(List<Index> indexes) {
		this.indexes = indexes;
	}

	/**
	 * 表的字段
	 */
	public class Column {
		private String name;// 列名称
		private String remarks;// 注释

		public String getRemarks() {

			return remarks;
		}

		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	/**
	 * 唯一索引，不包括主键索引
	 */
	public class Index {
		private String index_name;// 索引名字
		private String column_name;// 列名字

		public String getIndex_name() {
			return index_name;
		}

		public void setIndex_name(String index_name) {
			this.index_name = index_name;
		}

		public String getColumn_name() {
			return column_name;
		}

		public void setColumn_name(String column_name) {
			this.column_name = column_name;
		}

	}
}
