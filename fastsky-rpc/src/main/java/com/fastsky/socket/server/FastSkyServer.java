package com.fastsky.socket.server;

import com.fastsky.bean.BeanRegister;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: FastSkyServer
 * Description: rcp的服务端
 *
 * @author: starryfei
 * @date: 2019-01-22 15:26
 **/
public class FastSkyServer {
    private static ExecutorService pool;
    private static BeanRegister beanRegister = BeanRegister.getInstance();
    /**
     * 启动服务
     */
    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(9122);
            System.out.println("server start success");
            while (true) {
                Socket socket = serverSocket.accept();
                ServerThreadFactory server = new ServerThreadFactory(socket);
                pool = new ThreadPoolExecutor(5, 200,
                        60L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>(1024), server, new ThreadPoolExecutor.AbortPolicy());

                pool.execute(()-> {
                    System.out.println(Thread.currentThread().getName());
                    invoke(socket);

                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 执行方法
     * @param socket
     */
    private void invoke(Socket socket){
        Object object = null;
        try {
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            String className = input.readUTF();
            String methodName = input.readUTF();
            Class[] parTypes = (Class[])input.readObject();
            Class[] args = (Class[]) input.readObject();

            Class<?> clazz = beanRegister.getBean(className);
            object = clazz.getMethod(methodName,parTypes).invoke(clazz.newInstance(),args);
            out.writeObject(object);

            out.flush();
            input.close();
            out.close();
            socket.close();

        } catch (IOException | ClassNotFoundException | InstantiationException |
                IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }



    /**
     * 关闭服务
     */
    public static void shutDown() {

        pool.shutdown();//gracefully shutdown
    }

}
