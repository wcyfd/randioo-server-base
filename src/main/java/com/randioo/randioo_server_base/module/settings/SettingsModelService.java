package com.randioo.randioo_server_base.module.settings;

import com.randioo.randioo_server_base.module.BaseServiceInterface;

public interface SettingsModelService extends BaseServiceInterface {
	public void setBackgroundMusic(SettingsInterface settingsInterface, boolean status);

	public void setSoundEffect(SettingsInterface settingsInterface, boolean status);

	public void setInformationPush(SettingsInterface settingsInterface, boolean status);
}
