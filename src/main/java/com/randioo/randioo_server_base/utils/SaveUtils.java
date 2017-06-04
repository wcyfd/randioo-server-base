package com.randioo.randioo_server_base.utils;

import com.randioo.randioo_server_base.db.Saveable;
import com.randioo.randioo_server_base.entity.RoleInterface;

public class SaveUtils {
	public static boolean needSave(Saveable saveable, boolean mustSave) {
		boolean isChange = saveable.isChange() || mustSave;
		saveable.setChange(false);
//		if (isChange)
//			System.out.println("saveData:" + saveable.getClass().getSimpleName());
		return isChange;
	}

	public static boolean needSave(RoleInterface role, Saveable saveable, boolean mustSave) {
		boolean isChange = saveable.isChange() || mustSave;
		saveable.setChange(false);
//		if (isChange)
//			System.out.println("id:" + role.getRoleId() + ",account:" + role.getAccount() + ",name:" + role.getName()
//					+ "] saveData:" + saveable.getClass().getSimpleName());
		return isChange;
	}
}
