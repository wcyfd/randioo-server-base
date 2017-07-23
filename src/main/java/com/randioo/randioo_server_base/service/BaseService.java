package com.randioo.randioo_server_base.service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.randioo.randioo_server_base.annotation.PTAnnotation;
import com.randioo.randioo_server_base.annotation.PTStringAnnotation;
import com.randioo.randioo_server_base.entity.RoleInterface;
import com.randioo.randioo_server_base.log.HttpLogUtils;
import com.randioo.randioo_server_base.navigation.Navigation;
import com.randioo.randioo_server_base.template.IActionSupport;
import com.randioo.randioo_server_base.utils.PackageUtil;
import com.randioo.randioo_server_base.utils.SpringContext;
import com.randioo.randioo_server_base.utils.StringUtils;

public class BaseService implements BaseServiceInterface {

	/**
	 * 获得这个类的第一个接口
	 */
	private Logger logger = LoggerFactory.getLogger(getSysName());

	private String getSysName() {
		return this.getClass().getInterfaces()[0].getSimpleName();
	}

	@Override
	public void init() {

	}

	@Override
	public void initService() {

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

	protected void loggerdebug(RoleInterface role, Object msg) {
		logger.debug(HttpLogUtils.role(role, msg));
	}

	protected void loggerinfo(RoleInterface role, Object msg) {
		logger.info(HttpLogUtils.role(role, msg));
	}

	protected void loggererror(RoleInterface role, String msg) {
		logger.error(HttpLogUtils.role(role, msg));
	}

	protected void loggererror(RoleInterface role, String msg, Throwable e) {
		logger.error(HttpLogUtils.role(role, msg), e);
	}

	protected void loggerwarn(RoleInterface role, String msg) {
		logger.warn(HttpLogUtils.role(role, msg));
	}

	protected void loggerwarn(RoleInterface role, String msg, Throwable e) {
		logger.warn(HttpLogUtils.role(role, msg), e);
	}

	protected void loggerdebug(String msg) {
		logger.debug(HttpLogUtils.sys(msg));
	}

	protected void loggerinfo(String msg) {
		logger.info(HttpLogUtils.sys(msg));
	}

	protected void loggererror(String msg) {
		logger.error(HttpLogUtils.sys(msg));
	}

	protected void loggererror(String msg, Throwable e) {
		logger.error(HttpLogUtils.sys(msg), e);
	}

	protected void loggerwarn(String msg) {
		logger.warn(HttpLogUtils.sys(msg));
	}

	protected void loggerwarn(String msg, Throwable e) {
		logger.warn(HttpLogUtils.sys(msg), e);
	}

}
