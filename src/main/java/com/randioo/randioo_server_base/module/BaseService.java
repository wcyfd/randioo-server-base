package com.randioo.randioo_server_base.module;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import com.randioo.randioo_server_base.navigation.Navigation;
import com.randioo.randioo_server_base.net.IActionSupport;
import com.randioo.randioo_server_base.net.PTAnnotation;
import com.randioo.randioo_server_base.net.SpringContext;
import com.randioo.randioo_server_base.utils.PackageUtil;

public class BaseService implements BaseServiceInterface {

	@Override
	public void init() {
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
				PTAnnotation pt = clazz.getAnnotation(PTAnnotation.class);
				if (pt != null) {
					try {
						@SuppressWarnings("unchecked")
						Map<String, IActionSupport> navigate = (Map<String, IActionSupport>) field.get(null);
						String key = this.getRequestKeyName(pt.value());
						IActionSupport action = (IActionSupport) SpringContext
								.getBean(getActionInstanceSpringContextName(clazz));
						navigate.put(key, action);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			field.setAccessible(false);
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}

	}

	private String getActionInstanceSpringContextName(Class<?> action) {
		StringBuilder sb = new StringBuilder();
		String name = action.getSimpleName();
		String lowFirstStr = name.substring(0, 1).toLowerCase();
		sb.append(lowFirstStr).append(name.substring(1, name.length()));
		return sb.toString();
	}

	private String getRequestKeyName(Class<?> clazz) {
		StringBuilder sb = new StringBuilder();
		String name = clazz.getSimpleName();
		String lowFirstStr = name.substring(0, 1).toLowerCase();
		sb.append(lowFirstStr).append(name.substring(1, name.length()));
		return sb.toString();

	}
}
