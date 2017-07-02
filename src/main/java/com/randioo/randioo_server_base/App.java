package com.randioo.randioo_server_base;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalListeners;
import com.google.common.cache.RemovalNotification;
import com.randioo.randioo_server_base.entity.DefaultRole;
import com.randioo.randioo_server_base.lock.CacheLockUtil;
import com.randioo.randioo_server_base.template.EntityRunnable;
import com.randioo.randioo_server_base.utils.RandomUtils;

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
		cache2();
	}

	private static ExecutorService sqlpool = Executors.newCachedThreadPool();
	private static int size = 100;

	private static LoadingCache<String, DefaultRole> roleCache = null;

	public static void cache() {
		roleCache = CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.SECONDS)
				.removalListener(new RemovalListener<String, DefaultRole>() {

					@Override
					public void onRemoval(RemovalNotification<String, DefaultRole> notification) {
						System.out.println("remove " + notification.getKey());
						DefaultRole role = notification.getValue();
						Lock lock = CacheLockUtil.getLock(DefaultRole.class, role.getAccount());
						try {
							lock.lock();
							System.out.println("remove " + notification.getKey() + " locking");
							Future<?> future = sqlpool.submit(new EntityRunnable<DefaultRole>(role) {

								@Override
								public void run(DefaultRole entity) {

									System.out.println("role=" + entity.getAccount() + " sql update");

								}

							});
							future.get();
							System.out.println("future get " + role.getAccount());
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							lock.unlock();
						}

					}
				}).build(new CacheLoader<String, DefaultRole>() {

					@Override
					public DefaultRole load(String arg0) throws Exception {
						System.out.println("need load " + arg0);
						Lock lock = CacheLockUtil.getLock(DefaultRole.class, arg0);
						try {
							lock.lock();
							System.out.println("load" + arg0);
							DefaultRole role = new DefaultRole();
							role.setAccount(arg0);
							return role;
						} finally {
							lock.unlock();
						}
					}
				});

		try {
			for (int i = 0; i < size; i++) {
				roleCache.get("wcy" + i);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		Executors.newScheduledThreadPool(10).scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				roleCache.cleanUp();
			}

		}, 0, 1, TimeUnit.SECONDS);
		Executors.newScheduledThreadPool(100).scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				int value = RandomUtils.getRandomNum(size);
				try {
					DefaultRole role = roleCache.get("wcy" + value);
					System.out.println("thread read " + role.getAccount());
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}, 0, 500, TimeUnit.MILLISECONDS);
	}

	public static void cache2() {

		roleCache = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.SECONDS).maximumSize(100)
				.removalListener(new RemovalListener<String, DefaultRole>() {

					@Override
					public void onRemoval(RemovalNotification<String, DefaultRole> notification) {
						String key = notification.getKey();
						DefaultRole role = notification.getValue();
						System.out.println("remove " + key);

						Lock lock = CacheLockUtil.getLock(DefaultRole.class, key);
						try {
							lock.lock();
							Future<?> future = sqlpool.submit(new EntityRunnable<DefaultRole>(role) {

								@Override
								public void run(DefaultRole entity) {
									// TODO Auto-generated method stub

								}
							});

							future.get();
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							lock.unlock();
						}
						System.out.println("remove success");
					}
				}).build(new CacheLoader<String, DefaultRole>() {

					@Override
					public DefaultRole load(String arg0) throws Exception {
						System.out.println("load  " + arg0);
						Lock lock = CacheLockUtil.getLock(DefaultRole.class, arg0);
						try {
							lock.lock();
							System.out.println("read locking");
							DefaultRole role = new DefaultRole();
							role.setAccount(arg0);
							return role;
						} finally {
							lock.unlock();
						}

					}
				});

		for (int i = 0; i < size; i++) {
			try {
				roleCache.get("wcy" + i);
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				System.out.println("clean up");
				roleCache.cleanUp();
			}
		}, 2, 1, TimeUnit.SECONDS);

		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				try {
					int value = RandomUtils.getRandomNum(size);
					System.out.println("main need get");
					roleCache.get("wcy" + value);
					System.out.println("main has get");
				} catch (ExecutionException e1) {
					e1.printStackTrace();
				}
			}
		}, 0, 1, TimeUnit.SECONDS);

	}
}
