package com.care.sys.locationinfo.dao.ibatis;

import java.util.List;

import org.apache.commons.logging.Log;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.care.sys.locationinfo.dao.LocationInfoDao;
import com.care.sys.locationinfo.domain.LocationInfo;
import com.godoing.rose.lang.DataMap;
import com.godoing.rose.log.LogFactory;

public class SqlMapLocationInfoDao extends SqlMapClientDaoSupport implements LocationInfoDao{
	
	Log logger = LogFactory.getLog(SqlMapLocationInfoDao.class);
	public SqlMapLocationInfoDao(){
		
	}

	public List<DataMap> getLocationInfo(LocationInfo vo)
			throws DataAccessException {
		logger.debug("getLocationInfo(LocationInfo vo)");
		return getSqlMapClientTemplate().queryForList("getLocationInfo", vo);
	}
	
	public List<DataMap> getLocationListInfo(LocationInfo vo) throws DataAccessException {
		logger.debug("getLocationListInfo(LocationInfo vo)");
		return getSqlMapClientTemplate().queryForList("getLocationListInfo", vo);
	}

	public int insertLocationInfo(LocationInfo vo) throws DataAccessException {
		logger.debug("insertLocationInfo(LocationInfo vo)");
		return getSqlMapClientTemplate().update("insertLocationInfo", vo);
	}

	public int getLocationInfoCount(LocationInfo vo) throws DataAccessException {
		logger.debug("getLocationInfoCount(LocationInfo vo)");
		return (Integer)getSqlMapClientTemplate().queryForObject("getLocationInfoCount", vo);
	}

	public int updateLocationInfo(LocationInfo vo) throws DataAccessException {
		// TODO Auto-generated method stub
		logger.debug("updateLocationInfo(LocationInfo vo)");
		return getSqlMapClientTemplate().update("updateLocationInfo", vo);
	}

	public List<DataMap> getLocationInfoGroupByTime(LocationInfo vo)
			throws DataAccessException {
		logger.debug("getLocationInfoGroupByTime(LocationInfo vo)");
		return getSqlMapClientTemplate().queryForList("getLocationInfoGroupByTime", vo);
	}

	public int insertLocationInfoBackup(LocationInfo vo)
			throws DataAccessException {
		logger.debug("insertLocationInfoBackup(LocationInfo vo)");
		return getSqlMapClientTemplate().update("insertLocationInfoBackup",vo);
	}

	public int deleteLocationInfo(LocationInfo vo) throws DataAccessException {
		logger.debug("deleteLocationInfo(LocationInfo vo)");
		return getSqlMapClientTemplate().update("deleteLocationInfo", vo);
	}

	public int insertGpsLocationInfo(LocationInfo vo)
			throws DataAccessException {
		logger.debug("insertGpsLocationInfo(LocationInfo vo)");
		return getSqlMapClientTemplate().update("insertGpsLocationInfo", vo);
	}

	public int insertTestLocation(LocationInfo vo) throws DataAccessException {
		logger.debug("insertTestLocation(LocationInfo vo)");
		return getSqlMapClientTemplate().update("insertTestLocation", vo);
	}

}
