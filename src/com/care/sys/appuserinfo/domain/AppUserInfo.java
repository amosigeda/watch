package com.care.sys.appuserinfo.domain;

import java.io.Serializable;
import java.util.Date;

import com.godoing.rose.http.PublicVoBean;

public class AppUserInfo extends PublicVoBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6441812890203395966L;
	private Integer id;
	private String userName;
	private String password;
	private String nickName;
	private String age;
	private String sex;
	private Date createTime;
	private String status;
	private Date lastLoginTime;
	private String head;
	private String userHeight;
	private String userWeight;
	private String belongProject;
	private String bindCount;
	private String photoName;
	private String shuoMing;
	private String head2;
	private String head3;
	private String head4;
	private String head5;
	private String head6;
	
	public String getHead2() {
		return head2;
	}
	public void setHead2(String head2) {
		this.head2 = head2;
	}
	public String getHead3() {
		return head3;
	}
	public void setHead3(String head3) {
		this.head3 = head3;
	}
	public String getHead4() {
		return head4;
	}
	public void setHead4(String head4) {
		this.head4 = head4;
	}
	public String getHead5() {
		return head5;
	}
	public void setHead5(String head5) {
		this.head5 = head5;
	}
	public String getHead6() {
		return head6;
	}
	public void setHead6(String head6) {
		this.head6 = head6;
	}
	public String getShuoMing() {
		return shuoMing;
	}
	public void setShuoMing(String shuoMing) {
		this.shuoMing = shuoMing;
	}
	public String getPhotoName() {
		return photoName;
	}
	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}
	public String getBindCount() {
		return bindCount;
	}
	public void setBindCount(String bindCount) {
		this.bindCount = bindCount;
	}
	public String getBelongProject() {
		return belongProject;
	}
	public void setBelongProject(String belongProject) {
		this.belongProject = belongProject;
	}
	public String getUserWeight() {
		return userWeight;
	}
	public void setUserWeight(String userWeight) {
		this.userWeight = userWeight;
	}
	public String getUserHeight() {
		return userHeight;
	}
	public void setUserHeight(String userHeight) {
		this.userHeight = userHeight;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
}
