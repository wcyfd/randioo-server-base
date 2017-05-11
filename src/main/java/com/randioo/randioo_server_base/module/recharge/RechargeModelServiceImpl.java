package com.randioo.randioo_server_base.module.recharge;

import org.springframework.stereotype.Service;

import com.randioo.randioo_server_base.module.BaseService;

@Service("rechargeModelService")
public class RechargeModelServiceImpl extends BaseService implements RechargeModelService {

	private RechargeHandler handler;

	@Override
	public void setRechargeHandler(RechargeHandler handler) {
		this.handler = handler;
	}

	@Override
	public void recharge(Rechargeable rechargeable, int index) {
		handler.recharge(rechargeable, index);
	}	
}
