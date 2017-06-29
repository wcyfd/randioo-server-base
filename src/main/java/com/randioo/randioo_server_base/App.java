package com.randioo.randioo_server_base;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.randioo.randioo_server_base.entity.DefaultRole;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Hello World!");
		// RecordModelService g = new RecordModelServiceImpl();
		//
		// Role role = new Role();
		// War war = new War();
		// war.setPoint(20);
		// role.setWar(war);
		// System.out.println(role.getMoney());
		//
		// g.addRecord(role, ReflectUtils.getMethod(Role.class, "setMoney",
		// int.class), role, role.getMoney());
		// g.addRecord(role, new RefRecordInfo<Role>(role) {
		// private int point;
		//
		// @Override
		// public void record() {
		// point = t.getWar().getPoint();
		// }
		//
		// @Override
		// public void reset() {
		// t.getWar().setPoint(point);
		// }
		//
		// });
		//
		// System.out.println(role.getMoney());
		// System.out.println(role.getWar().getPoint());
		// role.setMoney(34);
		// role.getWar().setPoint(50);
		// System.out.println(role.getMoney());
		// System.out.println(role.getWar().getPoint());
		// // g.clearRecord(role);
		//
		// g.resetRecord(role);
		// System.out.println(role.getMoney());
		// System.out.println(role.getWar().getPoint());

		// Function function = new Function(){
		//
		// @Override
		// public Object apply(Object... params) {
		// // TODO Auto-generated method stub
		// System.out.println("shutdown");
		// System.exit(0);
		// return null;
		// }
		//
		// };
		// try {
		// if(Platform.getOS() == OS.WIN){
		// SignalTrigger.setSignCallback("INT", function);
		// }else if(Platform.getOS() == OS.LINUX){
		// SignalTrigger.setSignCallback("ABRT", function);
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// for(;;){
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
		cache();
	}

	private static LoadingCache<String, DefaultRole> roleCache = null;

	public static void cache() {
		roleCache = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.SECONDS)
				.removalListener(new RemovalListener<String, DefaultRole>() {

					@Override
					public void onRemoval(RemovalNotification<String, DefaultRole> notification) {
						System.out.println("remove " + notification.getKey());
					}
				}).build(new CacheLoader<String, DefaultRole>() {

					@Override
					public DefaultRole load(String arg0) throws Exception {
						System.out.println("load");
						return null;
					}
				});

		try {
			for (int i = 0; i < 100; i++) {
				DefaultRole role = new DefaultRole();
				role.setAccount("wcy" + i);
				roleCache.put("wcy" + i, role);
			}

			for (DefaultRole role : roleCache.asMap().values()) {
				System.out.println(role.getAccount());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				roleCache.cleanUp();
				System.out.println(roleCache.size());
			}
			
		}, 0, 1, TimeUnit.SECONDS);
	}
}
