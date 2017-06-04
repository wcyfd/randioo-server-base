package com.randioo.randioo_server_base.module.friend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;

import org.springframework.stereotype.Service;

import com.randioo.randioo_server_base.lock.CacheLockUtil;
import com.randioo.randioo_server_base.module.friend.FriendModelConstant.NewRelation;
import com.randioo.randioo_server_base.service.BaseService;
import com.randioo.randioo_server_base.template.Ref;

@Service("friendModelService")
public class FriendModelServiceImpl extends BaseService implements FriendModelService {

	private FriendHandler handler;

	@Override
	public void setFriendHandler(FriendHandler friendHandler) {
		this.handler = friendHandler;
	}

	@Override
	public boolean applyFriend(Friendable my, int targetKey, Ref<NewRelation> info) {
		int myKey = my.getFriendKey();

		RelationKey relationKey = new RelationKey().setRoleId1(myKey).setRoleId2(targetKey).build();

		Map<Integer, Byte> statusMap = new HashMap<>(2);
		// 申请人赋值同意
		statusMap.put(myKey, FriendModelConstant.AGREE);
		// 接受方赋值验证
		statusMap.put(targetKey, FriendModelConstant.WAIT);

		Lock lock = this.getRelationLock(relationKey);
		lock.lock();
		try {
			info.set(NewRelation.FALSE);
			Relation relation = FriendCache.getRelationMap().get(relationKey.getKey());
			// 检查是否已经是好友
			if (checkFriends(relation)) {
				return false;
			}
			if (relation == null) {
				relation = handler.createRelation(relationKey);
				relation.setRelationKey(relationKey);
				FriendCache.getRelationMap().put(relationKey.getKey(), relation);
				FriendCache.addRelationKeyMap(relationKey);
				info.set(NewRelation.TRUE);
			}

			relation.setRoleId1Status(statusMap.get(relationKey.getRoleId1()));
			relation.setRoleId2Status(statusMap.get(relationKey.getRoleId2()));

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}

		return false;
	}

	/**
	 * 检查是否已经是好友
	 * 
	 * @param relation
	 * @return
	 * @author wcy 2017年2月18日
	 */
	private boolean checkFriends(Relation relation) {
		if (relation == null) {
			return false;
		}
		return relation.getRoleId1Status() == FriendModelConstant.AGREE
				&& relation.getRoleId2Status() == FriendModelConstant.AGREE;
	}

	/**
	 * 获得关系锁
	 * 
	 * @param relationKey
	 * @return
	 * @author wcy 2017年2月18日
	 */
	@Override
	public Lock getRelationLock(RelationKey relationKey) {
		String lockName = relationKey.getKey();
		return CacheLockUtil.getLock(Relation.class, lockName);
	}

