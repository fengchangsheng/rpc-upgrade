package com.fcs.nio.complex.codec;

import com.fcs.bio.complex.remoting.RPCHolder;

import java.nio.ByteBuffer;

/**
 * Created by Lucare.Feng on 2017/4/14.
 */
public class CodecHelper {

    public static byte LUCARE_PROTOCOL_MAGIC_NUMBER = (byte) 'F';

    public static byte LUCARE_PROTOCOL_VERSION_1_0 = (byte) 0x10;

    public static ByteBuffer encodeRequest(RPCHolder request) {
        return ProtocolVersion1.encodeRequest(request);
    }

    public static RPCHolder decodeRequest(ByteBuffer buffer) {
        if (buffer.remaining() < 6)
            return null;
        byte magic = buffer.get();

        if (magic != LUCARE_PROTOCOL_MAGIC_NUMBER)
            throw new ProtocolException("Bad  request.");

        //1. 读取协议长度
        int len = buffer.getInt();
        if (buffer.remaining() < len)
            return null;

        //2. 识别协议版本
        byte version = buffer.get();
        if (version == LUCARE_PROTOCOL_VERSION_1_0) {
            return ProtocolVersion1.decodeRequest(buffer, len, version);
        } else {
            throw new UnsupportedOperationException("Unsupported version=" + version);
        }
    }

}
