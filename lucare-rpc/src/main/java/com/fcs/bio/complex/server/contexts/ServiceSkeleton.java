package com.fcs.bio.complex.server.contexts;

/**
 * Created by Lucare.Feng on 2017/3/29.
 */
public class ServiceSkeleton {


    /**
     * The service name.
     */
    private final String name;

    /**
     * The service interface class.
     */
    private final Class<?> apiClass;

    /**
     * The service implementation object.
     */
    private final Object service;

    /**
     *
     * @param name
     * @param apiClass
     * @param service
     */
    public ServiceSkeleton(String name, Class<?> apiClass, Object service) {
        this.name = name;
        this.service = service;
        this.apiClass = apiClass;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return this.name;
    }


    /**
     *
     * @return
     */
    public Class<?> getApiClass() {
        return this.apiClass;
    }

    /**
     *
     * @return
     */
    public Object getService() {
        return service;
    }


    @Override
    public String toString() {
        return "{name="+this.name+", apiClass="+this.apiClass.getName()+", service="+this.service.getClass().getName()+"}" ;
    }

}
