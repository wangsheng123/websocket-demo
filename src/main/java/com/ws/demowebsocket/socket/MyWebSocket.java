package com.ws.demowebsocket.socket;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/webSocket")
public class MyWebSocket {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);//加入set中
        addOnlineCount();//当前在线连接数  +1
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
        // System.out.println("来自"+request.getSession()+"======="+request.getRequestedSessionId());
    }

    @OnClose
    public void onClose() {
        webSocketSet.remove(this);//移除
        subOnlineCount();//当前在线连接数  -1
        System.out.println("断开一个连接！当前在线人数为" + getOnlineCount());
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("来自客户端的消息:" + message);
        for (MyWebSocket socket :
                webSocketSet) {
            sendMessage(message);
        }
    }

    @OnMessage
    public void onMessage2(ByteBuffer bytes, Session session) throws IOException {
        System.out.println("来自客户端的消息:" + bytes);
        System.out.println(session.getContainer());
       /* for (MyWebSocket socket :
                webSocketSet) {
            sendMessage(message);
        }*/
        byteToFile(bytes.array(), "C:\\Users\\Administrator\\Desktop\\down.png");
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生异常");
        error.printStackTrace();
    }

    public static synchronized void addOnlineCount() {
        MyWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        MyWebSocket.onlineCount--;
    }

    public static synchronized int getOnlineCount() {
        return MyWebSocket.onlineCount;
    }

    public void sendMessage(String message) throws IOException {
     this.session.getBasicRemote().sendText(message);

       /* this.session.getAsyncRemote()
                .sendBinary(ByteBuffer.wrap(InputStream2ByteArray("C:\\Users\\Administrator\\Desktop\\新建文本文档.txt")));*/
    }

    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message) throws IOException {
        for (MyWebSocket socket :
                webSocketSet) {
            socket.sendMessage(message);
        }

    }

    private byte[] InputStream2ByteArray(String filePath) throws IOException {

        InputStream in = new FileInputStream(filePath);
        byte[] data = toByteArray(in);
        in.close();

        return data;
    }

    private byte[] toByteArray(InputStream in) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    private String byteToFile(byte[] bytes, String filePath) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        fileOutputStream.write(bytes);
        fileOutputStream.flush();
        return filePath;
    }
}
