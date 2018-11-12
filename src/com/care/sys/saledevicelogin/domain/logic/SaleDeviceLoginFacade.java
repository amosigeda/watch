package com.care.sys.saledevicelogin.domain.logic;


import java.util.List;

import com.care.sys.saledevicelogin.domain.SaleDeviceLogin;
import com.godoing.rose.lang.DataList;
import com.godoing.rose.lang.DataMap;
import com.godoing.rose.lang.SystemException;

public interface SaleDeviceLoginFacade {

	public int insertSaleDeviceLogin(SaleDeviceLogin vo) throws SystemException;
	
	public DataList getSaleDeviceLoginByVo(SaleDeviceLogin vo) throws SystemException;
	
	public int insertSaleDeviceLoginList(List<SaleDeviceLogin> list) throws SystemException;
	
	public DataList getBTDeviceAddDayGroupByTime(SaleDeviceLogin vo) throws SystemException;
}
