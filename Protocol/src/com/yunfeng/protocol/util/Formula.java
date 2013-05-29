package com.yunfeng.protocol.util;

/**
 * 公式解析，例如28571*(n/6)
 */
public class Formula {
	private static Formula instance = new Formula();

	private Formula() {

	}

	public static final Formula getInstance() {
		return instance;
	}

	/**
	 * 减法
	 */
	private double subtraction(String string) {
		int index = string.indexOf("-");
		double value1 = Double.valueOf(string.substring(0, index));
		double value2 = Double.valueOf(string.substring(index + 1));
		return value1 - value2;
	}

	/**
	 * 加法
	 */
	private double addition(String string) {
		int index = string.indexOf("+");
		double value1 = Double.valueOf(string.substring(0, index));
		double value2 = Double.valueOf(string.substring(index + 1));
		return value1 + value2;
	}

	/**
	 * 乘法计算
	 */
	private double multiplication(String string) {
		int index = string.indexOf("*");
		double value1 = Double.valueOf(string.substring(0, index));
		double value2 = Double.valueOf(string.substring(index + 1));
		return value1 * value2;
	}

	/**
	 * 除法计算
	 */
	private double division(String string) {
		int index = string.indexOf("/");
		double value1 = Double.valueOf(string.substring(0, index));
		double value2 = Double.valueOf(string.substring(index + 1));
		return value1 / value2;
	}

	/**
	 * 提取一个计算方程式
	 */
	private String extract(String string, int index) {
		int right = right(string, index);
		int left = left(string, index);
		return string.substring(left, right);
	}

	/**
	 * 公式的右边界限
	 */
	private int right(String string, int index) {
		int length = string.length();
		for (int i = index + 1; i < length; ++i) {
			char ch = string.charAt(i);
			if (ch == '*' || ch == '/' || ch == '+' || ch == '-') {
				return i;
			}
		}
		return length;
	}

	/**
	 * 公式的左边界限
	 */
	private int left(String string, int index) {
		for (int i = index - 1; i > 0; --i) {
			char ch = string.charAt(i);
			if (ch == '*' || ch == '/' || ch == '+' || ch == '-') {
				return i + 1;
			}
		}
		return 0;
	}

	/**
	 * 解析对应的公式
	 * 
	 * @param value
	 *            参数的值
	 */
	@SuppressWarnings("unused")
	private double resolve(String string) {
		try {
			if (string == null || string.equals("")) {
				return 0;
			}
			int index = string.indexOf("(");
			if (index == -1) {
				int multiplication = string.indexOf("*");
				int division = string.indexOf("/");
				String tempString = null;
				double result = 0;
				if (multiplication == division) {
					// 乘法和除法都没有
					int addition = string.indexOf("+");
					int subtraction = string.indexOf("-");
					if (addition == subtraction) {
						// 无任何运算
						return Double.valueOf(string);
					} else {
						if (addition == -1) {
							// 减法
							tempString = extract(string, subtraction);
							result = subtraction(tempString);
						} else if (subtraction == -1) {
							// 加法
							tempString = extract(string, addition);
							result = addition(tempString);
						} else {
							if (addition > subtraction) {
								// 减法
								tempString = extract(string, subtraction);
								result = subtraction(tempString);
							} else {
								// 加法
								tempString = extract(string, addition);
								result = addition(tempString);
							}
						}
					}
				} else {
					if (division == -1) {
						// 计算乘法
						tempString = extract(string, multiplication);
						result = multiplication(tempString);
					} else if (multiplication == -1) {
						// 计算除法
						tempString = extract(string, division);
						result = division(tempString);
					} else {
						if (division > multiplication) {
							// 计算乘法
							tempString = extract(string, multiplication);
							result = multiplication(tempString);
						} else {
							// 计算除法
							tempString = extract(string, division);
							result = division(tempString);
						}
					}
				}
				string = string.replace(tempString, String.valueOf(result));
				return resolve(string);
			} else {
				int lastIndex = string.lastIndexOf(")");
				String tempString = string.substring(index, lastIndex + 1);
				double temp = resolve(tempString.substring(1,
						tempString.length() - 1));
				string = string.replace(tempString, String.valueOf(temp));
				return resolve(string);
			}
		} catch (Exception e) {
			Log.error("公式解析错误", e);
			return 1;
		}
	}

	public double formula(String expression) {
		stringOperate temp2 = new stringOperate(expression);
		stactMachine stactmachine = new stactMachine(temp2.getstctcode());
		stactmachine.outputstactcode();
		return stactmachine.outputcompute();
	}

	public static void main(String[] args) {
		Formula.getInstance().formula("(9-6) * 3");
	}

