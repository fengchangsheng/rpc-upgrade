package com.fcs.bio.complex.server.contexts;

import com.fcs.bio.complex.support.XmlElement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lucare.Feng on 2017/3/29.
 */
public class AppContext {

    private String name ;

    private Map<String, ServiceSkeleton> skeletonMap = new HashMap<>() ;

    public ServiceSkeleton getServiceSkeleton(String name) {
        return skeletonMap.get(name) ;
    }

    public AppContext(String name) {
        this.name = name;
        loadApp();
    }

    private void loadApp() {
        try {
            parseServicesXml();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseServicesXml() throws ClassNotFoundException, IllegalAccessException, InstantiationException, FileNotFoundException {
//        XmlElement root = XmlElement.read(new FileInputStream(new File("services.xml")));
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("services.xml");
        XmlElement root = XmlElement.read(inputStream);
        List<XmlElement> servicesList = root.elements("service");
        for (XmlElement node : servicesList) {
            String name = node.attributeValue("name");
            String apiName = node.element("home-api").getElementText().trim();
            String implName = node.element("home-class").getElementText().trim();
//            Class<?> apiClass = appClassLoader.loadClass(apiName);
//            Object service = appClassLoader.loadClass(implName).newInstance();
            Class<?> apiClass = Thread.currentThread().getContextClassLoader().loadClass(apiName);
            Object service = Thread.currentThread().getContextClassLoader().loadClass(implName).newInstance();
            ServiceSkeleton skeleton = new ServiceSkeleton(name, apiClass, service);
            this.skeletonMap.put(name, skeleton);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
