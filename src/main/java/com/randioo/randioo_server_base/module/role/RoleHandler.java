package com.randioo.randioo_server_base.module.role;

import com.randioo.randioo_server_base.utils.template.Ref;

public interface RoleHandler {
	public boolean checkNewNameIllege(String name, Ref<Integer> errorCode);
	
}
