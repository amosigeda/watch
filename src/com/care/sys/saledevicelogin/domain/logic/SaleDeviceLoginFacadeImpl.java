package com.care.sys.saledevicelogin.domain.logic;

import java.util.List;

import com.care.sys.saledevicelogin.dao.SaleDeviceLoginDao;
import com.care.sys.saledevicelogin.domain.SaleDeviceLogin;
import com.godoing.rose.lang.DataList;
import com.godoing.rose.lang.DataMap;
import com.godoing.rose.lang.SystemException;

public class SaleDeviceLoginFacadeImpl implements SaleDeviceLoginFacade{

	SaleDeviceLoginDao saleDeviceLoginDao;
	
	public SaleDeviceLoginDao getSaleDeviceLoginDao() {
		return saleDeviceLoginDao;
	}

	public void setSaleDeviceLoginDao(SaleDeviceLoginDao saleDeviceLoginDao) {
		this.saleDeviceLoginDao = saleDeviceLoginDao;
	}


	public int insertSaleDeviceLogin(SaleDeviceLogin vo) throws SystemException {
		return saleDeviceLoginDao.insertSaleDeviceLogin(vo);
	}
	
	public DataList getSaleDeviceLoginByVo(SaleDeviceLogin vo)
			throws SystemException {
		DataList  list = new DataList(saleDeviceLoginDao.getSaleDeviceLoginS(vo));
		list.setTotalSize(saleDeviceLoginDao.getSaleDeviceLoginCount(vo));
		return list;
	}
	
	public int insertSaleDeviceLoginList(List<SaleDeviceLogin> list)
			throws SystemException {
		return saleDeviceLoginDao.insertSaleDeviceLoginList(list);
	}

	public DataList getBTDeviceAddDayGroupByTime(SaleDeviceLogin vo)
			throws SystemException {
		DataList list = new DataList(saleDeviceLoginDao.getBTDeviceAddDayGroupByTime(vo));
		list.setTotalSize(saleDeviceLoginDao.getCountBTDeviceAddDayGroupByTime(vo));
		return list;
	}
}
