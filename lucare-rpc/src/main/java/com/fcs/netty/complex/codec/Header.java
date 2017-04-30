package com.fcs.netty.complex.codec;

/**
 * Created by Lucare.Feng on 2017/4/30.
 * 协议头部
 */
public class Header {

    private byte magic = 'F';
    private byte version = 0x10;
    private int length;
//    private String sessionId;


    public Header() {
    }

    public Header(byte magic, byte version, int length) {
        this.magic = magic;
        this.version = version;
        this.length = length;
    }

    public byte getMagic() {
        return magic;
    }

    public void setMagic(byte magic) {
        this.magic = magic;
    }

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    //    public String getSessionId() {
//        return sessionId;
//    }
//
//    public void setSessionId(String sessionId) {
//        this.sessionId = sessionId;
//    }
}
