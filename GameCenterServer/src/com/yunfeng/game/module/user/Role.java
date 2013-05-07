package com.yunfeng.game.module.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Role {

	private int id;
	private int uid;// 用户id
	private String nick;// 昵称
	private volatile int integral;// 积分
	private volatile long bwcuy;// 血战币
	private volatile long tile;// 板砖
	private volatile long diamond;// 钻石
	private volatile int winCount;// 胜利数
	private volatile int losCount;// 失败数
	private volatile int loginCount;// 连续登陆次数
	private volatile long gag;// 禁言(-1:永久禁言, 0:未禁言, 大于0:禁言时长)

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public long getBwcuy() {
		return bwcuy;
	}

	public void setBwcuy(long bwcuy) {
		this.bwcuy = bwcuy;
	}

	public long getTile() {
		return tile;
	}

	public void setTile(long tile) {
		this.tile = tile;
	}

	public long getDiamond() {
		return diamond;
	}

	public void setDiamond(long diamond) {
		this.diamond = diamond;
	}

	public int getWinCount() {
		return winCount;
	}

	public void setWinCount(int winCount) {
		this.winCount = winCount;
	}

	public int getLosCount() {
		return losCount;
	}

	public void setLosCount(int losCount) {
		this.losCount = losCount;
	}

	public int getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}

	public long getGag() {
		return gag;
	}

	public void setGag(long gag) {
		this.gag = gag;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

}