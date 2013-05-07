package com.yunfeng.protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XyGame {
	public static final int MESSAGETYPE = 0;

	public static final int DDDD_C2S_NUMBER = 1;
	private Ddd_C2S ddd_C2S;

	public static final int DDDD_S2C_NUMBER = 2;

	public Command parse(Map map) {
		int messageType = (Integer) map.get(MESSAGETYPE);
		switch (messageType) {
		case DDDD_C2S_NUMBER:
			return new Ddd_C2S(map);
		}
		return null;
	}

	public interface Command {
		public void excute();
	}

	public class Ddd_C2S implements Command {
		public static final int X_NUMBER = 1;
		private String x;

		public static final int MESSAGELIST_NUMBER = 2;
		private List<Yy_Struct> yy_Struct;

		/**
		 * 解析map
		 * 
		 * @param messageMap
		 * @return
		 */
		public Ddd_C2S(Map messageMap) {
			x = (String) messageMap.get(X_NUMBER);
			yy_Struct = (List<Yy_Struct>) messageMap.get(MESSAGELIST_NUMBER);
		}

		@Override
		public void excute() {
		}

		public String getX() {
			return x;
		}

		public void setX(String x) {
			this.x = x;
		}

		public List<Yy_Struct> getYy_Struct() {
			return yy_Struct;
		}

		public void setYy_Struct(List<Yy_Struct> yy_Struct) {
			this.yy_Struct = yy_Struct;
		}
	}

	public class Yy_Struct {
		private int y;
		private double cc;

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

		public double getCc() {
			return cc;
		}

		public void setCc(double cc) {
			this.cc = cc;
		}
	}
}
