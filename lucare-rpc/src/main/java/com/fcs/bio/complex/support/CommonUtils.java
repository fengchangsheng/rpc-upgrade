package com.fcs.bio.complex.support;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by Lucare.Feng on 2017/3/29.
 */
public class CommonUtils {


    public static final String UTF8 = "UTF-8";

    public static final List<?> EMPTY_LIST = Collections.unmodifiableList(new ArrayList<Object>());

    public static final Map<?, ?> EMPTY_MAP = Collections.unmodifiableMap(new HashMap<Object, Object>());

    /**
     * @param input
     * @return
     */
    public static boolean isEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }

    /**
     * @param input
     * @return
     */
    public static String emptyIfNull(String input) {
        return input == null ? "" : input.trim();
    }

    /**
     * @param input
     * @param def
     * @return
     */
    public static String emptyIfNull(String input, String def) {
        input = emptyIfNull(input);
        return input.isEmpty() ? def : input;
    }

    /**
     * @param data
     * @param def
     * @return
     */
    public static int parseInt(Object data, int def) {
        if (data != null) {
            try {
                return (data instanceof Number) ? ((Number) data).intValue() : Integer.valueOf(String.valueOf(data));
            } catch (Exception ignore) {
            }
        }
        return def;
    }

    /**
     * @param data
     * @param def
     * @return
     */
    public static long parseLong(Object data, int def) {
        if (data != null) {
            try {
                if (data instanceof Number) {
                    return ((Number) data).longValue();
                }
                return Long.valueOf(String.valueOf(data));
            } catch (Exception ignore) {
            }
            ;
        }
        return def;
    }


    /**
     * @param data
     * @param def
     * @return
     */
//    public static double parseDouble(Object data, double def) {
//        double value = def;
//        if (data != null) {
//            try {
//                if (data != null) {
//                    if (data instanceof BigDecimal) {
//                        value = ((BigDecimal) data).doubleValue();
//                    } else if (data instanceof Number) {
//                        value = ((Number) data).doubleValue();
//                    } else {
//                        value = Double.valueOf(String.valueOf(data));
//                    }
//                }
//            } catch (Exception ignore) {
//            }
//            ;
//        }
//        return value == def ? def : MathUtils.roundHalfUp(value);
//    }


    /**
     * @param array
     * @return
     */
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }


    /**
     * @param c
     * @return
     */
    public static boolean isEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }


    /**
     * @param c
     * @return
     */
    public static int size(Collection<?> c) {
        return c == null ? 0 : c.size();
    }


    /**
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> emptyList() {
        return (List<T>) EMPTY_LIST;
    }


    /**
     * 获取一个空List
     *
     * @param list
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> emptyList(List<T> list) {
        return (List<T>) (list == null ? emptyList() : list);
    }


    /**
     * @param c
     * @return
     */
    public static <T> List<T> newArrayList(Collection<?> c) {
        return new ArrayList<>(size(c));
    }


    /**
     * @param map
     * @return
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> emptyMap() {
        return (Map<K, V>) EMPTY_MAP;
    }

    /**
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> emptyMap(Map<K, V> map) {
        return (Map<K, V>) (map == null ? EMPTY_MAP : map);
    }


    /**
     * @param size
     * @return
     */
    public static <K, V> Map<K, V> stableMap(int size) {
        return new HashMap<K, V>(size, 1.0f);
    }


    /**
     * @param t
     * @return
     */
    public static IllegalStateException illegalStateException(Throwable t) {
        return new IllegalStateException(t);
    }

    /**
     * @param message
     * @return
     */
    public static IllegalStateException illegalStateException(String message) {
        return new IllegalStateException(message);
    }

    /**
     * @param message
     * @param t
     * @return
     */
    public static IllegalStateException illegalStateException(String message, Throwable t) {
        return new IllegalStateException(message, t);
    }


    /**
     * @param message
     * @return
     */
    public static IllegalArgumentException illegalArgumentException(String message) {
        return new IllegalArgumentException(message);
    }


    /**
     * @return
     */
    public static UnsupportedOperationException unsupportedMethodException() {
        return new UnsupportedOperationException("unsupport this method");
    }