	private class stactMachine {
		String stactcode = "";
		arrayStact tempstact = new arrayStact();

		public stactMachine(String temp) {
			stactcode = temp;
		}

		public void outputstactcode() {
			if (stactcode == "") {
				// System.out
				// .println("expression is wrong, so you must correct it~~!Then go on~!/n");
			} else {
				// for (int i = 0; i < stactcode.length(); i++){}
				// System.out.print(stactcode.charAt(i));
				// System.out.println("/n/n");
			}
		}

		public double outputcompute() {
			double tempa, tempb, tempc = 0;
			if (!"".equals(stactcode)) {
				String[] part = stactcode.split("/n");
				for (int j = 0; j < part.length; j++) {
					if (part[j].charAt(0) == 'P')
						tempstact.push(Double.parseDouble(part[j].substring(5,
								part[j].length())));
					else {

						tempb = tempstact.pop();
						tempa = tempstact.pop();

						if (part[j].charAt(0) == 'A') {
							tempc = tempa + tempb;
							// System.out.println("" + formatoutput(tempa) + "+"
							// + formatoutput(tempb) + " = " + tempc
							// + "/n");
						}
						if (part[j].charAt(0) == 'S') {
							tempc = tempa - tempb;
							// System.out.println("" + formatoutput(tempa) + "-"
							// + formatoutput(tempb) + " = " + tempc
							// + "/n");
						}
						if (part[j].charAt(0) == 'M') {
							tempc = tempa * tempb;
							// System.out.println("" + formatoutput(tempa) + "*"
							// + formatoutput(tempb) + " = " + tempc
							// + "/n");
						}
						if (part[j].charAt(0) == 'D') {
							tempc = tempa / tempb;
							// System.out.println("" + formatoutput(tempa) + "/"
							// + formatoutput(tempb) + " = " + tempc
							// + "/n");
						}
					}// end inner else
				}// end outer else
			}// end for
			return tempc;
		}

		@SuppressWarnings("unused")
		private String formatoutput(double temp) {
			if (temp < 0)
				return "(" + temp + ")";
			else
				return "" + temp + "";
		}
	}

	private class stringOperate {
		arrayStact stact1 = new arrayStact();// 存放操作数
		arrayStact stact2 = new arrayStact();// 存放运算符号
		String[] unit = new String[200]; // 存放解析出来的操作数与运算符
		int[][] priority = { // + - * / ( ) 优先级比较矩阵
		{ 2, 2, 1, 1, 1, 2, 2 }, { 2, 2, 1, 1, 1, 2, 2 },
				{ 2, 2, 2, 2, 1, 2, 2 }, { 2, 2, 2, 2, 1, 2, 2 },
				{ 1, 1, 1, 1, 1, 0, 3 }, { 2, 2, 2, 2, 3, 2, 2 },
				{ 1, 1, 1, 1, 1, 3, 0 },

		};
		// 0表示相等 1表示小于 2表示大于 3表示错误
		// + 用0表示，- 用1表示，* 用2表示，/ 用3表示，
		// ( 用4表示，) 用5表示,# 用6表示
		private String temp;// 用于构造函数
		private int arraypoint = 0;// 记录unit数组的存储长度

		private int i = 0;// 读取unit数组的元素

		private String tempstring;// 保存临时unit的值

		private String stactcode = "";// 栈码

		private int start = 0; // 数字字符串开始位置

		private int counter = 0; // 字符个数计数

		private int target = 0; // 保存开始计数位置

		private int lab = 1; // 表示计数结束输出数串

		private int index = 0; // 表示字符串读取位置的计数

		// private int iscorrect = 1;//
		// 表示是否继续读取整个字符串到存放数组(表示是是否是正确地表达式)，1表示继续，0表示停止

		private int isrecord = 0;// 表示是否存入数组，1表示存入数组合，0表示不存入数组

		private int isnext = 1; // 表示检测每一个数组单元值，如果非法为0，不非法为1（即进行下一步）

		private int leftbracket = 0, rightbracket = 0;// 左,右括号记数目

		public String getstctcode() {
			return stactcode;
		}

		public stringOperate(String passtemp) {
			this.temp = passtemp;
			processwhitespace();
			processexpression();

			if (leftbracket != rightbracket) {
				isnext = 0;
				// System.out
				// .println("the number of leftbracket and rightbracket is not equal~~!");
			}

			if (isnext == 1)
				computeexpression();
			else
				stactcode = "";
		}

