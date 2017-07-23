package com.randioo.randioo_server_base.log;

import com.randioo.randioo_server_base.entity.RoleInterface;
import com.randioo.randioo_server_base.utils.TimeUtils;

public class HttpLogUtils {

	protected static String projectName = null;

	public static void setProjectName(String proj) {
		projectName = proj;
	}

	public static String role(RoleInterface role, Object message) {
		String roleId = role != null ? role.getRoleId() + "" : null;
		String name = role != null ? role.getName() : null;
		String account = role != null ? role.getAccount() : null;

		StringBuilder sb = new StringBuilder();
		sb.append("<ROLE>[roleId:").append(roleId).append(",account:").append(account).append(",name:").append(name)
				.append("] * ").append(TimeUtils.getDetailTimeStr()).append(" * ").append(message);
		String output = sb.toString();
		if (output.length() < 120)
			output = output.replaceAll("\n", " ").replace("\t", " ").replace("  ", "");

		return output;
	}

	public static String sys(Object message) {
		StringBuilder sb = new StringBuilder();
		sb.append("<SYS> * ").append(TimeUtils.getDetailTimeStr()).append(" * ").append(message);
		return sb.toString();
	}
}
