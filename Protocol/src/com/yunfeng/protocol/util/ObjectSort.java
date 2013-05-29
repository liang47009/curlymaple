package com.yunfeng.protocol.util;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.Date;

public class ObjectSort implements Comparator<Object> {
	String[] fieldList = null;
	int[] sortList = null;// 0降序，1升序

	public ObjectSort(String[] fieldList, int[] sortList) {
		this.fieldList = fieldList;
		this.sortList = sortList;
	}

	@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		try {
			for (int i = 0; i < fieldList.length; i++) {
				Field field = MapUtil.getDeclaredField(o1.getClass(),
						fieldList[i]);

				if (field.getType().equals(int.class)) {
					int int1 = field.getInt(o1);
					int int2 = field.getInt(o2);
					int result = ascend(int1 - int2, sortList[i]);
					if (result != 0) {
						return result;
					}
				} else if (field.getType().equals(Integer.class)) {
					Integer integer1 = (Integer) field.get(o1);
					Integer integer2 = (Integer) field.get(o2);
					int result = ascend(integer1.compareTo(integer2),
							sortList[i]);
					if (result != 0) {
						return result;
					}
				} else if (field.getType().equals(Date.class)) {
					Date date1 = (Date) field.get(o1);
					Date date2 = (Date) field.get(o2);
					if (!date1.equals(date2)) {
						int result = ascend(date1.compareTo(date2), sortList[i]);
						if (result != 0) {
							return result;
						}
					}
				} else if (field.getType().equals(String.class)) {
					String string1 = String.valueOf(field.get(o1));
					String string2 = String.valueOf(field.get(o2));
					int result = ascend(string1.compareTo(string2), sortList[i]);
					if (result != 0) {
						return result;
					}
				} else if (field.getType().equals(long.class)) {
					long long1 = field.getLong(o1);
					long long2 = field.getLong(o2);
					int value = 0;
					if (long1 - long2 == 0)
						value = 0;
					if (long1 > long2)
						value = 1;
					if (long1 < long2)
						value = -1;

					int result = ascend(value, sortList[i]);
					if (result != 0) {
						return result;
					}
				} else if (field.getType().equals(Long.class)) {
					Long long1 = (Long) field.get(o1);
					Long long2 = (Long) field.get(o2);

					int result = ascend(long1.compareTo(long2), sortList[i]);
					if (result != 0) {
						return result;
					}
				}
			}
		} catch (Exception e) {
			Log.error("排序出异常", e);
		}
		return 0;
	}

	// public static void main(String[] args) {
	// // Collections.sort(somethingList, new ObjectSort(new String[] { "type",
	// // "cid", "binding" }, new int[] { 0, 1, 1 }));
	// ObjectSort os = new ObjectSort(new String[] { "gold", "silver",
	// "winCount" },
	// new int[] { 0, 0, 0 });
	// List<User> list = new ArrayList<User>();
	// User rank = new User();
	// rank.setGold(1231);
	// rank.setSilver(1232);
	// rank.setWinCount(1);
	// list.add(rank);
	// rank = new User();
	// rank.setGold(1237);
	// rank.setSilver(12302);
	// rank.setWinCount(2);
	// list.add(rank);
	// rank = new User();
	// rank.setGold(123);
	// rank.setSilver(12362);
	// rank.setWinCount(1);
	// list.add(rank);
	// rank = new User();
	// rank.setGold(123);
	// rank.setSilver(178);
	// rank.setWinCount(3);
	// list.add(rank);
	// rank = new User();
	// rank.setSilver(10232);
	// rank.setGold(123);
	// rank.setWinCount(2);
	// list.add(rank);
	// rank = new User();
	// rank.setGold(123);
	// rank.setSilver(19232);
	// rank.setWinCount(3);
	// list.add(rank);
	// rank = new User();
	// rank.setGold(123);
	// rank.setSilver(12832);
	// rank.setWinCount(1);
	// list.add(rank);
	// rank = new User();
	// rank.setGold(123);
	// rank.setSilver(12322);
	// rank.setWinCount(6);
	// list.add(rank);
	// rank = new User();
	// rank.setGold(123);
	// rank.setSilver(12362);
	// rank.setWinCount(7);
	// list.add(rank);
	// rank = new User();
	// rank.setGold(123);
	// rank.setSilver(232);
	// rank.setWinCount(8);
	// list.add(rank);
	// Collections.sort(list, os);
	// for (int i = 0; i < list.size(); i++) {
	// System.out.println(list.get(i));
	// }
	// }

	private int ascend(int value, int sort) {
		if (value > 0) {
			if (sort > 0) {
				return 1;
			} else {
				return -1;
			}
		} else if (value < 0) {
			if (sort > 0) {
				return -1;
			} else {
				return 1;
			}
		}
		return 0;
	}
}
