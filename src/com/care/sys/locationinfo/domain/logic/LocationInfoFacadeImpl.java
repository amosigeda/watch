package com.care.sys.locationinfo.domain.logic;

import java.util.List;
import com.care.sys.locationinfo.dao.LocationInfoDao;
import com.care.sys.locationinfo.domain.LocationInfo;
import com.godoing.rose.lang.DataList;
import com.godoing.rose.lang.DataMap;
import com.godoing.rose.lang.SystemException;

public class LocationInfoFacadeImpl implements LocationInfoFacade{
	
	private LocationInfoDao locationInfoDao;
	

	public void setLocationInfoDao(LocationInfoDao locationInfoDao) {
		this.locationInfoDao = locationInfoDao;
	}

	public List<DataMap> getLocationInfo(LocationInfo vo)
			throws SystemException {
		return locationInfoDao.getLocationInfo(vo);
	}
	
	public List<DataMap> getLocationListInfo(LocationInfo vo) throws SystemException {
		return locationInfoDao.getLocationListInfo(vo);
	}

	public int insertLocationInfo(LocationInfo vo) throws SystemException {
		return locationInfoDao.insertLocationInfo(vo);
	}

	public DataList getLocationInfoListByVo(LocationInfo vo)
			throws SystemException {
		DataList list = new DataList(locationInfoDao.getLocationInfo(vo));
		list.setTotalSize(locationInfoDao.getLocationInfoCount(vo));
		return list;
	}

	public int updateLocationInfo(LocationInfo vo) throws SystemException {
		// TODO Auto-generated method stub
		return locationInfoDao.updateLocationInfo(vo);
	}

	public List<DataMap> getLocationInfoGroupByTime(LocationInfo vo)
			throws SystemException {
		// TODO Auto-generated method stub
		return locationInfoDao.getLocationInfoGroupByTime(vo);
	}

	public int insertLocationInfoBackup(LocationInfo vo) throws SystemException {
		// TODO Auto-generated method stub
		return locationInfoDao.insertLocationInfoBackup(vo);
	}

	public int deleteLocationInfo(LocationInfo vo) throws SystemException {
		// TODO Auto-generated method stub
		return locationInfoDao.deleteLocationInfo(vo);
	}

	public int insertGpsLocationInfo(LocationInfo vo) throws SystemException {
		return locationInfoDao.insertGpsLocationInfo(vo);
	}

	public int insertTestLocation(LocationInfo vo) throws SystemException {
		return locationInfoDao.insertTestLocation(vo);
	}
}

	
