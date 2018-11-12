package com.care.sys.saledevicelogin.dao;



import java.util.List;

import org.springframework.dao.DataAccessException;

import com.care.sys.saledevicelogin.domain.SaleDeviceLogin;
import com.godoing.rose.lang.DataList;
import com.godoing.rose.lang.DataMap;

public interface SaleDeviceLoginDao {

	public int insertSaleDeviceLogin(SaleDeviceLogin vo) throws DataAccessException;

	public List<DataMap> getSaleDeviceLoginS(SaleDeviceLogin vo) throws DataAccessException;
	
	public int getSaleDeviceLoginCount(SaleDeviceLogin vo) throws DataAccessException;
	
	public int insertSaleDeviceLoginList(List<SaleDeviceLogin> list)throws DataAccessException;
	
	public List<DataMap> getBTDeviceAddDayGroupByTime(SaleDeviceLogin vo)throws DataAccessException;
	
	public int getCountBTDeviceAddDayGroupByTime(SaleDeviceLogin vo) throws DataAccessException;
}
