package com.randioo.randioo_server_base.module.role;

import org.springframework.stereotype.Service;

import com.randioo.randioo_server_base.service.BaseService;
import com.randioo.randioo_server_base.utils.GameUtils;

@Service("roleModelService")
public class RoleModelServiceImpl extends BaseService implements RoleModelService {

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
		return GameUtils.protectedGetTotalValue(originValue, addValue);
	}

}
