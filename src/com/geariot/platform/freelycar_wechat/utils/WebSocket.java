package com.geariot.platform.freelycar_wechat.utils;

import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 唐炜
 */
@Component
@ServerEndpoint("/webSocket/{username}")
public class WebSocket {
    private static int onlineCount = 0;
    private static Map<String, WebSocket> clients = new ConcurrentHashMap<String, WebSocket>();
    private Session session;
    private String username;

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocket.onlineCount--;
    }

    public static synchronized Map<String, WebSocket> getClients() {
        return clients;
    }

    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) throws IOException {

        this.username = username;
        this.session = session;

        addOnlineCount();
        clients.put(username, this);
        System.out.println(username + "已连接");
    }

    @OnClose
    public void onClose() throws IOException {
        System.out.println(username + "已关闭连接");
        clients.remove(username);
        subOnlineCount();
    }

    @OnMessage
    public void onMessage(String message) throws IOException {

        JSONObject jsonTo = JSONObject.fromObject(message);
        String mes = (String) jsonTo.get("message");

        if (!jsonTo.get("to").equals("All")) {
            sendMessageTo(mes, jsonTo.get("to").toString());
        } else {
            sendMessageAll(mes);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 向指定名称的客户端发送信息
     * @param message
     * @param to
     * @throws IOException
     */
    public void sendMessageTo(String message, String to) throws IOException {
        for (WebSocket item : clients.values()) {
            if (item.username.equals(to)) {
                this.sendText(item.session,message);
            }
        }
    }

    /**
     * 向所有链接的客户端发送信息
     * @param message
     * @throws IOException
     */
    public void sendMessageAll(String message) throws IOException {
        for (WebSocket item : clients.values()) {
            this.sendText(item.session,message);
        }
    }

    /**
     * 向对应用户发送消息（加方法锁，避免异常）
     * 异常信息：java.lang.IllegalStateException: The remote endpoint was in state [TEXT_FULL_WRITING] which is an invalid state for called method
     * @param webSocketSession
     * @param message
     */
    public synchronized void sendText(Session webSocketSession, String message) {
        webSocketSession.getAsyncRemote().sendText(message);
    }
}
