package com.randioo.randioo_server_base.heart;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

import com.google.protobuf.GeneratedMessage;
import com.randioo.randioo_server_base.utils.ReflectUtils;

public class ProtoHeartFactory implements KeepAliveMessageFactory {

	private GeneratedMessage heartResponse;
	private GeneratedMessage scHeart;
	private GeneratedMessage heartRequest;
	private Method scParseDelimitedFromMethod;
	private Method csParseDelimitedFromMethod;

	public ProtoHeartFactory(Class<? extends GeneratedMessage> csClass, Class<? extends GeneratedMessage> scClass) {
		GeneratedMessage heartRequestCS = getTargetGeneratedMessage(csClass, "HeartRequest");
		GeneratedMessage heartResponseSC = getTargetGeneratedMessage(scClass, "HeartResponse");
		GeneratedMessage SCHeartSC = getTargetGeneratedMessage(scClass, "SCHeart");

		this.checkNull(csClass, scClass, heartRequestCS, SCHeartSC, heartResponseSC);

		this.heartRequest = heartRequestCS;
		this.heartResponse = heartResponseSC;
		this.scHeart = SCHeartSC;

		csParseDelimitedFromMethod = getParseDelimatedFromMethod(csClass);
		scParseDelimitedFromMethod = getParseDelimatedFromMethod(scClass);
	}

	private Method getParseDelimatedFromMethod(Class<? extends GeneratedMessage> clazz) {
		String parseDelimitedFrom = "parseDelimitedFrom";
		return ReflectUtils.getMethod(clazz, parseDelimitedFrom, InputStream.class);
	}

	private GeneratedMessage getTargetGeneratedMessage(Class<? extends GeneratedMessage> csClass, String messageName) {
		Method csNewBuilderMethod = ReflectUtils.getMethod(csClass, "newBuilder");
		Object builder = ReflectUtils.invokeMethodWithResult(null, csNewBuilderMethod);
		Method csBuildMethod = ReflectUtils.getMethod(builder.getClass(), "build");

		// 获得HeartRequestBuilder的类
		Method getHeartRequestMethod = ReflectUtils.getMethod(builder.getClass(), "get" + messageName);
		GeneratedMessage heartRequest = ReflectUtils.invokeMethodWithResult(builder, getHeartRequestMethod);
		Method heartRequestBuilderMethod = ReflectUtils.getMethod(heartRequest.getClass(), "toBuilder");
		Class<?> heartRequestBuilderClass = heartRequestBuilderMethod.getReturnType();

		Method setHeartRequestMethod = ReflectUtils.getMethod(builder.getClass(), "set" + messageName,
				heartRequestBuilderClass);

		Method heartRequestBuilderNewBuilderMethod = ReflectUtils.getMethod(heartRequest.getClass(), "newBuilder");
		Object heartRequestBuilder = ReflectUtils.invokeMethodWithResult(null, heartRequestBuilderNewBuilderMethod);

		ReflectUtils.invokeMethod(builder, setHeartRequestMethod, heartRequestBuilder);
		GeneratedMessage cs = ReflectUtils.invokeMethodWithResult(builder, csBuildMethod);
		return cs;
	}

	private void checkNull(Class<? extends GeneratedMessage> csClass, Class<? extends GeneratedMessage> scClass,
			GeneratedMessage request, GeneratedMessage scHeart, GeneratedMessage response) {
		if (csClass == null)
			throw new NullPointerException();
		if (scClass == null)
			throw new NullPointerException();
		if (request == null)
			throw new NullPointerException();
		if (scHeart == null)
			throw new NullPointerException();
		if (response == null)
			throw new NullPointerException();
	}

	@Override
	public Object getRequest(IoSession arg0) {
		return scHeart;
	}

	@Override
	public Object getResponse(IoSession arg0, Object arg1) {
		return heartResponse;
	}

	@Override
	public boolean isRequest(IoSession arg0, Object arg1) {
		return check(arg1, heartRequest, csParseDelimitedFromMethod);
	}

	@Override
	public boolean isResponse(IoSession arg0, Object arg1) {
		return check(arg1, heartResponse, scParseDelimitedFromMethod);
	}

	private boolean check(Object arg1, Object msg, Method parseDelimatedFromMethod) {
		if (arg1 instanceof GeneratedMessage) {
			if (arg1.toString().equals(msg.toString()))
				return true;
		} else {
			InputStream input = (InputStream) arg1;
			input.mark(0);

			try {
				GeneratedMessage message = ReflectUtils.invokeMethodWithResult(null, parseDelimatedFromMethod, input);
				if (message.toString().equals(msg.toString()))
					return true;

			} catch (Exception e) {

			} finally {
				try {
					if (input != null)
						input.reset();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

}
