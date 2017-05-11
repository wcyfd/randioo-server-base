package com.randioo.randioo_server_base.module;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import com.randioo.randioo_server_base.annotation.PTAnnotation;
import com.randioo.randioo_server_base.annotation.PTStringAnnotation;
import com.randioo.randioo_server_base.navigation.Navigation;
import com.randioo.randioo_server_base.net.IActionSupport;
import com.randioo.randioo_server_base.net.SpringContext;
import com.randioo.randioo_server_base.utils.PackageUtil;
import com.randioo.randioo_server_base.utils.StringUtils;

public class BaseService implements BaseServiceInterface {

	@Override
	public void init() {

	}

	@Override
	public void initService() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initNavigation() {
		String packageName = PackageUtil.getParent(getClass().getPackage().getName()) + ".action";
		List<Class<?>> classes = PackageUtil.getClasses(packageName);
		try {
			Field field = Navigation.class.getDeclaredField("navigate");
			field.setAccessible(true);
			for (Class<?> clazz : classes) {
				String key = this.getKeyName(clazz);
				if (key != null) {
					try {
						@SuppressWarnings("unchecked")
						Map<String, IActionSupport> navigate = (Map<String, IActionSupport>) field.get(null);
						String beanName = StringUtils.firstStrToLowerCase(clazz.getSimpleName());
						IActionSupport action = (IActionSupport) SpringContext.getBean(beanName);
						navigate.put(key, action);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			field.setAccessible(false);
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获得action的key
	 * 
	 * @param clazz
	 * @return
	 * @author wcy 2017年2月13日
	 */
	private String getKeyName(Class<?> clazz) {
		String key = null;
		PTAnnotation pt = clazz.getAnnotation(PTAnnotation.class);
		PTStringAnnotation ptString = clazz.getAnnotation(PTStringAnnotation.class);
		if (pt != null) {
			key = pt.value().getSimpleName();
		} else if (ptString != null) {
			key = ptString.value();
		}
		return key;
	}

}
