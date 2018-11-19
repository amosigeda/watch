package com.care.sys.projectinfo.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.care.sys.projectinfo.domain.ProjectInfo;
import com.godoing.rose.lang.DataMap;

public interface ProjectInfoDao {
	
	public List<DataMap> getProjectInfo(ProjectInfo vo) throws DataAccessException;
	
	public List<DataMap> getProjectName(ProjectInfo vo) throws DataAccessException;
	
	public List<DataMap> getProjectInfoListByVo(ProjectInfo vo) throws DataAccessException;
	
	public int getProjectInfoListCountByVo(ProjectInfo vo) throws DataAccessException;
	
	public int getProjectInfoMaxId(ProjectInfo vo) throws DataAccessException;
	
	public int getProjectInfoCount(ProjectInfo vo)throws DataAccessException;
	
	public int insertProjectInfo(ProjectInfo vo) throws DataAccessException;

	public int insertRelevanceInfo2(ProjectInfo vo)throws DataAccessException;
	
	public int updatePorjectInfo(ProjectInfo vo) throws DataAccessException;

	public List<DataMap> getProjectStatus(ProjectInfo vo)throws DataAccessException;

	public List<DataMap> getProjectRoleInfo(ProjectInfo rvo)throws DataAccessException;

	public List<DataMap> getRoleProjectFuncInfo(ProjectInfo vo)throws DataAccessException;

	public int insertProjectRoleFuncInfo(ProjectInfo vo)throws DataAccessException;

	public int deleteProjectRoleFunInfo(ProjectInfo vo)throws DataAccessException;

	public List<DataMap> getProjectLocationSwitchInfo(ProjectInfo vo)throws DataAccessException;

	public List<DataMap> getProjectLocationSwitchInfoBvo(ProjectInfo vo)throws DataAccessException;

	public int getProjectLocationInfoListCountByVo(ProjectInfo vo)throws DataAccessException;

	public int updatePorjectSwitchInfo(ProjectInfo vo)throws DataAccessException;

	public int deletePorjectInfoxml(ProjectInfo vo)throws DataAccessException;

	public List<DataMap> getWatchInfoListByVo(ProjectInfo vo)throws DataAccessException;

	public int getWatchInfoListCountByVo(ProjectInfo vo)throws DataAccessException;

	public int insertProjectWatchInfo(ProjectInfo vo)throws DataAccessException;

	public List<DataMap> getProjectWatchInfo(ProjectInfo vo)throws DataAccessException;

	public int updatePorjectWatchInfo(ProjectInfo vo)throws DataAccessException;

	public int deletePorjectInfoWatch(ProjectInfo vo)throws DataAccessException;

	public List<DataMap> getProjectUserInfo(ProjectInfo vo)throws DataAccessException;

}