	@Override
	public boolean agreeApply(Friendable my, int key) {
		int myKey = my.getFriendKey();
		int targetKey = key;
		RelationKey relationKey = new RelationKey().setRoleId1(myKey).setRoleId2(targetKey).build();
		Lock lock = getRelationLock(relationKey);
		lock.lock();
		try {
			Relation relation = FriendCache.getRelationMap().get(relationKey.getKey());
			// 设置本人为同意状态
			this.setRelationStatus(relation, myKey, FriendModelConstant.AGREE);
			// 如果对面是等待状态说明请求覆盖了,默认就同意加为好友
			if (relation.getRoleId1Status() == FriendModelConstant.WAIT
					|| relation.getRoleId2Status() == FriendModelConstant.WAIT) {
				this.setRelationStatus(relation, targetKey, FriendModelConstant.AGREE);
			}
			return checkFriends(relation);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return false;
	}

	private void setRelationStatus(Relation relation, int myKey, byte status) {
		int roleId1 = relation.getRoleId1();
		int roleId2 = relation.getRoleId2();
		if (roleId1 == myKey) {
			relation.setRoleId1Status(status);
		}
		if (roleId2 == myKey) {
			relation.setRoleId2Status(status);
		}
	}

	@Override
	public int getAnotherOne(Relation relation, int myKey) {
		return relation.getRoleId1() != myKey ? relation.getRoleId1() : relation.getRoleId2();
	}

	@Override
	public List<Integer> agreeAllApply(Friendable my) {
		int myKey = my.getFriendKey();
		// 从数据中获得所有申请,这里面的人不能保证全部都处理完,所以再循环的时候要加锁
		List<String> keySet = new ArrayList<>(FriendCache.getRelationKeyByRoleId(myKey));
		List<Integer> agreeKeyList = new ArrayList<>(keySet.size());
		for (String key : keySet) {
			Relation relation = FriendCache.getRelationMap().get(key);
			RelationKey relationKey = new RelationKey().setRoleId1(relation.getRoleId1())
					.setRoleId2(relation.getRoleId2()).build();
			// 如果已经是好友则继续
			Lock lock = getRelationLock(relationKey);
			lock.lock();
			try {
				if (checkFriends(relation)) {
					continue;
				}
				setRelationStatus(relation, myKey, FriendModelConstant.AGREE);
				int targetKey = getAnotherOne(relation, myKey);
				// 如果对面是等待状态说明请求覆盖了,默认就同意加为好友
				if (relation.getRoleId1Status() == FriendModelConstant.WAIT
						|| relation.getRoleId2Status() == FriendModelConstant.WAIT) {
					this.setRelationStatus(relation, targetKey, FriendModelConstant.AGREE);
				}
				handler.noticeBecomeFriends(relation, targetKey);
				agreeKeyList.add(targetKey);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}

		}

		return agreeKeyList;
	}

	@Override
	public void rejectAddFriend(Friendable friendable, int key) {
		int myKey = friendable.getFriendKey();
		int targetKey = key;
		RelationKey relationKey = new RelationKey().setRoleId1(myKey).setRoleId2(targetKey).build();
		Lock lock = getRelationLock(relationKey);
		lock.lock();
		try {
			Relation relation = FriendCache.getRelationMap().get(relationKey.getKey());
			setRelationStatus(relation, myKey, FriendModelConstant.REJECT);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}

	}

	@Override
	public void deleteFriend(Friendable friendable, int key) {
		int myKey = friendable.getFriendKey();
		int targetKey = key;
		RelationKey relationKey = new RelationKey().setRoleId1(myKey).setRoleId2(targetKey).build();
		Lock lock = getRelationLock(relationKey);
		lock.lock();
		try {
			Relation relation = FriendCache.getRelationMap().get(relationKey.getKey());
			setRelationStatus(relation, myKey, FriendModelConstant.DELETE);
			setRelationStatus(relation, targetKey, FriendModelConstant.DELETE);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public boolean checkFriendsEachOther(int myKey, int targetKey) {
		RelationKey relationKey = new RelationKey().setRoleId1(myKey).setRoleId2(targetKey).build();
		Lock lock = getRelationLock(relationKey);
		lock.lock();
		try {
			Relation relation = FriendCache.getRelationMap().get(relationKey.getKey());
			return relation != null && checkFriends(relation);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return false;
	}

	@Override
	public Relation getRelation(int myKey, int targetKey) {
		String key = new RelationKey().setRoleId1(myKey).setRoleId2(targetKey).build().getKey();
		Relation relation = FriendCache.getRelationMap().get(key);
		return relation;
	}

	@Override
	public List<Integer> getAllFriends(Friendable role) {
		List<Integer> friendIdList = new ArrayList<>();
		Set<String> friendString = FriendCache.getRelationKeyByRoleId(role.getFriendKey());
		for (String relationKey : friendString) {
			Relation relation = FriendCache.getRelationMap().get(relationKey);
			if (relation.getRoleId1() != role.getFriendKey() && relation.getRoleId2() != role.getFriendKey()) {
				continue;
			}
			boolean isFriend = this.checkFriendsEachOther(relation.getRoleId1(), relation.getRoleId2());
			if (!isFriend) {
				continue;
			}
			int friendId = this.getAnotherOne(relation, role.getFriendKey());

			friendIdList.add(friendId);
		}

		return friendIdList;
	}

}
