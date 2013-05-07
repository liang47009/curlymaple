package test.com.yunfeng.protocol;

public class Message {
	private byte module;
	private byte command;
	private int length;
	private Object data;

	public byte getModule() {
		return module;
	}

	public void setModule(byte module) {
		this.module = module;
	}

	public byte getCommand() {
		return command;
	}

	public void setCommand(byte command) {
		this.command = command;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
