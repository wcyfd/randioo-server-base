package com.randioo.randioo_server_base.module.market;

import java.util.List;

public interface MarketHandler {
	public List<MarketItem> getItems(MarketInterface marketInterface,MarketTypeInfo info);
}
