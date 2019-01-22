package com.fastsky.server;

import java.net.Socket;
import java.util.concurrent.ThreadFactory;

/**
 * ClassName: ServerThreadFactory
 * Description: TODO
 *
 * @author: starryfei
 * @date: 2019-01-22 15:37
 **/
public class ServerThreadFactory implements ThreadFactory {
    private Socket socket;
    public ServerThreadFactory(Socket socket){
        this.socket = socket;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(socket.getInetAddress().getHostName());
        return thread;
    }
}
