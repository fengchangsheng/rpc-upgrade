package com.fcs.nio.complex.codec;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fcs.bio.complex.remoting.RPCHolder;
import com.fcs.bio.complex.support.CommonUtils;
import java.nio.ByteBuffer;


/**
 * Created by Lucare.Feng on 2017/4/14.
 */
public class ProtocolVersion1 {

    public static byte LUCARE_PROTOCOL_MAGIC_NUMBER = (byte) 'F';

    public static byte LUCARE_PROTOCOL_VERSION_1_0 = (byte) 0x10;

    private static Object[] __EMPTY__OBJECT__ARRAY__ = new Object[0];

    /**
     * request protocol: magic(1) + len(4) + version(1)  + header len(4) + header body + parameter len + parameter body
     */
    public static ByteBuffer encodeRequest(RPCHolder request) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(LUCARE_PROTOCOL_MAGIC_NUMBER);
        buffer.putInt(0);
        buffer.put(LUCARE_PROTOCOL_VERSION_1_0);

        String[] target = new String[]{request.getServiceName(), request.getMethodName()};
        byte[] header = JSON.toJSONBytes(target);
        buffer.putInt(header.length).put(header);
        int len = 1 + 4 + header.length;

        Class<?>[] paramType = request.getParameterTypes();
        Object[] args = request.getArgs();
        Object[] parameters = new Object[]{paramType,args};//使用对象数组传输的字节数是Java序列化的1/6  是json序列化的1/3
        if (parameters != null) {
            byte[] body = JSON.toJSONBytes(parameters, SerializerFeature.WriteClassName, SerializerFeature.WriteDateUseDateFormat);
            buffer.putInt(body.length).put(body);// 是否需要传递body length呢
            len = 1 + 4 + body.length;
        }

        int position = buffer.position();
        buffer.position(1);
        buffer.putInt(len).position(position);
        return buffer;
    }

    /**
     * request protocol: magic(1) + len(4) + version(1) + header len(4) + header body + parameter len + parameter body
     */
    public static RPCHolder decodeRequest(ByteBuffer buffer, int len, byte version) {
        if (version != LUCARE_PROTOCOL_VERSION_1_0)
            throw new ProtocolException("Invalid  protocol version.");
        len -= 1;// skip version(1)

        int hlen = buffer.getInt(); //head length
        len -= 4;// skip head length(4)

        byte[] hdata = new byte[hlen];
        buffer.get(hdata);
        len -= hlen;
        String[] header = JSON.parseObject(hdata, String[].class);

        RPCHolder request = new RPCHolder();
        request.setServiceName(header[0]);
        request.setMethodName(header[1]);
        Object[] parameters = __EMPTY__OBJECT__ARRAY__;
        if (len > 0) {
            int blen = buffer.getInt();
            byte[] bdata = new byte[blen];
            buffer.get(bdata);
            try {
                parameters = JSON.parseObject(bdata, Object[].class);
            } catch (Exception e) {
                System.err.println("decodeRequest[context=" + request.getAppName() + ", service=" + request.getServiceName() + ", method=" + request.getMethodName() + "] with ex:" + CommonUtils.formatThrowable(e));
            }
        }
        arrayToObjects(parameters, request);
        return request;
    }

    private static void arrayToObjects(Object[] params,RPCHolder holder) {
        JSONArray typeArray = (JSONArray) params[0];
        Class<?>[] paramTypes = new Class<?>[typeArray.size()];
        for (int index = 0; index < typeArray.size(); index++) {
            paramTypes[index] = typeArray.get(index).getClass();
        }
        JSONArray array = (JSONArray) params[1];
        Object[] args = new Object[array.size()];
        for (int index = 0; index < array.size(); index++) {
            args[index] = array.get(index);
        }
        holder.setParameterTypes(paramTypes);
        holder.setArgs(args);
    }

}