		private void processwhitespace() {
			String emptystring = "";
			String[] tempstring = temp.split(" ");
			for (int i = 0; i < tempstring.length; i++) {
				emptystring += tempstring[i];
			}

			// //System.out.println("the expression to be compute:/n" +
			// emptystring);
			// //System.out.println("/n");

			temp = emptystring + "#";
			stact2.push(6);// 初始化操作符堆栈
		}

		private void partprocessexpression(String tempstringtemp) {
			if (iserrorexpression(tempstringtemp))
				isnext = 0;
			else {
				if (tempstringtemp == "-" && index == 0)
					isrecord = 1;
				else if (temp.charAt(index - 1) == ')') {
					unit[arraypoint] = tempstringtemp;
					arraypoint++;
				} else {
					lab = 1;
					if (lab == 1) {
						target = 0;
						lab = 0;

						if (counter == 0)
							;// ()时候移位
						else {
							if (isrecord == 1) {
								unit[arraypoint] = "-"
										+ temp.substring(start, start + counter);// 开始第一位为负数
								arraypoint++;
								isrecord = 0;
							} else {
								unit[arraypoint] = ""
										+ temp.substring(start, start + counter);
								arraypoint++;
							}
						}
						counter = 0;
					}

					unit[arraypoint] = tempstringtemp;
					arraypoint++;
				}// end inner else
			}// end outer else
		}

		private void processexpression()// 读取表达式，将预算符号与操作数分开，并存入数组
		{
			for (; index < temp.length(); index++) {
				switch (temp.charAt(index)) {
				case '(':
					if (iserrorexpression("("))
						isnext = 0;
					else {
						leftbracket++;
						unit[arraypoint] = "(";
						arraypoint++;

						if (temp.charAt(index + 1) == '-'
								&& temp.charAt(index + 2) - '0' >= 1
								&& temp.charAt(index + 2) - '0' <= 9) {
							int end = 0;
							for (int tempm = index + 1; tempm < temp.length(); tempm++) {
								if (temp.charAt(tempm) == ')') {
									end = tempm;
									break;
								} else
									;
							}
							unit[arraypoint] = temp.substring(index + 1, end);
							arraypoint++;

							index = end - 1;

							target = 0;
							lab = 0;
							counter = 0;
						}
					}// end else
					break;
				case ')':
					if (index == 0)
						isnext = 0;
					else {
						partprocessexpression(")");
						rightbracket++;
					}
					break;
				case '+':
					if (index == 0)
						isnext = 0;
					else
						partprocessexpression("+");
					break;
				case '-':
					partprocessexpression("-");
					break;
				case '*':
					if (index == 0)
						isnext = 0;
					else
						partprocessexpression("*");
					break;
				case '/':
					if (index == 0)
						isnext = 0;
					else
						partprocessexpression("/");
					break;
				case '#':
					unit[arraypoint] = "#";
					break;
				default:
					if (target == 0) {
						start = index;
						lab = 0;
						target = 1;
					}
					if (lab == 0)
						counter++;
					if (start + counter == temp.length() - 1) {
						unit[arraypoint] = ""
								+ temp.substring(start, start + counter);
						arraypoint++;

					}
				}// end switch
			}// edn for
		}// end processexpression

		private boolean iserrorexpression(String errortempstring) {
			return false;
			// boolean iserror = false;
			//
			// switch (errortempstring.charAt(0)) {
			// case '(':
			// if (temp.charAt(index) == ')'
			// || temp.charAt(index + 1) == ')'
			// || (temp.charAt(index) >= '0' && temp
			// .charAt(index) <= '9'))
			//
			// iserror = true;
			// break;
			// case ')':
			// if (temp.charAt(index) == '('
			// || temp.charAt(index + 1) == '('
			// || (temp.charAt(index + 1) >= '0' && temp
			// .charAt(index + 1) <= '9'))
			//
			// iserror = true;
			// break;
			// case '+':
			// case '*':
			// case '/':
			// if (temp.charAt(index) == '('
			// || temp.charAt(index - 1) == '+'
			// || temp.charAt(index - 1) == '-'
			// || temp.charAt(index - 1) == '*'
			// || temp.charAt(index - 1) == '/'
			// || temp.charAt(index + 1) == ')'
			// || temp.charAt(index + 1) == '+'
			// || temp.charAt(index + 1) == '-'
			// || temp.charAt(index + 1) == '*'
			// || temp.charAt(index + 1) == '/')
			//
			// iserror = true;
			// break;
			// case '-':
			// if (index != 0) {
			// if (temp.charAt(index) == '+'
			// || temp.charAt(index - 1) == '-'
			// || temp.charAt(index - 1) == '*'
			// || temp.charAt(index - 1) == '/'
			// || temp.charAt(index + 1) == ')'
			// || temp.charAt(index + 1) == '+'
			// || temp.charAt(index + 1) == '-'
			// || temp.charAt(index + 1) == '*'
			// || temp.charAt(index + 1) == '/')
			//
			// iserror = true;
			//
			// if (temp.charAt(index) == '(') {
			// if (temp.charAt(index + 1) - '0' >= 1
			// && temp.charAt(index + 1) - '0' <= 9)
			// iserror = false;
			// else
			//
			// iserror = true;
			//
			// }
			// } else {
			// if (temp.charAt(index + 1) == ')'
			// || temp.charAt(index + 1) == '+'
			// || temp.charAt(index + 1) == '-'
			// || temp.charAt(index + 1) == '*'
			// || temp.charAt(index + 1) == '/')
			//
			// iserror = true;
			// }
			// break;
			// default:
			// //System.out.println("error6 at" + (index - 1) + "~~"
			// + (index + 1));
			//
			// }// end switch
			//
			// return iserror;
		}

