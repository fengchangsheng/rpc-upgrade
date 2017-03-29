package com.fcs.bio.complex;

/**
 * Created by Lucare.Feng on 2017/3/28.
 */
public class ServerConfig {


    private String host = "127.0.0.1" ;

    private int tcpPort = 12580 ;

    public ServerConfig(String host, int tcpPort) {
        this.host = host;
        this.tcpPort = tcpPort;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }
}
