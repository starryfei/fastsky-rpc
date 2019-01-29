### [fastsky-rpc](https://starryfei.github.io/fastsky-rpc/) 基于socket和netty实现的rpc服务简单框架

#### fastsky-rpc-v1.0实现的功能

- 1 基于socket实现rpc
- 2 基于netty实现

v1.1预计实现
- ZooKeeper：提供服务注册与发现功能
- 整合Spring



#### fastsky-rpc 使用

#### 基于socket使用

- 1 定义服务接口
```java
public interface HelloService {

    String sayHello();
}

```
- 2 服务提供者
```java
public class HelloServiceImpl implements HelloService {

    public String sayHello() {
        return "Hello World";
    }
}
``` 

```java
import com.fastsky.bean.BeanRegister;
import com.fastsky.socket.server.FastSkyServer;
import com.starry.service.impl.HelloServiceImpl;

public static void main(String[] args){
    FastSkyServer skyServer = new FastSkyServer();
    // 注册服务        
    BeanRegister.getInstance().registerBeans(HelloServiceImpl.class);
    skyServer.start();

    }
```
- 3 服务消费者
```java
import com.fastsky.socket.client.FastSkyClient;
import com.starry.service.HelloService;

public static void main(String[] args) {
    FastSkyClient client = new FastSkyClient();
    client.start();
    HelloService helloService = (HelloService) client.getBean(HelloService.class);
    String hellp = helloService.sayHello();
    System.out.println(hellp);
    client.destroy();
}

```
---
#### 基于netty实现 [参考](https://my.oschina.net/huangyong/blog/361751 "参考")

- 1 服务提供者
```java
import com.fastsky.api.HelloServiceImpl;
import com.fastsky.bean.BeanRegister;

public static void main(String[] args) {
    // 注册服务    
    BeanRegister.getInstance().registerBeans(HelloServiceImpl.class);
    FastSkyNettyServer server = new FastSkyNettyServer();
    server.start(9122);
}
```
- 2 服务消费者
```java
import com.fastsky.api.HelloService;

public static void main(String[] args) {
    FastSkyNettyClient client = new FastSkyNettyClient();
    HelloService service = (HelloService) client.getBean(HelloService.class);
    System.out.println(service.sayHello());
}

```
