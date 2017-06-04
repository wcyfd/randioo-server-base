package com.randioo.randioo_server_base.module.role;

import com.randioo.randioo_server_base.entity.RoleInterface;
import com.randioo.randioo_server_base.service.BaseServiceInterface;
import com.randioo.randioo_server_base.template.Ref;

public interface RoleModelService extends BaseServiceInterface {
	public void setRoleHandler(RoleHandler roleHandler);

	public boolean rename(RoleInterface roleInterface, String name, Ref<Integer> errorCode);

	int protectedGetTotalValue(int originValue, int addValue);

}
