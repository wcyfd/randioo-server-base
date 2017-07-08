package com.randioo.randioo_server_base.module.role;

import com.randioo.randioo_server_base.service.BaseServiceInterface;

public interface RoleModelService extends BaseServiceInterface {

	int protectedGetTotalValue(int originValue, int addValue);

}
