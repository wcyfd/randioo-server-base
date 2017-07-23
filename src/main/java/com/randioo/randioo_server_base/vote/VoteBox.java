package com.randioo.randioo_server_base.vote;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.randioo.randioo_server_base.template.Function;

public class VoteBox {

	public enum VoteResult {
		WAIT, PASS, REJECT
	}

	private Map<String, Boolean> voteMap;
	private Set<String> joinVoteSet;
	private VoteStrategy voteStrategy;
	private int voteTimes;
	private String applyer;
	private Function generateFunction;
	private VoteResult voteResult = null;

	public VoteBox() {
		this.voteMap = new HashMap<>();
		this.joinVoteSet = new HashSet<>();
		this.voteResult = VoteResult.WAIT;
		this.generateFunction = new Function() {

			@Override
			public Object apply(Object... params) {
				generateVoteId();
				return null;
			}
		};
	}

	public void setStrategy(VoteStrategy strategy) {
		this.voteStrategy = strategy;
	}

	public void reset() {
		this.voteMap.clear();
		this.joinVoteSet.clear();
		this.voteResult = VoteResult.WAIT;
		this.applyer = null;
	}

	/**
	 * 申请投票
	 * 
	 * @param t
	 * @return 返回投票标号
	 * @author wcy 2017年7月17日
	 */
	public int applyVote(String applyer) {
		if (this.applyer == null)
			this.applyer = applyer;

		return voteTimes;
	}

	public String getApplyer() {
		return applyer;
	}

	private void generateVoteId() {
		voteTimes++;
	}

	public int getVoteId() {
		return voteTimes;
	}

	public void vote(String joiner, boolean vote, int voteId) {
		if (voteId != voteTimes)
			return;
		this.checkAllowVote();

		if (!voteStrategy.filterVoter(joiner, applyer))
			return;

		VoteResult voteResult = voteStrategy.vote(joiner, vote, voteMap, joinVoteSet, generateFunction, applyer);
		this.voteResult = voteResult;
	}

	public Map<String, Boolean> getVoteMap() {
		return voteMap;
	}

	public VoteResult getResult() {
		return voteResult;
	}

	/**
	 * 检查是否允许投票
	 * 
	 * @author wcy 2017年7月17日
	 */
	private void checkAllowVote() {
		if (joinVoteSet.size() == 0)
			throw new RuntimeException("not set total count");
		if (voteStrategy == null)
			throw new RuntimeException("not set strategy");
	}

	/**
	 * 设置加入投票的人
	 * 
	 * @return
	 * @author wcy 2017年7月17日
	 */
	public Set<String> getJoinVoteSet() {
		return joinVoteSet;
	}

	public static void main(String[] args) {
		VoteBox voteBox = new VoteBox();
		voteBox.setStrategy(new AllVoteExceptApplyerStrategy() {

			@Override
			public VoteResult waitVote(String joiner) {
				return VoteResult.WAIT;
			}
		});

		voteBox.getJoinVoteSet().addAll(Arrays.asList("1", "2", "3", "4"));
		int voteId = voteBox.applyVote("1");
		voteBox.vote("2", false, voteId);
		voteBox.vote("3", false, voteId);
		System.out.println(voteBox.getResult());
	}

}
