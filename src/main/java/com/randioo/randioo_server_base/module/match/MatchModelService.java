package com.randioo.randioo_server_base.module.match;

import com.randioo.randioo_server_base.service.BaseServiceInterface;

public interface MatchModelService extends BaseServiceInterface {
	public void setMatchHandler(MatchHandler matchHandler);

	public void matchRole(MatchRule matchRule);

	public void cancelMatch(String ruleId);
}
