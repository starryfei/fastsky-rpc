package com.fastsky.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.*;

/**
 * ClassName: FastSkyServer
 * Description: rcp的服务端
 *
 * @author: starryfei
 * @date: 2019-01-22 15:26
 **/
public class FastSkyServer {
    private static ExecutorService pool;
    private Map<String,Class<?>> classMap ;

    /**
     * 启动服务
     */
    public  void start() {
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

            Class<?> clazz = classMap.get(className);
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
     * 注册服务
     * @param clazz
     */
    public void registerBeans(Class<?> clazz) {
        if (classMap == null) {
            classMap = new ConcurrentHashMap<>(16);
        }
        for (Class<?> cla : clazz.getInterfaces()) {
            System.out.println(cla.getName());
            classMap.put(cla.getName(), clazz);
        }

    }

    /**
     * 关闭服务
     */
    public static void shutDown() {

        pool.shutdown();//gracefully shutdown
    }

}
