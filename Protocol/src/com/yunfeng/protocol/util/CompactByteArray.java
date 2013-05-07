package com.yunfeng.protocol.util;

import java.util.ArrayList;
import java.util.List;

public class CompactByteArray {

	private static byte[] getBytes(List<Byte> bytes) {
		byte[] tbytes = new byte[bytes.size()];
		for (int i = 0; i < bytes.size(); i++) {
			tbytes[i] = bytes.get(i);
		}
		return tbytes;
	}

	public static byte[] writeInts(int... values) {
		List<Byte> bytes = new ArrayList<Byte>();
		for (int value : values) {
			int buffer1 = value & 127;
			int buffer2 = (value >>> 7) & 127;
			int buffer3 = (value >>> 14) & 127;
			int buffer4 = (value >> 21) & 127;
			int buffer5 = value >> 28;
			if (buffer2 != 0 || buffer3 != 0 || buffer4 != 0 || buffer5 != 0) {
				bytes.add((byte) (128 | buffer1));
				// bytes[_position++] = 128 | buffer1;
			} else {
				bytes.add((byte) (buffer1));
				// _bytes[_position++] = buffer1;
			}
			if (buffer3 != 0 || buffer4 != 0 || buffer5 != 0) {
				bytes.add((byte) (128 | buffer2));
				// _bytes[_position++] = 128 | buffer2;
			} else {
				if (buffer2 != 0) {
					bytes.add((byte) (buffer2));
					// _bytes[_position++] = buffer2;
				}
			}
			if (buffer4 != 0 || buffer5 != 0) {
				bytes.add((byte) (128 | buffer3));
				// _bytes[_position++] = 128 | buffer3;
			} else {
				if (buffer3 != 0) {
					bytes.add((byte) (buffer3));
					// _bytes[_position++] = buffer3;
				}
			}
			if (buffer5 != 0) {
				bytes.add((byte) (128 | buffer4));
				bytes.add((byte) (buffer5));
				// _bytes[_position++] = 128 | buffer4;
				// _bytes[_position++] = buffer5;
			} else {
				if (buffer4 != 0) {
					bytes.add((byte) (buffer4));
					// _bytes[position++] = buffer4;
				}
			}
		}
		return getBytes(bytes);
	}

	// 111 1000100 1000000
	public static List<Integer> readInt(byte[] bytes) {
		List<Integer> values = new ArrayList<Integer>();
		for (int position = 0; position < bytes.length;) {
			int buffer1 = 0;
			int buffer2 = 0;
			int buffer3 = 0;
			int buffer4 = 0;
			int buffer5 = 0;

			int value = bytes[position++];
			buffer1 = value & 127;
			if (value >>> 7 != 0) {
				value = bytes[position++];
				buffer2 = value & 127;
				if (value >>> 7 != 0) {
					value = bytes[position++];
					buffer3 = value & 127;
					if (value >>> 7 != 0) {
						value = bytes[position++];
						buffer4 = value & 127;
						if (value >>> 7 != 0) {
							value = bytes[position++];
							buffer5 = value;
						}
					}
				}
			}
			int value1 = buffer1 + (buffer2 << 7) + (buffer3 << 14)
					+ (buffer4 << 21) + (buffer5 << 28);
			values.add(value1);
		}
		return values;
	}

	public static void main(String[] a) {
		byte[] bytes = CompactByteArray.writeInts(971);
		for (byte v : bytes) {
			System.err.println(v);
		}
		List<Integer> values = CompactByteArray.readInt(bytes);
		for (int v : values) {
			System.err.println(v);
		}
	}
}
