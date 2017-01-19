package com.randioo.randioo_server_base.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JSInvoker {
	private static JSInvoker jsInvoker = null;
	private ScriptEngine engine;
	private File file;

	private JSInvoker() {

	}

	public synchronized static JSInvoker getInstance() {
		if (jsInvoker == null) {
			jsInvoker = new JSInvoker();
			jsInvoker.init("./function.js");
		}
		return jsInvoker;
	}

	private void init(String filename) {		
		ScriptEngineManager manager = new ScriptEngineManager();
		engine = manager.getEngineByName("js");
		
		file = new File(filename);
		this.refreshScript();
	}
	
	public void refreshScript(){
		try {
			FileReader reader = new FileReader(file);
			engine.eval(reader);
		} catch (ScriptException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Object invoke(String functionName ,Object... param) {
		try {
			if (engine instanceof Invocable) {
				Invocable invoke = (Invocable) engine;
				Object obj = invoke.invokeFunction(functionName, param);
				return obj;
			}

		} catch (NoSuchMethodException | ScriptException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		JSInvoker invoker = JSInvoker.getInstance();
		Object obj =invoker.invoke("getCatName");
		System.out.println(obj);
		
	}
}
