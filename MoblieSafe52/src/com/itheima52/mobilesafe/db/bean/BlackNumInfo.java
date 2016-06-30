package com.itheima52.mobilesafe.db.bean;

public class BlackNumInfo {
	public String phone ;
	public String mode ;
	
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	@Override
	public String toString() {
		return "BlackNumInfo [phone=" + phone + ", mode=" + mode + "]";
	}

}