		private void computeexpression() {
			tempstring = unit[i];

			while (tempstring != "#" || stact2.gettop() != 6) {
				if (tempstring != "+" && tempstring != "-" && tempstring != "*"
						&& tempstring != "/" && tempstring != "("
						&& tempstring != ")" && tempstring != "#") {
					stact1.push(Double.parseDouble(unit[i]));
					stactcode = stactcode + "PUSH " + unit[i] + "/n";
					tempstring = unit[++i];
				} else {
					switch (tempstring.charAt(0)) {
					case '+':
						compareandprocess(0);
						break;
					case '-':
						compareandprocess(1);
						break;
					case '*':
						compareandprocess(2);
						break;
					case '/':
						compareandprocess(3);
						break;
					case '(':
						compareandprocess(4);
						break;
					case ')':
						compareandprocess(5);
						break;
					case '#':
						compareandprocess(6);
						break;
					}// end switch
				} // end else
			}// end while
		}

		private void compareandprocess(int a) {
			switch (priority[(int) stact2.gettop()][a]) {
			case 0:
				stact2.pop();
				tempstring = unit[++i];
				break;
			case 1:
				stact2.push(a);
				tempstring = unit[++i];
				break;
			case 2:
				partcompareandprocess();
				break;
			case 3: {
				// //System.out.println("error~!");
				// //System.out.println(stact2.gettop() + "  " + a);
			}
			}// end switch
		}// end method compareandprocess

		private void partcompareandprocess() {
			double tempa, tempb, tempc;
			double tempoperator;
			tempoperator = stact2.pop();
			tempb = stact1.pop();
			tempa = stact1.pop();

			switch ((int) tempoperator) {
			case 0:
				tempc = tempa + tempb;
				stact1.push(tempc);
				stactcode = stactcode + "ADD" + "/n";
				stactcode = stactcode + "PUSH " + tempc + "/n";
				break;
			case 1:
				tempc = tempa - tempb;
				stact1.push(tempc);
				stactcode = stactcode + "SUB" + "/n";
				stactcode = stactcode + "PUSH " + tempc + "/n";
				break;
			case 2:
				tempc = tempa * tempb;
				stact1.push(tempc);
				stactcode = stactcode + "MLU" + "/n";
				stactcode = stactcode + "PUSH " + tempc + "/n";
				break;
			case 3:
				tempc = tempa / tempb;
				stact1.push(tempc);
				stactcode = stactcode + "DIV" + "/n";
				stactcode = stactcode + "PUSH " + tempc + "/n";
				break;
			}
		}// end method partcompareandprocess
	}

	private class arrayStact {
		private int pointcounter;// stact bottom pointer
		double[] stact = new double[100];

		public arrayStact() {
			for (int i = 0; i < stact.length; i++)
				stact[i] = -1;
			pointcounter = 0;
		}

		public void push(double temp) {
			pointcounter++;

			if (pointcounter < 100) {
				for (int i = pointcounter; i > 0; i--)
					stact[i] = stact[i - 1];
				stact[0] = temp;

			} else {
			}
			// System.out
			// .println("the length of expression is over the length of stact~!/n");
		}

		public double pop() {
			double temptop = -1;
			if (isEmpty()) {
				// System.out.println("the stact is empty!/n");
			} else {
				temptop = stact[0];
				for (int i = 0; i < pointcounter; i++)
					stact[i] = stact[i + 1];

				pointcounter--;
			}
			return temptop;
		}

		public double gettop() {
			return stact[0];
		}

		private boolean isEmpty() {
			if (pointcounter == 0)
				return true;
			else
				return false;
		}
	}
}
