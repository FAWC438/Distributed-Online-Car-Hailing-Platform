package cn.bupt.driverhailingserver.server;


import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;


@Component
@ServerEndpoint(value = "/websocket/{username}")
public class WebSocketServer {
    //    static Log log = InternalLoggerFactory.getInstance(WebSocketServer.class)
    //    public static Map<String, WebSocketServer> webSocketServerMap = new ConcurrentHashMap<~>();
    private String userName;
    //    private static Logger log = Logger.getLogger(String.valueOf(WebSocketServer.class));
    private Session session;
    private Session id;
    private static int onlineCount = 0;
    private static final CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();
    private static final ConcurrentHashMap<Long, WebSocketServer> websocketMap = new ConcurrentHashMap<>();
    private Long userId = 0L;

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Long userId) {


        this.session = session;
        this.userId = userId;
        if (websocketMap.containsKey(userId)) {
            websocketMap.remove(userId);
            websocketMap.put(userId, this);
            //加入set中
        } else {
            websocketMap.put(userId, this);
            //加入set中
            addOnlineCount();
            //在线数加1
        }

        System.out.println("用户连接:" + userId + ",当前在线人数为:" + getOnlineCount());

        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            System.out.println("用户:" + userId + ",网络异常!!!!!!");
        }
    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
//        subOnlineCount();           //在线数减1
//        webSocketSet.remove(this);  //从set中删除
//        websocketMap.remove(session);
//        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());\
        if (websocketMap.containsKey(userId)) {
            websocketMap.remove(userId);
            //从set中删除
            subOnlineCount();
        }
        System.out.println("用户退出:" + userId + ",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
        try {
            sendInfo(message, userId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }


    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }

    public void sendInfo(String message, @PathParam("userId") Long userId) throws IOException {
        System.out.println("发送消息到:" + userId + "，报文:" + message);
        if (websocketMap.get(userId) != null) {
            websocketMap.get(userId).sendMessage("用户" + session.getId() + "发来消息：" + message);
        } else {
            //如果用户不在线则返回不在线信息给自己
//            sendMessageToUser("当前用户不在线", id);
            System.out.println("用户" + userId + ",不在线！");
        }

    }

    /*
     *
     * @param message
     */
    public void sendMessageToAll(String message) {
        for (WebSocketServer item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    /**
//     * 群发自定义消息
//     */
//    public static void sendInfo(String message) throws IOException {
//        for (WebSocketServer item : webSocketSet) {
//            try {
//                item.sendMessage(message);
//            } catch (IOException e) {
//                continue;
//            }
//        }
//    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
