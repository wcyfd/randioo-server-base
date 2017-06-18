package com.randioo.randioo_server_base.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.randioo.randioo_server_base.utils.RandomUtils;
import com.randioo.randioo_server_base.utils.template.Function;

public class GameUtils {

	/**
	 * 根据权重获得值
	 * 
	 * @param value
	 * @param list
	 * @param weightValue
	 * @return
	 * @author wcy 2017年2月25日
	 * @throws Exception
	 */
	public static <T, V> V getValueByWeight(int value, Collection<T> list, WeightGet<T> weightValue,
			ValueGet<T, V> valueGet) {
		int min = 0;
		int max = 0;
		for (T t : list) {
			int weight = weightValue.getWeightValue(t);
			if (weight == 0)
				continue;
			max += weight;
			if (value >= min && value <= max)
				return valueGet.getValue(t);

			min += weight;
		}

		throw new RuntimeException("Out of WeightValue,weight is " + value);
	}

	public static <T, V> List<V> getRandomCountValuesByWeightValue(int count, Collection<T> collections,
			WeightGet<T> weightValue, ValueGet<T, V> valueGet) {
		int totalWeight = 0;
		for (T value : collections) {
			int weight = weightValue.getWeightValue(value);
			totalWeight += weight;
		}

		return getRandomCountValuesByWeight(count, collections, weightValue, valueGet, totalWeight);
	}

	public static <T, V> List<V> getRandomCountValuesByWeight(int count, Collection<T> collections,
			WeightGet<T> weightValue, ValueGet<T, V> valueGet, int totalWeight) {
		return getRandomCountValuesByWeight(count, collections, weightValue, valueGet, 0, totalWeight);
	}

	public static <T, V> List<V> getRandomCountValuesByWeight(int count, Collection<T> collections,
			WeightGet<T> weightValue, ValueGet<T, V> valueGet, int startRange, int endRange) {

		List<V> result = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			int index = RandomUtils.getRandomNum(startRange, endRange);
			V value = getValueByWeight(index, collections, weightValue, valueGet);

			result.add(value);
		}
		return result;
	}

	/**
	 * map<A,map<B,C>>注入
	 * 
	 * @param outerMap
	 * @param outerId
	 * @param innerKey
	 * @param po
	 * @param function
	 * @author wcy 2017年3月1日
	 */
	public static <A, B, C> void mapABCInsert(Map<A, Map<B, C>> outerMap, A outerId, B innerKey, C po,
			Function function, boolean insteadPo) {
		Map<B, C> map = outerMap.get(outerId);
		if (map == null) {
			map = new LinkedHashMap<>();
			outerMap.put(outerId, map);
		}

		if (function != null) {
			Boolean result = (Boolean) function.apply(po);
			if (result) {
				C c = map.get(innerKey);
				if (c == null) {
					map.put(innerKey, po);
				} else if (insteadPo) {
					map.put(innerKey, po);
				}
			}
		} else {
			C c = map.get(innerKey);
			if (c == null) {
				map.put(innerKey, po);
			} else if (insteadPo) {
				map.put(innerKey, po);
			}
		}
	}

	/**
	 * map<A,map<B,C>>注入
	 * 
	 * @param outerMap
	 * @param outerId
	 * @param innerKey
	 * @param po
	 * @param function
	 * @author wcy 2017年3月1日
	 */
	public static <A, B, C> void mapABCInsert(Map<A, Map<B, C>> outerMap, A outerId, B innerKey, C po) {
		mapABCInsert(outerMap, outerId, innerKey, po, null, false);
	}

	public static <V> V mapGetValue(Map<?, ?> map, Object... keys) {
		return mapGet(map, 0, keys);
	}

	@SuppressWarnings("unchecked")
	private static <V> V mapGet(Map<?, ?> map, int index, Object... keys) {
		Object value = map.get(keys[index]);
		if (value == null)
			return null;
		if (keys.length - 1 == index)
			return (V) value;
		index++;
		return mapGet((Map<?, ?>) value, index, keys);
	}

	/**
	 * 随机一个map中的值
	 * 
	 * @param map
	 * @return
	 * @author wcy 2017年3月1日
	 */
	public static <A, B> B randomMapValue(Map<A, B> map) {
		List<B> list = new ArrayList<>(map.values());
		if (list.size() == 0) {
			return null;
		}
		int index = RandomUtils.getRandomNum(list.size());
		return list.get(index);
	}

	/**
	 * 随机一个map中的键
	 * 
	 * @param map
	 * @return
	 * @author wcy 2017年3月1日
	 */
	public static <A, B> A randomMapKey(Map<A, B> map) {
		List<A> list = new ArrayList<>(map.keySet());
		if (list.size() == 0) {
			return null;
		}
		int index = RandomUtils.getRandomNum(list.size());
		return list.get(index);
	}

	public enum FloatToIntegerType {
		FLOOR, CEIL
	}

	public static int percentAdd(int value, int percent, FloatToIntegerType type) {
		switch (type) {
		case CEIL:
			value += Math.ceil(percent / 100.0f * value);
			break;
		case FLOOR:
			value += Math.floor(percent / 100.0f * value);
			break;
		default:
			break;
		}
		return value;
	}

	public static int protectedGetTotalValue(int originValue, int addValue) {
		// TODO Auto-generated method stub
		int total = originValue + addValue;
		if (total < 0) {
			if (addValue >= 0) {
				total = Integer.MAX_VALUE;
			} else {
				total = 0;
			}
		}
		return total;
	}

}
