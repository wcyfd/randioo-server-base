package com.randioo.randioo_server_base.net.protocal.http;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.mina.http.api.HttpMethod;
import org.apache.mina.http.api.HttpRequest;
import org.apache.mina.http.api.HttpVersion;

public class HttpRequestImpl implements HttpRequest {

	private final HttpVersion version;
	private final HttpMethod method;
	private final String requestedPath;
	private final String queryString;
	private final Map headers;

	public HttpRequestImpl(HttpVersion version, HttpMethod method, String requestedPath, String queryString, Map headers) {
		this.version = version;
		this.method = method;
		this.requestedPath = requestedPath;
		this.queryString = queryString;
		this.headers = headers;
	}

	public HttpVersion getProtocolVersion() {
		return version;
	}

	public String getContentType() {
		return (String) headers.get("content-type");
	}

	public boolean isKeepAlive() {
		return false;
	}

	public String getHeader(String name) {
		return (String) headers.get(name);
	}

	public boolean containsHeader(String name) {
		return headers.containsKey(name);
	}

	public Map getHeaders() {
		return headers;
	}

	public boolean containsParameter(String name) {
		Matcher m = parameterPattern(name);
		return m.find();
	}

	public String getParameter(String name) {
		Matcher m = parameterPattern(name);
		if (m.find())
			return m.group(1);
		else
			return null;
	}

	protected Matcher parameterPattern(String name) {
		return Pattern.compile((new StringBuilder()).append("[&]").append(name).append("=([^&]*)").toString()).matcher(
				(new StringBuilder()).append("&").append(queryString).toString());
	}

	public Map getParameters() {
		Map parameters = new HashMap();
		String params[] = queryString.split("&");
		if (params.length == 1)
			return parameters;
		for (int i = 0; i < params.length; i++) {
			String param[] = params[i].split("=");
			String name = param[0];
			String value = param.length != 2 ? "" : param[1];
			if (!parameters.containsKey(name))
				parameters.put(name, new ArrayList());
			((List) parameters.get(name)).add(value);
		}

		return parameters;
	}

	public String getQueryString() {
		return queryString;
	}

	public HttpMethod getMethod() {
		return method;
	}

	public String getRequestPath() {
		return requestedPath;
	}

	public String toString() {
		String result = (new StringBuilder()).append("HTTP REQUEST METHOD: ").append(method).append("\n").toString();
		result = (new StringBuilder()).append(result).append("VERSION: ").append(version).append("\n").toString();
		result = (new StringBuilder()).append(result).append("PATH: ").append(requestedPath).append("\n").toString();
		result = (new StringBuilder()).append(result).append("QUERY:").append(queryString).append("\n").toString();
		result = (new StringBuilder()).append(result).append("--- HEADER --- \n").toString();
		for (Iterator iterator = headers.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			String value = (String) headers.get(key);
			result = (new StringBuilder()).append(result).append(key).append(":").append(value).append("\n").toString();
		}

		result = (new StringBuilder()).append(result).append("--- PARAMETERS --- \n").toString();
		Map parameters = getParameters();
		for (Iterator iterator1 = parameters.keySet().iterator(); iterator1.hasNext();) {
			String key = (String) iterator1.next();
			Collection values = (Collection) parameters.get(key);
			Iterator iterator2 = values.iterator();
			while (iterator2.hasNext()) {
				String value = (String) iterator2.next();
				result = (new StringBuilder()).append(result).append(key).append(":").append(value).append("\n")
						.toString();
			}
		}

		return result;
	}
}
