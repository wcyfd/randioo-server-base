package com.randioo.randioo_server_base.module.login;

import java.sql.Connection;
import java.sql.SQLException;

import com.randioo.randioo_server_base.entity.Ref;
import com.randioo.randioo_server_base.entity.RoleInterface;

public interface LoginHandler {
	/**
	 * 获得登录帐号
	 * 
	 * @param loginMessage
	 * @return
	 * @author wcy 2016年11月30日
	 */
	String getLoginAccount(Object loginMessage);

	/**
	 * 检查该帐号是否可以进行登录
	 * 
	 * @param account
	 * @param message
	 * @return
	 * @author wcy 2016年12月13日
	 */
	boolean checkLoginAccountCanLogin(String account, Ref<Object> loginMessage);

	/**
	 * 是否是新的帐号
	 * 
	 * @param account
	 * @return
	 * @author wcy 2016年11月30日
	 */
	Object isNewAccount(String account);

	/**
	 * 获得创建账号的帐号
	 * 
	 * @param createRoleMessage
	 * @return
	 * @author wcy 2016年11月30日
	 */
	String getCreateRoleAccount(Object createRoleMessage);

	/**
	 * 检查创建的帐号是否合法
	 * 
	 * @param account
	 * @return
	 * @author wcy 2016年11月30日
	 */
	boolean checkCreateRoleAccount(Object createRoleMessage, Ref<Object> checkCreateRoleAccountMessage);

	/**
	 * 获得数据库连接
	 * 
	 * @return
	 * @author wcy 2016年11月30日
	 * @throws SQLException
	 */
	Connection getConnection() throws SQLException;

	/**
	 * 创建帐号
	 * 
	 * @param conn
	 * @param createRoleMessage
	 * @return
	 * @author wcy 2016年11月30日
	 */
	Object createRole(Connection conn, Object createRoleMessage);

	/**
	 * 获得玩家信息
	 */
	Object getRoleData(Ref<RoleInterface> ref);

	/**
	 * 需要获得信息的玩家账号
	 * 
	 * @return
	 * @author wcy 2016年11月30日
	 */
	String getRoleDataAccount(Object createRoleMessage);

	/**
	 * 根据帐号获得玩家对象
	 * 
	 * @param ref
	 * @return
	 * @author wcy 2016年11月30日
	 * @param requestMessage
	 */
	boolean getRoleObject(Ref<RoleInterface> ref, Object requestMessage,
			Ref<Object> errorMessage);

	/**
	 * 用户已经在某地连接的错误,如果不处理则返回null
	 * 
	 * @return
	 * @author wcy 2016年11月30日
	 */
	boolean connectingError(Ref<Object> connectingErrorMessage);

}
