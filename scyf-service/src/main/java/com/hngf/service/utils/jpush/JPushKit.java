package com.hngf.service.utils.jpush;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.connection.HttpProxy;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.AndroidNotification.Builder;
import cn.jpush.api.push.model.notification.IosAlert;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;


import com.hngf.entity.sys.Setting;
import com.hngf.service.sys.SettingService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JPushKit {
    private static final Logger log = LoggerFactory.getLogger(JPushKit.class);

    protected static final String APP_KEY = "23a1003b27fd994d10493c09";
    protected static final String MASTER_SECRET = "9b82c06a5a400a1a54527a1e";

    public JPushKit() {
    }

    public static void main(String[] args) {
        Map<String, String> extras = new HashMap();
        extras.put("id", "123");
        extras.put("title", "123");
        extras.put("body", "123");
        String registrationId = "";
        sendPush("23a1003b27fd994d10493c09", "9b82c06a5a400a1a54527a1e", "小雷。。。。。。。","123", registrationId, extras);
    }

    public static Integer sendPush(SettingService settingsService, String alert, Map<String, String> extras, List<Long> aId) {
        Setting appKey = settingsService.selectByPrimaryKey("jpush_app_key");
        Setting masterSecret = settingsService.selectByPrimaryKey("jpush_master_secret");
        return appKey != null && masterSecret != null ? sendPushList(appKey.getSettingValue(), masterSecret.getSettingValue(), alert, extras, aId) : 0;
    }
    public static Integer sendPush(SettingService settingsService, String alert, Map<String, String> extras, List<Long> aId, int flag) {
        Setting appKey;
        Setting masterSecret;
        if (flag == 1) {
            appKey = settingsService.selectByPrimaryKey("org_app_key");
            masterSecret = settingsService.selectByPrimaryKey("org_app_master_secret");
        } else {
            appKey = settingsService.selectByPrimaryKey("group_app_key");
            masterSecret = settingsService.selectByPrimaryKey("group_app_master_secret");
        }

        return appKey != null && masterSecret != null ? sendPushList(appKey.getSettingValue(), masterSecret.getSettingValue(), alert, extras, aId, flag) : 0;
    }

    public static Integer sendPushList(String appKey, String masterSecret, String alert, Map<String, String> extras, List<Long> aId, int flag) {
        ClientConfig clientConfig = ClientConfig.getInstance();
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, (HttpProxy)null, clientConfig);
        PushPayload payload = null;

        try {
            if (aId != null && extras != null) {
                return sendPushAIdList(jpushClient, aId, alert, extras, flag);
            }

            if (StrUtil.isNotEmpty(alert) && extras != null) {
                payload = buildPushObject(alert, extras);
            } else {
                payload = buildPushObject(alert);
            }

            PushResult result = jpushClient.sendPush(payload);
            int status = result.getResponseCode();
            if (status == 200) {
                log.info("推送成功 - " + result);
                return 1;
            }

            log.info("Got result - " + result);
        } catch (APIConnectionException var11) {
            log.error("Connection error. Should retry later. ", var11);
            log.error("Sendno: " + payload.getSendno());
        } catch (APIRequestException var12) {
            log.error("Error response from JPush server. Should review and fix it. ", var12);
            log.info("HTTP Status: " + var12.getStatus());
            log.info("Error Code: " + var12.getErrorCode());
            log.info("Error Message: " + var12.getErrorMessage());
            log.info("Msg ID: " + var12.getMsgId());
            log.error("Sendno: " + payload.getSendno());
        }

        return 0;
    }

    public static Integer sendPushAIdList(JPushClient jpushClient, List<Long> aId, String alert, Map<String, String> extras, int flag) throws APIConnectionException, APIRequestException {
        if (!CollectionUtil.isNotEmpty(aId)) {
            return 0;
        } else {
            StringBuilder prefix = new StringBuilder();
            if (flag == 1) {
                prefix.append("SCYF");
            } else {
                prefix.append("EP");
            }

            int totalSize = aId.size();
            int mode = totalSize % 1000;
            int sendTimes = totalSize / 1000;
            if (mode > 0) {
                ++sendTimes;
            }

            List<String> accountIds = new ArrayList();

            for(int i = 0; i < sendTimes; ++i) {
                int start = i * 1000;
                int end;
                if (i == sendTimes - 1 && mode > 0) {
                    end = start + mode - 1;
                } else {
                    end = start + 1000 - 1;
                }

                for(int j = start; j <= end; ++j) {
                    accountIds.add(prefix + ((Long)aId.get(j)).toString());
                }

                PushPayload payload = buildPushObject((List)accountIds, alert, extras);
                PushResult result = jpushClient.sendPush(payload);
                int status = result.getResponseCode();
                if (status == 200) {
                    log.info("推送成功 - " + result);
                } else {
                    log.info("推送失败 - " + result);
                }

                accountIds.clear();
            }

            return 1;
        }
    }

    public static Integer sendPushList(String appKey, String masterSecret, String alert, Map<String, String> extras, List<Long> aId) {
        ClientConfig clientConfig = ClientConfig.getInstance();
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, (HttpProxy)null, clientConfig);
        PushPayload payload = null;

        try {
            if (aId != null && extras != null) {
                return sendPushAIdList(jpushClient, aId, alert, extras);
            }

            if (StrUtil.isNotEmpty(alert) && extras != null) {
                payload = buildPushObject(alert, extras);
            } else {
                payload = buildPushObject(alert);
            }

            PushResult result = jpushClient.sendPush(payload);
            int status = result.getResponseCode();
            if (status == 200) {
                log.info("推送成功 - " + result);
                return 1;
            }

            log.info("Got result - " + result);
        } catch (APIConnectionException var10) {
            log.error("Connection error. Should retry later. ", var10);
            log.error("Sendno: " + payload.getSendno());
        } catch (APIRequestException var11) {
            log.error("Error response from JPush server. Should review and fix it. ", var11);
            log.info("HTTP Status: " + var11.getStatus());
            log.info("Error Code: " + var11.getErrorCode());
            log.info("Error Message: " + var11.getErrorMessage());
            log.info("Msg ID: " + var11.getMsgId());
            log.error("Sendno: " + payload.getSendno());
        }

        return 0;
    }

    public static Integer sendPushAIdList(JPushClient jpushClient, List<Long> aId, String alert, Map<String, String> extras) throws APIConnectionException, APIRequestException {
        final  List<String> accountIds = new ArrayList();
        PushPayload payload;
        PushResult result;
        int status;
        if (aId.size() < 1000) {
            aId.forEach((e) -> {
                accountIds.add("push".concat(e.toString()));
            });
            payload = null;
            payload = buildPushObject((List)accountIds, alert, extras);
            result = jpushClient.sendPush(payload);
            status = result.getResponseCode();
            if (status == 200) {
                log.info("推送成功 - " + result);
                return 1;
            }

            log.info("Got result - " + result);
        } else {
            for(int i = 0; i < aId.size(); ++i) {
                accountIds.add("push".concat(((Long)aId.get(i)).toString()));
                if (i % 1000 == 0) {
                    payload = null;
                    payload = buildPushObject((List)accountIds, alert, extras);
                    result = jpushClient.sendPush(payload);
                    status = result.getResponseCode();
                    if (status == 200) {
                        log.info("推送成功 - " + result);
                    } else {
                        log.info("Got result - " + result);
                    }

                    accountIds.removeAll(accountIds);
                }
            }
        }

        return 0;
    }

    public static PushPayload buildPushObject(List<String> accountIds, String alert, Map<String, String> extras) {
        IosAlert iosAlert = IosAlert.newBuilder().setTitleAndBody((String)extras.get("title"), (String)null, (String)extras.get("body")).build();
        extras.remove("title");
        extras.remove("body");
        return PushPayload.newBuilder().setPlatform(Platform.android_ios()).setAudience(Audience.alias(accountIds)).setNotification(Notification.newBuilder().addPlatformNotification(((Builder)AndroidNotification.newBuilder().setAlert(alert).setTitle(alert).addExtras(extras)).build()).addPlatformNotification(((IosNotification.Builder)IosNotification.newBuilder().setAlert(iosAlert).addExtras(extras)).setSound("default").build()).build()).setOptions(Options.newBuilder().setApnsProduction(false).setTimeToLive(86000L).build()).build();
    }

    public static Integer sendPush(SettingService settingsService, String alert,String content, String aId, Map<String, String> extras) {
        Setting appKey = settingsService.selectByPrimaryKey("jpush_app_key");
        Setting masterSecret = settingsService.selectByPrimaryKey("jpush_master_secret");
        return appKey != null && masterSecret != null ? sendPush(appKey.getSettingValue(), masterSecret.getSettingValue(),content, alert,  aId , extras) : 0;
    }
    public static Integer sendPush(String appKey, String masterSecret, String alert,String content, String aId, Map<String, String> extras) {
        ClientConfig clientConfig = ClientConfig.getInstance();
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, (HttpProxy)null, clientConfig);
        PushPayload payload;
        if (StrUtil.isNotEmpty(alert) && StrUtil.isNotEmpty(aId) && extras != null) {
            payload = buildPushObject(aId, alert,content, extras);
        } else if (StrUtil.isNotEmpty(alert) && extras != null) {
            payload = buildPushObject(alert, extras);
        } else {
            payload = buildPushObject(alert);
        }

        try {
            PushResult result = jpushClient.sendPush(payload);
            int status = result.getResponseCode();
            if (status == 200) {
                log.info("推送成功 - " + result);
                return 1;
            }

            log.info("Got result - " + result);
        } catch (APIConnectionException var10) {
            log.error("Connection error. Should retry later. ", var10);
            log.error("Sendno: " + payload.getSendno());
        } catch (APIRequestException var11) {
            log.error("Error response from JPush server. Should review and fix it. ", var11);
            log.info("HTTP Status: " + var11.getStatus());
            log.info("Error Code: " + var11.getErrorCode());
            log.info("Error Message: " + var11.getErrorMessage());
            log.info("Msg ID: " + var11.getMsgId());
            log.error("Sendno: " + payload.getSendno());
        }

        return 0;
    }

    public static PushPayload buildPushObject(String accountId, String alert,String content, Map<String, String> extras) {
        IosAlert iosAlert = IosAlert.newBuilder().setTitleAndBody((String)extras.get("title"), (String)null, (String)extras.get("body")).build();
        extras.remove("title");
        extras.remove("body");
        return PushPayload.newBuilder().setPlatform(Platform.android_ios()).setAudience(Audience.alias(accountId.split(","))).setNotification(Notification.newBuilder().addPlatformNotification(((Builder) AndroidNotification.newBuilder().setAlert(alert).setTitle(content).addExtras(extras)).build()).addPlatformNotification(((IosNotification.Builder) IosNotification.newBuilder().setAlert(iosAlert).addExtras(extras)).setSound("default").build()).build()).setOptions(Options.newBuilder().setApnsProduction(false).setTimeToLive(86000L).build()).build();
       // return PushPayload.newBuilder().setPlatform(Platform.android_ios()).setAudience(Audience.registrationId((accountId))).setNotification(Notification.newBuilder().addPlatformNotification(((Builder) AndroidNotification.newBuilder().setAlert(alert).setTitle(alert).addExtras(extras)).build()).addPlatformNotification(((cn.jpush.api.push.model.notification.IosNotification.Builder) IosNotification.newBuilder().setAlert(iosAlert).addExtras(extras)).setSound("default").build()).build()).setOptions(Options.newBuilder().setApnsProduction(false).setTimeToLive(86000L).build()).build();
    }

    public static PushPayload buildPushObject(String alert) {
        return PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.all()).setNotification(Notification.alert(alert)).setOptions(Options.newBuilder().setApnsProduction(false).setTimeToLive(86000L).build()).build();
    }

    public static PushPayload buildPushObject(String alert, Map<String, String> extras) {
        return PushPayload.newBuilder().setPlatform(Platform.android_ios()).setAudience(Audience.all()).setNotification(Notification.newBuilder().addPlatformNotification(((Builder)AndroidNotification.newBuilder().setAlert(alert).setTitle(alert).addExtras(extras)).build()).addPlatformNotification(((IosNotification.Builder)IosNotification.newBuilder().setAlert(alert).addExtras(extras)).setSound("default").build()).build()).setOptions(Options.newBuilder().setApnsProduction(false).setTimeToLive(86000L).build()).build();
    }
    private ClientConfig config = ClientConfig.getInstance();
    private JPushClient client = new JPushClient(MASTER_SECRET, APP_KEY, null, config);



    public void pushAline(){
           String registrationId = "100d855909cf1f340c0";
        //        sendPush("23a1003b27fd994d10493c09", "9b82c06a5a400a1a54527a1e", "小雷。。。。。。。", registrationId, extras);
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.android())//推送平台
                .setAudience(Audience.alias(registrationId))//推送目标
                .setNotification(Notification.newBuilder().addPlatformNotification(AndroidNotification.newBuilder().
                        setAlert("小雷。。。。。。。").setTitle("哈哈哈哈").build()).build())//通知消息（标题，内容）
                .setOptions(Options.newBuilder().setApnsProduction(true).setTimeToLive(86400).build()//离线消息保存时间
                )
                .build();
        try {
            PushResult result = client.sendPush(payload);//推送
            System.out.println("推送结果：" + result.getResponseCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
