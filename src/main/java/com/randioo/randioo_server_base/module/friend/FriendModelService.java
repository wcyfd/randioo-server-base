package com.randioo.randioo_server_base.module.friend;

import com.randioo.randioo_server_base.entity.RoleInterface;
import com.randioo.randioo_server_base.module.BaseServiceInterface;

public interface FriendModelService extends BaseServiceInterface{
	public void setFriendHandler(FriendHandler friendHandler);

	public void applyFriend(RoleInterface roleInterface, Friendable friendable, String targetAccount);

	public void agreeFriend(RoleInterface roleInterface, Friendable friendable, String account);

	public void rejectAddFriend(RoleInterface roleInterface, Friendable friendable, String account);

	public void deleteFriend(Friendable friendable, String account);
}
