package com.randioo.randioo_server_base.module.market;

import java.util.List;

import com.randioo.randioo_server_base.module.BaseServiceInterface;

public interface MarketModelService extends BaseServiceInterface {
	public List<MarketItem> gamble(MarketInterface marketInterface, MarketTypeInfo info);
}
