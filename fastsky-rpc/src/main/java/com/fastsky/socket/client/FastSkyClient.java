package com.fastsky.socket.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

/**
 * ClassName: FastSkyClient
 * Description: fastsky的客户端
 *
 * @author: starryfei
 * @date: 2019-01-22 16:17
 **/
public class FastSkyClient {
    private static Socket socket;

    /**
     * 建立与服务端的连接
     */
    public void start() {
        try {
            socket = new Socket("127.0.0.1",9122);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取接口的代理对象
     * @param clazz
     * @return
     */
    public Object getBean(Class<?> clazz) {

        return Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                ObjectOutputStream out = writeData(clazz, method, args);
                Object obj = readData();
                out.close();
                return obj;
            }
        });
    }

    /**
     * 向服务端发送参数
     * @param cla
     * @param method
     * @param args
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private ObjectOutputStream writeData(Class<?> cla, Method method, Object[] args) throws IOException, ClassNotFoundException {
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeUTF(cla.getName());
        out.writeUTF(method.getName());
        out.writeObject(method.getParameterTypes());
        out.writeObject(args);
        out.flush();
        return out;

//        out.close();
    }

    /**
     * 从服务端读取参数
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private Object readData() throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        Object object = inputStream.readObject();
        inputStream.close();
        return object;
    }

    /**
     * 结束客户端
     */
    public void destroy(){

        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
