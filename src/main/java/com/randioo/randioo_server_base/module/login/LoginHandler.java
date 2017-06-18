package com.randioo.randioo_server_base.module.login;

import com.randioo.randioo_server_base.entity.RoleInterface;
import com.randioo.randioo_server_base.utils.template.Ref;

public interface LoginHandler {

	/**
	 * 检查创建的帐号是否合法
	 * 
	 * @param account
	 * @return
	 * @author wcy 2016年11月30日
	 */
	boolean createRoleCheckAccount(LoginCreateInfo info, Ref<Integer> errorCode);

	/**
	 * 创建帐号
	 * 
	 * @param conn
	 * @param createRoleMessage
	 * @return
	 * @author wcy 2016年11月30日
	 */
	RoleInterface createRole(LoginCreateInfo loginCreateInfo);

	/**
	 * 根据帐号获得玩家对象
	 * 
	 * @return
	 * @author wcy 2016年11月30日
	 * @param requestMessage
	 */
	RoleInterface getRoleInterface(LoginInfo loginInfo);

	/**
	 * 用户已经在某地连接的错误,如果不处理则返回null
	 * 
	 * @return
	 * @author wcy 2016年11月30日
	 */
	boolean canSynLogin();

	RoleInterface getRoleInterfaceFromDBById(int roleId);

	RoleInterface getRoleInterfaceFromDBByAccount(String account);

	void loginRoleModuleDataInit(RoleInterface roleInterface);

}
