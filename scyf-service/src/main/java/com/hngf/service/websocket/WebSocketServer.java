package com.hngf.service.websocket;


import cn.hutool.core.collection.ConcurrentHashSet;
import com.hngf.service.bigscreen.BigScreenMessageService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * websocket 服务端 - 使用javax.websocket.server.ServerEndpoint注解
 */
@ServerEndpoint("/ws/{companyId}/{groupId}/{apiFrom}")
@Component
public class WebSocketServer{

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static BigScreenMessageService bigScreenMessageService;

    @Autowired
    private void setBigScreenMessageService(BigScreenMessageService bigScreenMessageService) {
        WebSocketServer.bigScreenMessageService = bigScreenMessageService;
    }

    //建一个线程安全map，用来存放相同companyId和groupID的session。key为公司ID，value是一个Set集合，用来解决同一个公司多个大屏同时链接的问题
    private static Map<String, ConcurrentHashSet<Session>> sessionPool =new ConcurrentHashMap<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("companyId")Long companyId,@PathParam("groupId")Long groupId,@PathParam("apiFrom")String apiFrom) {
        this.session = session;
        String key = companyId + "-" + groupId;
        //从map获取companyId下的session，如果为null，创建一个ConcurrentHashSet将当前的session放入set中
        ConcurrentHashSet<Session> set = sessionPool.get(key);
        if (null == set)
            set = new ConcurrentHashSet<>();

        //如果apiFrom不等于 client，将session放入set中。client是WsClientKit发送的请求
        if(!"client".equals(apiFrom))
            set.add(session);
        sessionPool.put(key,set);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("companyId")Long companyId,@PathParam("groupId")Long groupId) {
        ConcurrentHashSet<Session> set = sessionPool.get(companyId + "-" + groupId);
        if (null != set) {
            set.remove(this);
        }
        logger.info("有一连接关闭");
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("companyId")Long companyId,@PathParam("groupId")Long groupId,@PathParam("apiFrom")String apiFrom) {
        logger.info("来自客户端的消息:" + message);
        if (StringUtils.isNotBlank(message)) {
            String path = message.split("-")[0];

            Map<String, Object> params = new HashMap<>();
            params.put("path", path);
            //遍历key，拆解出companyId和groupId，查询数据并返回
            for (String key : sessionPool.keySet()) {
                if (StringUtils.isNotBlank(key)) {
                    String[] keyary = key.split("-");

                    params.put("companyId", keyary[0]);
                    params.put("groupId", keyary[1]);

                    try {
                        String res = bigScreenMessageService.bigScreenMessage(params);
                        this.sendMessage(res,key);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("大屏websocket服务发送消息失败",e.getMessage());
                    }
                }
            }
        }

    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 自定义方法 - 根据公司ID发送消息
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message,String key) {
        ConcurrentHashSet<Session> set = sessionPool.get(key);
        if (null != set) {
            set.stream().filter(s -> null != s && s.isOpen()).forEach(s -> {
                try {
                    s.getAsyncRemote().sendText(message);
                    logger.info("【websocket消息】 单点消息:" + message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
        }
    }
}
