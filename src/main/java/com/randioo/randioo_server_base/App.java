package com.randioo.randioo_server_base;

import com.randioo.randioo_server_base.platform.Platform;
import com.randioo.randioo_server_base.platform.SignalTrigger;
import com.randioo.randioo_server_base.platform.Platform.OS;
import com.randioo.randioo_server_base.template.Function;
import com.randioo.randioo_server_base.utils.GameUtils;
import com.randioo.randioo_server_base.utils.GameUtils.ValueGet;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Hello World!");
//		RecordModelService g = new RecordModelServiceImpl();
//
//		Role role = new Role();
//		War war = new War();
//		war.setPoint(20);
//		role.setWar(war);
//		System.out.println(role.getMoney());
//
//		g.addRecord(role, ReflectUtils.getMethod(Role.class, "setMoney", int.class), role, role.getMoney());
//		g.addRecord(role, new RefRecordInfo<Role>(role) {
//			private int point;
//
//			@Override
//			public void record() {
//				point = t.getWar().getPoint();
//			}
//
//			@Override
//			public void reset() {
//				t.getWar().setPoint(point);
//			}
//
//		});
//
//		System.out.println(role.getMoney());
//		System.out.println(role.getWar().getPoint());
//		role.setMoney(34);
//		role.getWar().setPoint(50);
//		System.out.println(role.getMoney());
//		System.out.println(role.getWar().getPoint());
//		// g.clearRecord(role);
//
//		g.resetRecord(role);
//		System.out.println(role.getMoney());
//		System.out.println(role.getWar().getPoint());
		
		
//		Function function = new Function(){
//
//			@Override
//			public Object apply(Object... params) {
//				// TODO Auto-generated method stub
//				System.out.println("shutdown");
//				System.exit(0);
//				return null;
//			}
//			
//		};
//		try {
//			if(Platform.getOS() == OS.WIN){
//				SignalTrigger.setSignCallback("INT", function);				
//			}else if(Platform.getOS() == OS.LINUX){
//				SignalTrigger.setSignCallback("ABRT", function);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		for(;;){
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
		
	}
}
