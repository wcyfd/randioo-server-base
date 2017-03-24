package com.randioo.randioo_server_base.module.role;

import org.springframework.stereotype.Service;

import com.randioo.randioo_server_base.cache.RoleCache;
import com.randioo.randioo_server_base.entity.RoleInterface;
import com.randioo.randioo_server_base.module.BaseService;
import com.randioo.randioo_server_base.utils.template.Ref;

@Service("roleModelService")
public class RoleModelServiceImpl extends BaseService implements RoleModelService {

	private RoleHandler roleHandler;

	@Override
	public void setRoleHandler(RoleHandler roleHandler) {
		this.roleHandler = roleHandler;
	}

	@Override
	public boolean rename(RoleInterface roleInterface, String name, Ref<Integer> errorCode) {
		boolean result = roleHandler.checkNewNameIllege(name, errorCode);
		if (result) {
			String oldName = roleInterface.getName();
			roleInterface.setName(name);
			RoleCache.getNameSet().remove(oldName);
			RoleCache.getNameSet().put(name, name);
		}
		return result;
	}

	/**
	 * 保护获得增加后的值
	 * 
	 * @param originValue
	 * @param addValue
	 * @return
	 * @author wcy 2017年2月10日
	 */
	@Override
	public int protectedGetTotalValue(int originValue, int addValue) {
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
