package com.randioo.randioo_server_base.module.record;

import java.lang.reflect.Method;
import java.util.Map;

import com.randioo.randioo_server_base.service.BaseService;

public class RecordModelServiceImpl extends BaseService implements RecordModelService {

	/**
	 * 获得记录点
	 * 
	 * @param recordable
	 * @return
	 * @author wcy 2016年12月20日
	 */
	private RecordPoint getRecordPoint(Recordable recordable) {
		if (recordable == null)
			return null;

		Map<Recordable, RecordPoint> recordableMap = RecordCache.getRecordableMap();
		RecordPoint recordPoint = recordableMap.get(recordable);
		if (recordPoint == null) {
			recordPoint = new RecordPoint();
			recordPoint.setRecordable(recordable);
			recordableMap.put(recordable, recordPoint);
		}

		return recordPoint;
	}

	@Override
	public void addRecord(Recordable recordable, AbstractRecordInfo recordInfo) {
		RecordPoint point = this.getRecordPoint(recordable);
		point.getRecordInfoList().add(0, recordInfo);
	}

	@Override
	public void addRecord(Recordable recordable, Method method, Object target, Object... params) {
		addRecord(recordable, new ValueRecordInfo(method, target, params));
	}

	@Override
	public void clearRecord(Recordable recordable) {
		this.getRecordPoint(recordable).getRecordInfoList().clear();
	}

	@Override
	public void resetRecord(Recordable recordable) {
		RecordPoint point = this.getRecordPoint(recordable);
		for (AbstractRecordInfo recordInfo : point.getRecordInfoList()) {
			recordInfo.reset();
		}
	}

}
