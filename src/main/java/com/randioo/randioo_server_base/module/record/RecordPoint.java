package com.randioo.randioo_server_base.module.record;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录点
 * 
 * @author wcy 2016年12月20日
 *
 */
public class RecordPoint {
	private Recordable recordable;
	private List<AbstractRecordInfo> recordInfoSet = new ArrayList<>();

	protected Recordable getRecordable() {
		return recordable;
	}

	public void setRecordable(Recordable recordable) {
		this.recordable = recordable;
	}

	protected List<AbstractRecordInfo> getRecordInfoList() {
		return recordInfoSet;
	}
}
