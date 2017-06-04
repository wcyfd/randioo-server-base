package com.randioo.randioo_server_base.module.invite;

import com.randioo.randioo_server_base.service.BaseServiceInterface;

public interface InviteModelService extends BaseServiceInterface{
	public void setHandler(InviteHandler handler);

	public void newInvitation(Invitable starter, int count);

	public void invite(Invitable starter, Invitable targeter);
	
	public void response(Invitable starter, Invitable targeter, boolean invite);
	
	public void cancelInvite(Invitable targeter) ;
	
	public void cancelAllInvite(Invitable starter);
}
