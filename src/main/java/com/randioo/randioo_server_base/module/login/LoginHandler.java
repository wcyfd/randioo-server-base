package com.randioo.randioo_server_base.module.login;

import java.sql.Connection;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.randioo_server_base.entity.Ref;

public interface LoginHandler<T> {
	/**
	 * 获得登录帐号
	 * 
	 * @param loginMessage
	 * @return
	 * @author wcy 2016年11月30日
	 */
	String getLoginAccount(GeneratedMessage loginMessage);

	/**
	 * 是否是新的帐号
	 * 
	 * @param account
	 * @return
	 * @author wcy 2016年11月30日
	 */
	GeneratedMessage isNewAccount(String account);

	/**
	 * 获得创建账号的帐号
	 * 
	 * @param createRoleMessage
	 * @return
	 * @author wcy 2016年11月30日
	 */
	String getCreateRoleAccount(GeneratedMessage createRoleMessage);

	/**
	 * 检查创建的帐号是否合法
	 * 
	 * @param account
	 * @return
	 * @author wcy 2016年11月30日
	 */
	GeneratedMessage checkCreateRoleAccount(GeneratedMessage createRoleMessage);

	/**
	 * 获得数据库连接
	 * 
	 * @return
	 * @author wcy 2016年11月30日
	 */
	Connection getConnection();

	/**
	 * 创建帐号
	 * 
	 * @param conn
	 * @param createRoleMessage
	 * @return
	 * @author wcy 2016年11月30日
	 */
	GeneratedMessage createRole(Connection conn, GeneratedMessage createRoleMessage);

	/**
	 * 获得玩家信息
	 */
	GeneratedMessage getRoleData(Ref<T> ref);

	/**
	 * 需要获得信息的玩家账号
	 * 
	 * @return
	 * @author wcy 2016年11月30日
	 */
	String getRoleDataAccount(GeneratedMessage createRoleMessage);

	/**
	 * 根据帐号获得玩家对象
	 * 
	 * @param ref
	 * @return
	 * @author wcy 2016年11月30日
	 * @param requestMessage
	 */
	GeneratedMessage getRoleObjectFromCollectionsByGeneratedMessage(Ref<T> ref, GeneratedMessage requestMessage);

	/**
	 * session的标记标签
	 * 
	 * @return
	 * @author wcy 2016年11月30日
	 */
	String getIoSessionTag();

	/**
	 * 获得session的值
	 * 
	 * @param ref
	 * @return
	 * @author wcy 2016年11月30日
	 */
	Object getIoSessionAttributeValue(Ref<T> ref);

	/**
	 * 通过地址内的对象获得Session
	 * 
	 * @param ref
	 * @return
	 * @author wcy 2016年11月30日
	 */
	IoSession getSessionByRef(Ref<T> ref);

	/**
	 * 用户已经在某地连接的错误,如果不处理则返回null
	 * 
	 * @return
	 * @author wcy 2016年11月30日
	 */
	GeneratedMessage connectingError();

	/**
	 * 记录session
	 * 
	 * @param ref
	 * @param session
	 * @author wcy 2016年11月30日
	 */
	void recordSession(Ref<T> ref, IoSession session);
}
