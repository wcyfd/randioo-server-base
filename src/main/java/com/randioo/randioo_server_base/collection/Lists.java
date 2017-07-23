package com.randioo.randioo_server_base.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lists {
	/**
	 * 填充列表
	 * 
	 * @param list
	 * @param arr
	 * @return
	 */
	public static void fillList(List<Integer> list, int[] arr) {
		for (int i : arr)
			list.add(i);
	}

	/**
	 * 变为动态数组
	 * 
	 * @param arr
	 * @return
	 */
	public static <T> List<T> asDynamicList(T[] arr) {
		return asDynamicList(arr, 0, arr.length);
	}

	/**
	 * 变为动态数组
	 * 
	 * @param arr
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	public static <T> List<T> asDynamicList(T[] arr, int startIndex, int endIndex) {
		List<T> list = new ArrayList<>();

		if (startIndex > endIndex || startIndex < 0 || endIndex > arr.length)
			throw new ArrayIndexOutOfBoundsException();

		for (int i = startIndex; i < endIndex; i++)
			list.add(arr[i]);

		return list;
	}

	/**
	 * 
	 * @param souceList
	 * @param removeList
	 * @author wcy 2017年6月19日
	 */
	public static <T> void removeElementByList(List<T> sourceList, List<T> removeList) {
		for (T card : removeList) {
			for (int i = sourceList.size() - 1; i >= 0; i--) {
				if (sourceList.get(i).equals(card)) {
					sourceList.remove(i);
					break;
				}
			}
		}
	}

	/**
	 * 从数组指定位置查找是否存在一个值
	 * 
	 * @param arr
	 * @param startIndex
	 * @param endIndex
	 * @param value
	 * @return
	 * @author wcy 2017年7月21日
	 */
	public static <T> boolean contains(List<T> arr, int startIndex, int endIndex, Object o) {
		return indexOf(arr, startIndex, endIndex, o) >= 0;
	}

	/**
	 * 返回列表中某值的数量
	 * 
	 * @param arr
	 * @param startIndex
	 * @param endIndex
	 * @param o
	 * @return
	 * @author wcy 2017年7月21日
	 */
	public static <T> int containsCount(List<T> arr, Object o) {
		return containsCount(arr, 0, arr.size(), o);
	}

	/**
	 * 返回列表中指定位置某值的数量
	 * 
	 * @param arr
	 * @param startIndex
	 * @param endIndex
	 * @param o
	 * @return
	 * @author wcy 2017年7月21日
	 */
	public static <T> int containsCount(List<T> arr, int startIndex, int endIndex, Object o) {
		int count = 0;
		for (int i = startIndex; i < endIndex;) {
			int index = indexOf(arr, i, endIndex, o);

			if (index == -1)
				break;

			i = index + 1;
			count++;

		}
		return count;
	}

	/**
	 * 从数组指定位置返回一个值的索引,不存在返回-1
	 * 
	 * @param arr
	 * @param startIndex
	 * @param endIndex
	 * @param o
	 * @return
	 * @author wcy 2017年7月21日
	 */
	public static <T> int indexOf(List<T> arr, int startIndex, int endIndex, Object o) {
		if (startIndex < 0 || endIndex > arr.size() || startIndex > endIndex)
			throw new RuntimeException("index out of bounds");

		if (o == null) {
			for (int i = startIndex; i < endIndex; i++)
				if (arr.get(i) == null)
					return i;
		} else {
			for (int i = startIndex; i < endIndex; i++)
				if (o.equals(arr.get(i)))
					return i;
		}

		return -1;
	}

	/**
	 * 移除所有的指定的索引
	 * 
	 * @param sources
	 * @param indexList
	 * @author wcy 2017年7月21日
	 */
	public static <T> void removeAllIndex(List<T> sources, List<Integer> indexList) {
		Collections.sort(indexList);

		int temp = -1;
		int i = sources.size() - 1;
		for (int j = indexList.size() - 1; j >= 0;) {
			int index = indexList.get(j);
			if (index == temp) {
				j--;
				continue;
			}
			for (; i >= 0; i--) {
				if (index > i)
					continue;
				if (index == i) {
					temp = index;
					sources.remove(i);
					j--;
					break;
				}
			}
		}
	}

}
