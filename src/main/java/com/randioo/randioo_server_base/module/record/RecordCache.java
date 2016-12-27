package com.randioo.randioo_server_base.module.record;

import java.util.HashMap;
import java.util.Map;

public class RecordCache {
	private static Map<Recordable, RecordPoint> recordableMap = new HashMap<>();
	
	public static Map<Recordable, RecordPoint> getRecordableMap() {
		return recordableMap;
	}
}
