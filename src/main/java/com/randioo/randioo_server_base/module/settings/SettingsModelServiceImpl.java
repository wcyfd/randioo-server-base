package com.randioo.randioo_server_base.module.settings;

import org.springframework.stereotype.Service;

import com.randioo.randioo_server_base.module.BaseService;

@Service("SettingsModelService")
public class SettingsModelServiceImpl extends BaseService implements SettingsModelService {

	@Override
	public void setBackgroundMusic(SettingsInterface settingsInterface, boolean status) {
		settingsInterface.setMusic(status ? 1 : 0);
	}

	@Override
	public void setSoundEffect(SettingsInterface settingsInterface, boolean status) {
		settingsInterface.setSound(status ? 1 : 0);
	}

	@Override
	public void setInformationPush(SettingsInterface settingsInterface, boolean status) {
		settingsInterface.setPush(status ? 1 : 0);
	}

}
