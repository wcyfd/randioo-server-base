package com.randioo.randioo_server_base;

import com.randioo.randioo_server_base.utils.ReflectUtils;
import com.randioo.randioo_server_base.utils.game.record.GameRecorder;
import com.randioo.randioo_server_base.utils.game.record.RefRecordInfo;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Hello World!");
		GameRecorder g = new GameRecorder();

		Role role = new Role();
		War war = new War();
		war.setPoint(20);
		role.setWar(war);
		System.out.println(role.getMoney());

		g.addRecord(role, ReflectUtils.getMethod(Role.class, "setMoney", int.class), role, role.getMoney());
		g.addRecord(role, new RefRecordInfo<Role>(role) {
			private int point;

			@Override
			public void record() {
				point = t.getWar().getPoint();
			}

			@Override
			public void reset() {
				t.getWar().setPoint(point);
			}

		});

		System.out.println(role.getMoney());
		System.out.println(role.getWar().getPoint());
		role.setMoney(34);
		role.getWar().setPoint(50);
		System.out.println(role.getMoney());
		System.out.println(role.getWar().getPoint());
		// g.clearRecord(role);

		g.resetRecord(role);
		System.out.println(role.getMoney());
		System.out.println(role.getWar().getPoint());

	}
}
