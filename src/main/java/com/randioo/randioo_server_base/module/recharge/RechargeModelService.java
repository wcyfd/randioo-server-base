package com.randioo.randioo_server_base.module.recharge;

import com.randioo.randioo_server_base.module.BaseServiceInterface;

public interface RechargeModelService extends BaseServiceInterface{
	
	public void recharge(Rechargeable rechargeable,int index);

	void setRechargeHandler(RechargeHandler handler);
}
