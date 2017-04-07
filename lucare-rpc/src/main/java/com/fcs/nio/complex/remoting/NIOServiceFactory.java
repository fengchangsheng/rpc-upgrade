package com.fcs.nio.complex.remoting;

import com.fcs.bio.complex.support.XmlElement;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Lucare.Feng on 2017/4/2.
 */
public class NIOServiceFactory {

    private static Map<String, XmlLocator> xmlConfigs = new HashMap<>();

    private static ConcurrentHashMap<String, NIOServiceLocator<?>> serviceCache = new ConcurrentHashMap<>();

    static {
        load();
    }

    public static void load() {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("remoting.xml");
        if (is != null) {
            XmlElement root = XmlElement.read(is);
            List<XmlElement> businesses = root.elements("business");
            for (XmlElement b : businesses) {
                String business = b.attributeValue("name");
                List<XmlElement> services = b.elements("service");
                for (XmlElement s : services) {
                    String service = s.attributeValue("name");
                    String location = s.elementText("locator");

//                    String key = business + "-" + service;
                    String key = "xml-locator-" + service;
                    XmlLocator locator = new XmlLocator();
                    locator.business = business;
                    locator.service = service;
                    locator.location = location;
                    xmlConfigs.put(key, locator);
                }
            }
        }
    }

    public static <T> T getService(Class<T> api) {
        try {
            return (T) foundLocator(api).getProxy();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> NIOServiceLocator<T> foundLocator(Class<T> api) throws IOException, InterruptedException {
        String key = "locator-config-" + api.getName();
        String serviceName = api.getSimpleName();
        NIOServiceLocator<T> RPCServiceLocator = (NIOServiceLocator<T>) serviceCache.get(key);
        if (RPCServiceLocator == null) {
            synchronized (api) {
                RPCServiceLocator = (NIOServiceLocator<T>) serviceCache.get(key);
                if (RPCServiceLocator == null) {
                    String akey = "xml-locator-" + serviceName;
                    XmlLocator xml = xmlConfigs.get(akey);
                    String[] array = analyzeAddress(xml.location);
                    String host = array[0];
                    int port = Integer.valueOf(array[1]);
                    String context = array[2];
                    RPCServiceLocator = new NIOServiceLocator<T>(api, serviceName, host, port, context);
                    serviceCache.put(key, RPCServiceLocator);
                }
            }
        }
        return RPCServiceLocator;

    }

    private static String[] analyzeAddress(String address) {
        String[] array = address.split(":");
        String host = array[0].trim(), other = array[1].trim(), port = "", appname = "";
        int index = other.indexOf("/");
        if (index > -1 && index < other.length() - 1) {
            port = other.substring(0, index).trim();
            appname = other.substring(index + 1).trim();
        }
        return new String[]{host, port, appname};
    }

    private static class XmlLocator {
        @SuppressWarnings("unused")
        String business;
        @SuppressWarnings("unused")
        String service;
        int connTimeout = 1000;
        int readTimeout = 10000;
        String location = null;

    }

}
