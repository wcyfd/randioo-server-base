package com.randioo.randioo_server_base.utils.game.record;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.randioo.randioo_server_base.Role;
import com.randioo.randioo_server_base.utils.ReflectUtils;

public class GameRecorder {
	private Map<Recordable, RecordPoint> recordableMap = new HashMap<>();

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

		RecordPoint recordPoint = recordableMap.get(recordable);
		if (recordPoint == null) {
			recordPoint = new RecordPoint();
			recordPoint.setRecordable(recordable);
			recordableMap.put(recordable, recordPoint);
		}

		return recordPoint;
	}

	/**
	 * 添加一条记录
	 * 
	 * @param recordable
	 * @param recordInfo
	 * @author wcy 2016年12月20日
	 */
	public void addRecord(Recordable recordable, AbstractRecordInfo recordInfo) {
		RecordPoint point = this.getRecordPoint(recordable);
		point.getRecordInfoList().add(0, recordInfo);
	}

	/**
	 * 适用于基本数据类型的记录
	 * 
	 * @param recordable
	 * @param method
	 * @param target
	 * @param params
	 * @author wcy 2016年12月20日
	 */
	public void addRecord(Recordable recordable, Method method, Object target, Object... params) {
		addRecord(recordable, new ValueRecordInfo(method, target, params));
	}

	/**
	 * 清楚记录
	 * 
	 * @param recordable
	 * @author wcy 2016年12月20日
	 */
	public void clearRecord(Recordable recordable) {
		this.getRecordPoint(recordable).getRecordInfoList().clear();
	}

	/**
	 * 重置
	 * 
	 * @param recordable
	 * @author wcy 2016年12月20日
	 */
	public void resetRecord(Recordable recordable) {
		RecordPoint point = this.getRecordPoint(recordable);
		for (AbstractRecordInfo recordInfo : point.getRecordInfoList()) {
			recordInfo.reset();
		}
	}

}
