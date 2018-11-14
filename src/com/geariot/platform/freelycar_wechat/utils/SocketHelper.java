package com.geariot.platform.freelycar_wechat.utils;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author 唐炜
 */
@Component
@ServerEndpoint("/echo")
public class SocketHelper {

	private static Set<Session> peers = Collections.synchronizedSet(new HashSet());

	@OnOpen
	public void onOpen(Session session) {
		peers.add(session);
	}

	@OnMessage
	public void onMessage(String message,Session session) {
		try {
			for (Session s : peers) {
				if(session == s){
					s.getBasicRemote().sendText("{msg:\"推送成功\"}");
				}else{
					s.getBasicRemote().sendText(message);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@OnClose
	public void onClose(Session session) {
		peers.remove(session);
	}
}
