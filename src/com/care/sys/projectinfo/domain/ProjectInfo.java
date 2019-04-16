package com.care.sys.projectinfo.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.godoing.rose.http.PublicVoBean;

public class ProjectInfo extends PublicVoBean implements Serializable{
	private static final long serialVersionUID = 1L;
	public ProjectInfo(){
		
	}
	
	private Integer id;
	private String projectNo;
	private String projectName;
	private String channelId;
	private String companyId;
	private String status;
	private Date addTime;
	private String remark;
	private String projectStatus;
	private Integer idP;
	private String roleCodeP;
	private String mName;
	private String projectCode;
	private String need;
	private String heartS;
	private int shiqu;
	private Integer projectType;  //��Ŀ���ͣ�1��λ��2������3����
	private Integer dataSourceC; //���� LBS���Դ��1���ߵ£�2���MiNiGps 0�� 
	private Integer dataSourceO;  //���� LBS���Դ ��1���ߵ� 2MiNiGps 0��
	private Integer mapTypeC;  //���ڵ�ͼ���� ͼ�̣�1���ߵµ�ͼ��2���Google��ͼ��0���û��ͼ
	private Integer mapTypeO;  //�����ҵ�ͼ���� ͼ�̣�1���ߵµ�ͼ��2���Google��ͼ��0���û��ͼ
	private Integer socketWay;  //Socket���ӷ�����1���mina������ӣ�2���ֱ��socket,0�����
	private String locationSwitch;
	private String beifen;
	
	private String adTitle;
	private String adDetail;
	
	
	private String username;
	private String nickname;
	private String avatar;
	private Integer use_status;
	private Timestamp createtime;
	
	
	
	public Timestamp getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public Integer getUse_status() {
		return use_status;
	}
	public void setUse_status(Integer use_status) {
		this.use_status = use_status;
	}
	public String getAdTitle() {
		return adTitle;
	}
	public void setAdTitle(String adTitle) {
		this.adTitle = adTitle;
	}
	public String getAdDetail() {
		return adDetail;
	}
	public void setAdDetail(String adDetail) {
		this.adDetail = adDetail;
	}
	public String getNeed() {
		return need;
	}
	public void setNeed(String need) {
		this.need = need;
	}
	public String getHeartS() {
		return heartS;
	}
	public void setHeartS(String heartS) {
		this.heartS = heartS;
	}
	public int getShiqu() {
		return shiqu;
	}
	public void setShiqu(int shiqu) {
		this.shiqu = shiqu;
	}
	public Integer getProjectType() {
		return projectType;
	}
	public void setProjectType(Integer projectType) {
		this.projectType = projectType;
	}
	public Integer getDataSourceC() {
		return dataSourceC;
	}
	public void setDataSourceC(Integer dataSourceC) {
		this.dataSourceC = dataSourceC;
	}
	public Integer getDataSourceO() {
		return dataSourceO;
	}
	public void setDataSourceO(Integer dataSourceO) {
		this.dataSourceO = dataSourceO;
	}
	public Integer getMapTypeC() {
		return mapTypeC;
	}
	public void setMapTypeC(Integer mapTypeC) {
		this.mapTypeC = mapTypeC;
	}
	public Integer getMapTypeO() {
		return mapTypeO;
	}
	public void setMapTypeO(Integer mapTypeO) {
		this.mapTypeO = mapTypeO;
	}
	public Integer getSocketWay() {
		return socketWay;
	}
	public void setSocketWay(Integer socketWay) {
		this.socketWay = socketWay;
	}
	public String getBeifen() {
		return beifen;
	}
	public void setBeifen(String beifen) {
		this.beifen = beifen;
	}
	public String getLocationSwitch() {
		return locationSwitch;
	}
	public void setLocationSwitch(String locationSwitch) {
		this.locationSwitch = locationSwitch;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getProjectNo() {
		return projectNo;
	}
	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Integer getIdP() {
		return idP;
	}
	public void setIdP(Integer idP) {
		this.idP = idP;
	}
	public String getRoleCodeP() {
		return roleCodeP;
	}
	public void setRoleCodeP(String roleCodeP) {
		this.roleCodeP = roleCodeP;
	}
	
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	public String getProjectStatus() {
		return projectStatus;
	}
	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}
	

}
