package com.randioo.randioo_server_base.module.friend;

import java.util.Set;

import com.randioo.randioo_server_base.entity.RoleInterface;
import com.randioo.randioo_server_base.module.BaseService;

public class FriendModelServiceImpl extends BaseService implements FriendModelService {

	private FriendHandler handler;

	@Override
	public void setFriendHandler(FriendHandler friendHandler) {
		this.handler = friendHandler;
	}

	@Override
	public void applyFriend(RoleInterface roleInterface, Friendable friendable, String targetAccount) {
		// 检查他是否是自己的好友
		if (friendable.getFriendList().contains(targetAccount)) {
			handler.repeatedAddFriend(roleInterface, targetAccount);
			return;
		}

		Friendable targetFriendable = handler.getFriendableByAccount(targetAccount);
		// 检查自己是否是他的好友
		if (targetFriendable.getFriendList().contains(roleInterface.getAccount())) {
			handler.repeatedAddFriend(roleInterface, targetAccount);
			return;
		}

		// 添加到申请队列和等待队列
		targetFriendable.getApplyAccountList().add(roleInterface.getAccount());

		// 发送通知
		handler.sendApply(roleInterface, targetAccount);

	}

	@Override
	public void agreeFriend(RoleInterface roleInterface, Friendable friendable, String account) {
		Set<String> applyAccountSet = friendable.getApplyAccountList();

		Friendable targetFriendable = handler.getFriendableByAccount(account);
		RoleInterface targetRoleInterface = handler.getRoleInterfaceByAccount(account);
		// 添加至好友列表（被请求方）
		friendable.getFriendList().add(account);
		applyAccountSet.remove(account);// 申请标记改变为同意

		// 添加至好友列表（请求方）
		targetFriendable.getFriendList().add(account);

		// 通知
		handler.agreeAddFriend(roleInterface, targetRoleInterface);

	}

	@Override
	public void rejectAddFriend(RoleInterface roleInterface, Friendable friendable, String account) {
		// 获取自己的申请好友列表
		Set<String> applyAccountSet = friendable.getApplyAccountList();
		// 申请状态改变为拒绝
		applyAccountSet.remove(account);

	}

	@Override
	public void deleteFriend(Friendable friendable, String account) {
		friendable.getFriendList().remove(account);
	}

}