//    /**
//     * @param t
//     * @return
//     */
//    public static RuntimeException convertRuntimeException(Throwable t) {
//        return (t instanceof RuntimeException) ? (RuntimeException) t : new InvokeNotFoundException(t);
//    }


    /**
     * @param t
     * @return
     */
    public static Throwable foundRealThrowable(Throwable t) {
        Throwable cause = t.getCause();
        if (cause == null) return t;
        return foundRealThrowable(cause);
    }


    /**
     * 格式化异常
     *
     * @param t
     * @return
     */
    public static String formatThrowable(Throwable t) {
        if (t == null) return "";
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }


    public static String formatThrowableForHtml(Throwable t) {
        String ex = formatThrowable(t);
        return ex.replaceAll("\n\t", " ");
    }


    /**
     * 实例化对象,注意该对象必须有无参构造函数
     *
     * @param klass
     * @return
     */
    public static <T> T newInstance(Class<T> klass) {
        try {
            return (T) klass.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("instance class[" + klass.getName() + "] with ex:", e);
        }
    }


    /**
     * @param klass
     * @param cstTypes
     * @param cstParameters
     * @return
     */
    public static <T> T newInstance(Class<T> klass, Class<?>[] cstTypes, Object... cstParameters) {
        try {
            Constructor<T> cst = klass.getConstructor(cstTypes);
            return cst.newInstance(cstParameters);
        } catch (Exception e) {
            throw new IllegalArgumentException("instance class[" + klass.getName() + "], cstTypes=" + Arrays.toString(cstTypes) + ", " + Arrays.toString(cstParameters) + " with ex:", e);
        }
    }


    /**
     * @param className
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(String className) {
        try {
            return (T) newInstance(classForName(className));
        } catch (Exception e) {
            throw new IllegalArgumentException("instance class[" + className + "] with ex:", e);
        }
    }


    /**
     * @param className
     * @return
     */
    public static Class<?> classForName(String className) {
        try {
            return Class.forName(className, false, Thread.currentThread().getContextClassLoader());

        } catch (Exception ignore) {
            try {
                return Class.forName(className);
            } catch (Exception e) {
                throw new IllegalArgumentException("classForName(" + className + ")  with ex:", e);
            }
        }
    }


    /**
     * @param className
     * @param classLoader
     * @return
     */
    public static Class<?> loadClass(String className, ClassLoader classLoader) {
        try {
            return classLoader.loadClass(className);
        } catch (Exception e) {
            throw new IllegalArgumentException("loadClass(" + className + ")  with ex:", e);
        }
    }


    /**
     * @param filename
     * @return
     */
    public static InputStream getInputStreamFromClassPath(String filename) {
        return CommonUtils.isEmpty(filename) ? null : Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
    }


    /**
     * 合并多个路径
     *
     * @param paths
     * @return
     */
    public static String merges(String... paths) {
        if (isEmpty(paths)) return null;
        StringBuilder builder = new StringBuilder(paths[0]);
        for (int i = 1; i < paths.length; i++) {
            String path = emptyIfNull(paths[i]);
            path = path.startsWith(File.separator) ? path.substring(1) : path;
            path = path.endsWith(File.separator) ? path.substring(0, path.length() - 1) : path;
            builder.append(File.separator).append(path);
        }
        return builder.toString();
    }


    /**
     * @param input
     * @return
     */
    public static String urlDecodeUTF8(String input) {
        try {
            return input == null ? null : URLDecoder.decode(input, UTF8);
        } catch (Exception e) {
            throw illegalStateException(e);
        }
    }


    /**
     * @param input
     * @return
     */
    public static String urlEncodeUTF8(String input) {
        try {
            return input == null ? null : URLEncoder.encode(input, UTF8);
        } catch (Exception e) {
            throw illegalStateException(e);
        }
    }


    /**
     * @param map
     * @param key
     * @param value
     */
    public static <K, V> void putIfNotNull(Map<K, V> map, K key, V value) {
        if (map != null && key != null && value != null) map.put(key, value);
    }


    /**
     * @param map
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(Map<?, ?> map, Object key) {
        return (T) (map == null ? null : map.get(key));
    }


    /**
     * @param map
     * @param key
     * @param def
     * @return
     */
    public static <T> T get(Map<?, ?> map, Object key, T def) {
        T t = get(map, key);
        return t == null ? def : t;
    }


    /**
     * @param e
     * @return
     */
    public static RuntimeException convertRuntimeException(Exception e) {
        return new RuntimeException(e);
    }


    /**
     *
     */
    private static Boolean isWindowsOS = null;

    /**
     * 是否是windows操作系统
     *
     * @return
     */
    public static boolean isWindowsOS() {
        if (isWindowsOS == null) {
            isWindowsOS = (System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1);
        }
        return isWindowsOS.booleanValue();
    }

}
