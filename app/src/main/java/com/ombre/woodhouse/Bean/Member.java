package com.ombre.woodhouse.Bean;

import java.util.Date;

/**
 * Member entity.
 * 
 * @author MyEclipse Persistence Tools
 */
//用户
public class Member implements java.io.Serializable {

	// Fields

	private Integer id;
	private String loginName;//用户账号
	private String loginPwd;//用户密码
	private String memberName;//用户昵称
	private String phone;//用户电话
	private String email;//邮件
	
	// Constructors

	/** default constructor */
	public Member() {
	}

	/** full constructor */
	public Member( String loginName, String loginPwd,
                  String memberName, String phone, String email) {
		this.loginName = loginName;
		this.loginPwd = loginPwd;
		this.memberName = memberName;
		this.phone = phone;
		this.email = email;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginPwd() {
		return this.loginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	public String getMemberName() {
		return this.memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}