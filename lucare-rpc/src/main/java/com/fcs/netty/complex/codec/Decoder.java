package com.fcs.netty.complex.codec;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fcs.nio.complex.codec.ProtocolException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Created by Lucare.Feng on 2017/4/30.
 */
public class Decoder extends LengthFieldBasedFrameDecoder {

    private static final int HEADER_SIZE = 6;

    public Decoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if (in == null)
            return null;
        if (in.readableBytes() < HEADER_SIZE)
            throw new Exception("可读信息端小于头部信息");

        byte magic = in.readByte();
        byte version = in.readByte();
        int length = in.readInt();//body的长度
        if (in.readableBytes() < length)
            throw new ProtocolException("Bad  request.");

        Header header = new Header(magic, version, length);
        Message message = new Message();
        message.setHeader(header);

        ByteBuf buf = in.readBytes(length);
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        Object[] parameters = null;
        try {
            parameters = JSON.parseObject(req, Object[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        arrayToObjects(parameters, message);
        return message;
    }


    private static void arrayToObjects(Object[] params,Message message) {
        String serviceName = (String) params[0];
        String methodName = (String) params[1];
        message.setServiceName(serviceName);
        message.setMethodName(methodName);
        JSONArray typeArray = (JSONArray) params[2];
        Class<?>[] paramTypes = new Class<?>[typeArray.size()];
        for (int index = 0; index < typeArray.size(); index++) {
            paramTypes[index] = typeArray.get(index).getClass();
        }
        JSONArray array = (JSONArray) params[3];
        Object[] args = new Object[array.size()];
        for (int index = 0; index < array.size(); index++) {
            args[index] = array.get(index);
        }
        message.setParamTypes(paramTypes);
        message.setArgs(args);
    }
}
