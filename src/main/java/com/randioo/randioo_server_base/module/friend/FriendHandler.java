package com.randioo.randioo_server_base.module.friend;

public interface FriendHandler {
	Relation createRelation(RelationKey relationKey);

	void noticeBecomeFriends(Relation relation, int key);

}
