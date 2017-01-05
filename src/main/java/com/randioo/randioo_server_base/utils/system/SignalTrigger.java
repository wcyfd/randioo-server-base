package com.randioo.randioo_server_base.utils.system;
import java.util.HashMap;
import java.util.Map;

import sun.misc.Signal;
import sun.misc.SignalHandler;

import com.randioo.randioo_server_base.utils.template.Function;

/**
 * 信号触发器，不同平台信号量字符串不同
 * 
 * @author wcy 2017年1月5日
 *
 */
public class SignalTrigger {
	private static Map<String, Function> callbackMap = new HashMap<>();
	private static SignalHandler handler = new SignalHandler() {

		@Override
		public void handle(Signal signal) {
			Function function = callbackMap.get(signal.getName());
			if (function != null) {
				function.apply();
			}
		}
	};

	public static void setSignCallback(String sign, Function callback) throws Exception {
		Signal.handle(new Signal(sign), handler);
		callbackMap.put(sign, callback);
	}

}
