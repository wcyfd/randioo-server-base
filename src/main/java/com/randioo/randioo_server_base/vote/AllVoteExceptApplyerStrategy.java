package com.randioo.randioo_server_base.vote;

import java.util.Map;
import java.util.Set;

import com.randioo.randioo_server_base.template.Function;
import com.randioo.randioo_server_base.vote.VoteBox.VoteResult;

public abstract class AllVoteExceptApplyerStrategy implements VoteStrategy {

	/**
	 * 等待投票
	 * 
	 * @param joiner
	 * @return
	 * @author wcy 2017年7月17日
	 */
	public abstract VoteResult waitVote(String joiner);

	@Override
	public boolean filterVoter(String voter, String applyer) {
		return !voter.equals(applyer);
	}

	@Override
	public VoteResult vote(String voter, boolean vote, Map<String, Boolean> voteMap, Set<String> joiners,
			Function generateFunction, String applyer) {
		voteMap.put(voter, vote);
		if (voteMap.size() == joiners.size() - 1) {
			generateFunction.apply();
			return checkResult(voteMap);
		} else {
			WAIT_VOTE: {
				for (String joiner : joiners) {
					// 申请人就跳过
					if (joiner.equals(applyer))
						continue;

					// 投票中没有此人就检查连接,没断就返回
					if (!voteMap.containsKey(joiner)) {
						VoteResult voteResult = this.waitVote(joiner);
						if (voteResult != VoteResult.WAIT) {
							voteMap.put(joiner, voteResult == VoteResult.PASS);
						} else {
							break WAIT_VOTE;
						}
					}
				}
				generateFunction.apply();
				return checkResult(voteMap);
			}
		}
		return VoteResult.WAIT;
	}

	private VoteResult checkResult(Map<String, Boolean> voteMap) {
		boolean checkResult = true;
		for (boolean result : voteMap.values())
			checkResult &= result;

		return checkResult ? VoteResult.PASS : VoteResult.REJECT;
	}

}
