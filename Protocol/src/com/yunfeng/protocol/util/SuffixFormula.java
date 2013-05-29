package com.yunfeng.protocol.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 后缀表达式原理
 */
public class SuffixFormula {

	private static String transformToSuffix(String infix) {
		InToPost inToPost = new InToPost(infix);
		return inToPost.doTrans();
	}

	public static double formula(String infix) {
		List<Double> values = new ArrayList<Double>();
		double value = 0;
		double value1 = 0;
		double value2 = 0;

		String suffix = transformToSuffix(infix);
		String[] nodes = suffix.split("#");
		for (String node : nodes) {
			if (node.equals("")) {
				continue;
			}
			if (node.equals("+")) {
				value2 = values.remove(values.size() - 1);
				value1 = values.remove(values.size() - 1);
				value = value1 + value2;
			} else if (node.equals("-")) {
				value2 = values.remove(values.size() - 1);
				value1 = values.remove(values.size() - 1);
				value = value1 - value2;
			} else if (node.equals("*")) {
				value2 = values.remove(values.size() - 1);
				value1 = values.remove(values.size() - 1);
				value = value1 * value2;
			} else if (node.equals("/")) {
				value2 = values.remove(values.size() - 1);
				value1 = values.remove(values.size() - 1);
				value = value1 / value2;
			} else if (node.equals("^")) {
				value2 = values.remove(values.size() - 1);
				value1 = values.remove(values.size() - 1);
				value = Math.pow(value1, value2);
			} else {
				value = Double.parseDouble(node);
			}
			values.add(value);
		}

		return values.get(0);
	}

	public static void main(String[] args) {
		String abc = "1*100*4.8^2";
		// System.err.println(System.currentTimeMillis());
		double value = SuffixFormula.formula(abc);
		// System.err.println(System.currentTimeMillis());
		System.err.println(value);
	}
}

class MyStack {
	private int maxSize;// 栈的最大容量
	private char[] ch; // 栈的数据
	private int top; // 栈头标记

	public MyStack(int s) {
		maxSize = s;
		ch = new char[s];
		top = -1;
	}

	public void push(char c) {// 入栈
		ch[++top] = c;
	}

	public char pop() {// 出栈
		return ch[top--];
	}

	public char peek() {
		return ch[top];
	}

	public boolean isEmpty() {
		return top <= -1;
	}

	public boolean isFull() {
		return top == (maxSize - 1);
	}

	public int size() {
		return top + 1;
	}

	public char get(int index) {
		return ch[index];
	}

	public void display(String str) {
		System.out.print(str);
		System.out.print(" Stack (bottom-->top): ");
		for (int i = 0; i < size(); i++) {
			System.out.print(get(i) + " ");
		}
		System.out.println();
	}
}

class InToPost {
	private MyStack ms;// 自定义栈
	private String input;// 输入中缀表达式
	private String output = "";// 输出的后缀表达式

	public InToPost(String input) {
		this.input = input;
		int size = input.length();
		ms = new MyStack(size);
	}

	public String doTrans() {// 转换为后缀表达式方法
		boolean ischar = false;
		for (int i = 0; i < input.length(); i++) {
			char ch = input.charAt(i);
			if (ch == ' ') {
				continue;
			} else if (ch == '-' && (output.equals("") || ischar)) {
				output = output + "#" + ch;
				ischar = false;
				continue;
			}
			switch (ch) {
			case '+':
			case '-':
				getOper(ch, 1);
				ischar = true;
				break;
			case '*':
			case '/':
				getOper(ch, 2);
				ischar = true;
				break;
			case '^':
				getOper(ch, 3);
				ischar = true;
				break;
			case '(':
				ms.push(ch);
				ischar = true;
				break;
			case ')':
				getParent(ch);
				ischar = true;
				break;
			default:
				if (!ischar) {
					output = output + ch;
				} else {
					output = output + "#" + ch;
				}
				ischar = false;
				break;
			}// end switch
		}// end for
		while (!ms.isEmpty()) {
			// ms.display("While ");
			output = output + "#" + ms.pop();
		}
		// ms.display("end while!!");
		return output;
	}

	public void getParent(char ch) {
		while (!ms.isEmpty()) {
			char chx = ms.pop();
			if (chx == '(') {
				break;
			} else {
				output = output + "#" + chx;
			}
		}
	}

	public void getOper(char ch, int prec1) {
		while (!ms.isEmpty()) {// 判断栈是否为空
			char operTop = ms.pop();
			if (operTop == '(') {
				ms.push(operTop);
				break;
			} else {
				int prec2 = 0;
				if (operTop == '+' || operTop == '-') {
					prec2 = 1;
				} else if (operTop == '*' || operTop == '/') {
					prec2 = 2;
				} else if (operTop == '^') {
					prec2 = 3;
				}
				if (prec2 < prec1) {
					ms.push(operTop);
					break;
				} else {
					output = output + "#" + operTop;
				}
			}
		}// end while
		ms.push(ch);
	}
}
