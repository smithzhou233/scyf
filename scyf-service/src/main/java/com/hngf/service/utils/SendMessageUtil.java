package com.hngf.service.utils;

import com.hngf.service.utils.jpush.JPushKit;
import com.hngf.entity.sys.Message;
import com.hngf.entity.sys.User;
import com.hngf.service.sys.MessageService;
import com.hngf.service.sys.SettingService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息推送
 * @author zhangfei
 * @date 2020-06-11
 */
public class SendMessageUtil {
    public static void sendMessage(Long companyId, String title, Long addresseeId, String content, MessageService messageDao, SettingService settingService) throws Exception {
        Message message = new Message();
        message.setMsgTitle(title);
        message.setMsgContent(content);
        message.setCompanyId(companyId);
        message.setAddresseeId(addresseeId);
        message.setMsgType(1);
        message.setMsgStatus(0);
        message.setPushType(2);
        message.setAddresseeTime(new Date());
        messageDao.save(message);

        //TODO 极光推送的部分先注释掉
        if (settingService != null) {
            Map<String, String> extras = new HashMap();
            extras.put("msgId", String.valueOf(message.getMsgId()));
            extras.put("title", title);
            extras.put("body", content);
           JPushKit.sendPush(settingService, title,content,String.valueOf( addresseeId), extras);
        }

    }

    public static void sendChatMessage(Integer msgId, String title, Long addresseeId, String content, SettingService settingsService) throws Exception {
        /*if (settingsService != null) {
            Map<String, String> extras = new HashMap();
            extras.put("msgId", String.valueOf(msgId));
            extras.put("title", title);
            extras.put("body", content);
            JPushKit.sendPush(settingsService, title, addresseeId, extras);
        }*/

    }

    public static void sendAllChatMessage(Integer msgId, String title, List<Long> aId, String content, SettingService settingsService) throws Exception {
        /*if (settingsService != null) {
            Map<String, String> extras = new HashMap();
            extras.put("msgId", String.valueOf(msgId));
            extras.put("title", title);
            extras.put("body", content);
            JPushKit.sendPush(settingsService, title, extras, aId);
        }*/

    }

    public static void sendAllChatMessage(Integer msgId, String title, List<Long> aId, String content, SettingService settingsService, int flag) throws Exception {
       /* if (settingsService != null) {
            Map<String, String> extras = new HashMap();
            extras.put("msgId", String.valueOf(msgId));
            extras.put("title", title);
            extras.put("body", content);
            JPushKit.sendPush(settingsService, title, extras, aId, flag);
        }*/

    }

    public static void sendBigScreen(Long companyId, Long groupId, String path) {
        WsClientKit.sendWs(companyId, groupId, path);
    }

    public static void sendBigScreen(User user, String path) {
        WsClientKit.sendWs(user.getCompanyId(), user.getGroupId(), path);
    }

    public static void sendRongYunChatMessage(Integer userId, String contents) throws Exception {
        /*RongCloud rongCloud = RongCloud.getInstance("sfci50a7s30ki", "Ty2oz7Mx0sJy7");
        PushModel pushmodel = new PushModel();
        pushmodel.setPlatform(new String[]{"ios", "android"});
        Audience audience = new Audience();
        audience.setUserid(new String[]{userId + ""});
        pushmodel.setAudience(audience);
        Notification notification = new Notification();
        notification.setAlert("this is push");
        pushmodel.setNotification(notification);
        rongCloud.push.push(pushmodel);*/
    }
}
