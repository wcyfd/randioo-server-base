package com.randioo.randioo_server_base.module.friend;

import java.util.List;
import java.util.concurrent.locks.Lock;

import com.randioo.randioo_server_base.module.BaseServiceInterface;
import com.randioo.randioo_server_base.module.friend.FriendModelConstant.NewRelation;
import com.randioo.randioo_server_base.utils.template.Ref;

public interface FriendModelService extends BaseServiceInterface{
	public void setFriendHandler(FriendHandler friendHandler);

	public boolean applyFriend(Friendable my, int targetKey,Ref<NewRelation> info);

	public boolean agreeApply(Friendable my, int key);
	
	public List<Integer> agreeAllApply(Friendable my);

	public void rejectAddFriend(Friendable friendable, int key);

	public void deleteFriend(Friendable friendable, int key);

	boolean checkFriendsEachOther(int myKey, int targetKey);

	int getAnotherOne(Relation relation, int myKey);
	
	public Relation getRelation(int myKey,int targetKey);

	Lock getRelationLock(RelationKey relationKey);

	List<Integer> getAllFriends(Friendable role);
}
