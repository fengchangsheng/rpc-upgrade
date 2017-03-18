package com.fcs.nio.simple;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * Created by Lucare.Feng on 2017/3/18.
 * 事件分发器接口
 */
public interface Handler {

    /**
     * 处理{@link SelectionKey#OP_ACCEPT}事件
     * @param key
     * @throws IOException
     */
    void handleAccept(SelectionKey key) throws IOException;
    /**
     * 处理{@link SelectionKey#OP_READ}事件
     * @param key
     * @throws IOException
     */
    void handleRead(SelectionKey key) throws IOException, ClassNotFoundException, NoSuchMethodException;
    /**
     * 处理{@link SelectionKey#OP_WRITE}事件
     * @param key
     * @throws IOException
     */
    void handleWrite(SelectionKey key) throws IOException;

}
