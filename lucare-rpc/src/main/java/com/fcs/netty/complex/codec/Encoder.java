package com.fcs.netty.complex.codec;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by Lucare.Feng on 2017/4/30.
 */
public class Encoder extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        System.out.println("encode come in.");
        Header header = message.getHeader();
        byteBuf.writeByte(header.getMagic());
        byteBuf.writeByte(header.getVersion());
        Class<?>[] paramType = message.getParamTypes();
        Object[] args = message.getArgs();
        Object[] parameters = new Object[]{message.getServiceName(),message.getMethodName(),paramType,args};//使用对象数组传输的字节数是Java序列化的1/6  是json序列化的1/3
        if (parameters != null) {
            byte[] body = JSON.toJSONBytes(parameters, SerializerFeature.WriteClassName, SerializerFeature.WriteDateUseDateFormat);
            int len = body.length;
            byteBuf.writeInt(len);
            byteBuf.writeBytes(body);
        }

    }

}
