package com.randioo.randioo_server_base.module.login;

import java.sql.Connection;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;
import com.randioo.randioo_server_base.entity.Ref;
import com.randioo.randioo_server_base.module.BaseService;
import com.randioo.randioo_server_base.net.CacheLockUtil;

public class LoginModelServiceImpl extends BaseService implements LoginModelService {
	private LoginHandler<?> loginHandler;

	public void setLoginHandler(LoginHandler<?> loginHandler) {
		this.loginHandler = loginHandler;
	}

	@Override
	public GeneratedMessage login(GeneratedMessage msg) {
		String account = loginHandler.getLoginAccount(msg);

		ReentrantLock reentrantLock = CacheLockUtil.getLock(String.class, account);
		reentrantLock.lock();
		GeneratedMessage message = null;
		try {
			message = loginHandler.isNewAccount(account);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			reentrantLock.unlock();
		}
		return message;
	}

	@Override
	public GeneratedMessage creatRole(GeneratedMessage msg) {
		ReentrantLock reentrantLock = CacheLockUtil.getLock(String.class, loginHandler.getCreateRoleAccount(msg));
		reentrantLock.lock();
		try {
			GeneratedMessage checkCreateRoleAccountResult = loginHandler.checkCreateRoleAccount(msg);
			if (checkCreateRoleAccountResult != null) {
				return checkCreateRoleAccountResult;
			}

			Connection conn = loginHandler.getConnection();
			try { // mysql事务
				if (conn != null) {
					conn.setAutoCommit(false);
				}

				GeneratedMessage createRoleResultProtobufMessage = loginHandler.createRole(conn, msg);

				if (conn != null) {
					conn.commit(); // 提交JDBC事务
					conn.setAutoCommit(true); // 恢复JDBC事务的默认提交方式
				}

				return createRoleResultProtobufMessage;
			} catch (Exception e1) {
				e1.printStackTrace();
				try {
					if (conn != null)
						conn.rollback();// 回滚JDBC事务
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				e1.printStackTrace();
				return null;
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			reentrantLock.unlock();
		}
	}

	@Override
	public GeneratedMessage getRoleData(GeneratedMessage requestMessage, IoSession ioSession) {		
		ReentrantLock reentrantLock = CacheLockUtil.getLock(String.class, loginHandler.getRoleDataAccount(requestMessage));
		reentrantLock.lock();

		try {
			Ref ref = new Ref();
			GeneratedMessage resultMessage = loginHandler.getRoleObjectFromCollectionsByGeneratedMessage(ref,requestMessage);
			if (resultMessage != null) {
				return resultMessage;
			}

			IoSession oldSession = loginHandler.getSessionByRef(ref);
			if (oldSession != null) { // 该账号已登录
				if (oldSession.isConnected()) {
					GeneratedMessage connectingErrorMessage = loginHandler.connectingError();
					if (connectingErrorMessage != null) {
						return connectingErrorMessage;
					}
				}
				oldSession.setAttribute(loginHandler.getIoSessionTag(), null);
				oldSession.close(false);
			}

			// session绑定ID
			ioSession.setAttribute(loginHandler.getIoSessionTag(), loginHandler.getIoSessionAttributeValue(ref));
			// session放入缓存
			loginHandler.recordSession(ref, ioSession);

			return loginHandler.getRoleData(ref);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			reentrantLock.unlock();
		}
	}

}
