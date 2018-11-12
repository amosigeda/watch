package com.care.sys.saledevicelogin.dao.ibatis;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.care.sys.saledevicelogin.dao.SaleDeviceLoginDao;
import com.care.sys.saledevicelogin.domain.SaleDeviceLogin;
import com.godoing.rose.lang.DataMap;

public class SqlMapSaleDeviceLoginDao extends SqlMapClientDaoSupport implements SaleDeviceLoginDao{
	
	Log logger = LogFactory.getLog(SqlMapSaleDeviceLoginDao.class);
	
	public int insertSaleDeviceLogin(SaleDeviceLogin vo) throws DataAccessException {
		// TODO Auto-generated method stub
		logger.debug("insertSaleDeviceLogin(SaleDeviceLogin vo)");
		return getSqlMapClientTemplate().update("insertSaleDeviceLogin", vo);
	}


	public List<DataMap> getSaleDeviceLoginS(SaleDeviceLogin vo)
			throws DataAccessException {
		logger.debug("getSaleDeviceLoginS");
		return  getSqlMapClientTemplate().queryForList("getSaleDeviceLoginS",vo);
	}

	public int getSaleDeviceLoginCount(SaleDeviceLogin vo)
			throws DataAccessException {
		logger.debug("getSaleDeviceLoginCount");
		return (Integer) getSqlMapClientTemplate().queryForObject("getSaleDeviceLoginCount", vo);
	}


	public int insertSaleDeviceLoginList(List<SaleDeviceLogin> list)
			throws DataAccessException {
		logger.debug("insertSaleDeviceLoginList");
		return  getSqlMapClientTemplate().update("insertSaleDeviceLoginList", list);
	}


	public List<DataMap> getBTDeviceAddDayGroupByTime(SaleDeviceLogin vo)
			throws DataAccessException {
		logger.debug("getBTDeviceAddDayGroupByTime");
		return getSqlMapClientTemplate().queryForList("getBTDeviceAddDayGroupByTime",vo);
	}


	public int getCountBTDeviceAddDayGroupByTime(SaleDeviceLogin vo)
			throws DataAccessException {
		logger.debug("getCountBTDeviceAddDayGroupByTime");
		return (Integer) getSqlMapClientTemplate().queryForObject("getCountBTDeviceAddDayGroupByTime", vo);
	}



	
	
}
