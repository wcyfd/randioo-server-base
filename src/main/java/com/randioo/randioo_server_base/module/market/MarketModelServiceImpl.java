package com.randioo.randioo_server_base.module.market;

import java.util.List;

import com.randioo.randioo_server_base.module.BaseService;

public class MarketModelServiceImpl extends BaseService implements MarketModelService {

	private MarketHandler handler;

	public void setHandler(MarketHandler handler) {
		this.handler = handler;
	}

	@Override
	public List<MarketItem> gamble(MarketInterface marketInterface, MarketTypeInfo info) {
		List<MarketItem> list = handler.getItems(marketInterface, info);
		return list;
	}

}
