package com.fcs.bio.complex.server.contexts;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lucare.Feng on 2017/3/29.
 */
public class AppContextManager {

    private static Map<String, AppContext> contexts = new HashMap<>() ;

    public static void register(AppContext appContext) {
        contexts.put(appContext.getName(), appContext) ;
    }

    public static AppContext getAppContextImpl(String name) {
        return contexts.get(name) ;
    }

    public static void unRegister(String name) {
        contexts.remove(name) ;
    }

}
