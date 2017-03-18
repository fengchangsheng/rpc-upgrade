package com.fcs.nio.util;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * Created by Lucare.Feng on 2017/3/18.
 */
public class ByteUtil {

    public static Object getObject(byte[] bytes) throws IOException, ClassNotFoundException{
        ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
        ObjectInputStream oi = new ObjectInputStream(bi);
        Object obj = oi.readObject();
        bi.close();
        oi.close();
        return obj;
    }

    public static ByteBuffer getByteBuffer(Object obj) throws IOException{
        byte[] bytes = ByteUtil.getBytes(obj);
        ByteBuffer buff = ByteBuffer.wrap(bytes);
        return buff;
    }

    public static byte[] getBytes(Object obj) throws IOException{
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bout);
        out.writeObject(obj);
        out.flush();
        byte[] bytes = bout.toByteArray();
        bout.close();
        out.close();
        return bytes;
    }

}
