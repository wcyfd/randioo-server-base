package com.randioo.randioo_server_base.utils.system;

public abstract class SystemManager {
	/** gm口令 */
	private String serverPassword = null;
	/** 服务器入口开关 */
	private boolean isService;

	private ServerManagerHandler serverTerminatedHandler;

	public void setServerTerminatedHandler(ServerManagerHandler serverTerminatedHandler) {
		this.serverTerminatedHandler = serverTerminatedHandler;
	}

	public boolean isService() {
		return isService;
	}

	public void open() {
		if (serverPassword == null) {
			serverPassword = createCode();
		}

		isService = true;
		
		if(serverTerminatedHandler!=null){
			serverTerminatedHandler.open();
		}
	}

	public void close() {
		isService = false;
		if(serverTerminatedHandler!=null){
			serverTerminatedHandler.close();
		}
	}

	public boolean checkPassword(String code) {
		return checkCode(serverPassword, code);
	}

	protected abstract String createCode();

	protected abstract boolean checkCode(String origin, String code);

	public void terminated() {
		if (serverTerminatedHandler != null) {
			serverTerminatedHandler.terminated();
		}
	}
}
