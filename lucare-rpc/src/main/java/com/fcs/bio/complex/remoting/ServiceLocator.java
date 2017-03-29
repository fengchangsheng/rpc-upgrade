package com.fcs.bio.complex.remoting;

import java.io.IOException;
import java.lang.reflect.Proxy;

public class ServiceLocator<T> {
	
	/**
	 * 
	 */
	private Class<T> api ;
	
	/**
	 * 
	 */
	private String serviceName ;

	/**
	 * 
	 */
	private String host ;
	
	/**
	 * 
	 */
	private int port ;
	
	/**
	 * 
	 */
	private String appName ;
	
	/**
	 * 
	 */
	private int connectTimeout = 1000 ;
	
	/**
	 * 
	 */
	private int readTimeout = 1000 ;
	
	/**
	 * 
	 */
	private Object proxy ;

	public ServiceLocator(Class<T> api, String serviceName, String host, int port, String appName) throws IOException, InterruptedException {
		this.api = api;
		this.serviceName = serviceName;
		this.host = host;
		this.port = port;
		this.appName = appName;
		initProxy();
	}

	public ServiceLocator(Class<T> api, String serviceName, String host, int port, String appName, int connectTimeout, int readTimeout) throws IOException, InterruptedException {
		this.api = api ;
		this.serviceName = serviceName ;
		this.host = host ;
		this.port = port ;
		this.appName = appName ;
		this.connectTimeout = connectTimeout ;
		this.readTimeout = readTimeout ;
		initProxy() ;
	}
	
	
	private void initProxy() throws IOException, InterruptedException {
		this.proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{api}, new RemotingServiceProxy(this)) ;
	}
	
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}


	public Object getProxy() {
		return proxy;
	}


	@Override
	public String toString() {
		return "ServiceLocator [api=" + api + ", serviceName=" + serviceName + ", host=" + host + ", port=" + port + ", contextName=" + appName + ", connectTimeout=" + connectTimeout + ", readTimeout=" + readTimeout + "]";
	}

}
