package com.qjc.midjourney.event;

import cn.hutool.core.text.CharSequenceUtil;
import com.alibaba.fastjson.JSONObject;
import com.qjc.midjourney.config.MidjourneyBotConfiguration;
import com.qjc.midjourney.dao.OrderDao;
import com.qjc.midjourney.dto.Order;
import com.qjc.midjourney.enums.Scene;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * The type DiscordEventHandler event trigger .
 *
 * @author zw
 */
@Component
@Slf4j
public class DiscordEventHandler extends ListenerAdapter {

    private static final String STRING_RANDOM = "0123456789";

    private static final int SIZE = 4;

    private final MidjourneyBotConfiguration midjourneyBotConfiguration;
    private final WebClient webClient;
    private final OrderDao orderDao;

    @Autowired
    public DiscordEventHandler(MidjourneyBotConfiguration midjourneyBotConfiguration, WebClient.Builder webClientBuilder, OrderDao orderDao) {
        this.midjourneyBotConfiguration = midjourneyBotConfiguration;
        this.webClient = webClientBuilder.build();
        this.orderDao = orderDao;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();
        String messageId = message.getId();
        String content = message.getContentDisplay();
        MessageChannel channel = event.getChannel();
        User author = event.getAuthor();

        if (!channel.getId().equals(midjourneyBotConfiguration.getDiscordConfig().getChannelId()) ||
                author.getId().equals(event.getJDA().getSelfUser().getId())) {
            return;
        }

        if (message.getContentRaw().contains("(Waiting to start)") && !message.getContentRaw().contains("Rerolling **")) {
            log.info("first request");
            return;
        }

        for (Message.Attachment attachment : message.getAttachments()) {
            if (attachment.isImage()) {
                replay(attachment, messageId, content);
                return;
            }
        }
    }

    @Override
    public void onMessageUpdate(MessageUpdateEvent event) {
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        User author = event.getAuthor();

        if (!channel.getId().equals(midjourneyBotConfiguration.getDiscordConfig()
                .getChannelId()) || author.getId().equals(event.getJDA().getSelfUser().getId())) {
            return;
        }

        if (message.getContentRaw().contains("(Stopped)")) {
//            trigger(message.getContentRaw(), Scene.GENERATE_EDIT_ERROR);
        }
    }

    public static void main(String[] args) {
        System.out.println("\u7ec4\u4ef6\u6838\u9a8c\u5931\u8d25");
    }

    private void replay(Message.Attachment attachment, String messageId, String content) {
      /*  //获取服务器（公会）信息
        Guild guild = message.getGuild();
        String guildId = guild.getId();
        String guildName = guild.getName();

        System.out.println("guildId = " + guildId);//输出服务器（公会）id
        System.out.println("guildName = " + guildName);//输出服务器（公会）名称

        //获取频道信息
        MessageChannel channel = message.getChannel();
        String channelId = channel.getId();
        String channelName = channel.getName();
        ChannelType channelType = channel.getType();
        System.out.println("channelId = " + channelId);//输出频道id
        System.out.println("channelName = " + channelName);//输出频道名称
        System.out.println("channelType = " + channelType);//输出频道类型

        //获取发消息人
        Member member = message.getMember();
        String memberId = member.getId();
        String memberNickname = member.getNickname();
        String memberEffectiveName = member.getEffectiveName();
        List<Role> memberRoles = member.getRoles();
        EnumSet<Permission> memberPermissions = member.getPermissions();
        OnlineStatus memberOnlineStatus = member.getOnlineStatus();
        System.out.println("memberId = " + memberId);//输出发送消息人的id
        System.out.println("memberNickname = " + memberNickname);//输出发送消息人的昵称
        System.out.println("memberEffectiveName = " + memberEffectiveName);//输出发送消息人的有效名称
        System.out.println("memberRoles = " + memberRoles);//输出发送消息人的角色列表
        System.out.println("memberPermissions = " + memberPermissions);//输出发送消息人的权限列表
        System.out.println("memberOnlineStatus = " + memberOnlineStatus);//输出发送消息人的在线状态

        //获取消息信息
        String messageId = message.getId();
        String messageContentRaw = message.getContentRaw();
        MessageType messageType = message.getType();
        System.out.println("messageId = " + messageId);//输出消息id
        System.out.println("messageContentRaw = " + messageContentRaw);//输出消息内容
        System.out.println("messageType = " + messageType);//输出消息类型*/
//截取  文件名称  hrldaiz_Cute_Magical_Flying_Dogs_fantasy_art_drawn_by_Disney_co_450bfebf-ff51-4ed3-83ec-273c665f4414.png

        com.alibaba.fastjson.JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", attachment.getId());
        jsonObject.put("url", attachment.getUrl());
        jsonObject.put("proxyUrl", attachment.getProxyUrl());
        jsonObject.put("fileName", attachment.getFileName());
        jsonObject.put("contentType", attachment.getContentType());
        jsonObject.put("description", attachment.getDescription());
        jsonObject.put("size", attachment.getSize());
        jsonObject.put("height", attachment.getHeight());
        jsonObject.put("width", attachment.getWidth());

        String url = attachment.getUrl();
        int hashStartIndex = url.lastIndexOf("_");
        String msgHash = CharSequenceUtil.subBefore(url.substring(hashStartIndex + 1), ".", true);
        jsonObject.put("msgHash", msgHash);

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.now();
        String localTime = df.format(time);
        jsonObject.put("date", localTime);

        //拼接路径，数据库中可以直接保存该路径，返回前端，前端即可访问
        log.info("MessageMetadataRegistry:" + jsonObject.toJSONString());
        Map<String, Object> body = new HashMap<>();
        body.put("discord", jsonObject);
        body.put("type", Scene.GENERATE_END);
//        request(body);

        String path = saveImage(url);

        //**A beautiful woman wearing a cheongsam is standing at the door --q 2 --s 750 --v 5.1 --style raw** - @brandtshelly (relaxed)
        content = content.substring(2, content.indexOf("--q")).trim();
        List<Order> orders = orderDao.findByPrompt(content);
        if (CollectionUtils.isNotEmpty(orders)) {
            for (Order order : orders) {
                order.setMessageId(messageId);
                order.setMsgHash(msgHash);
                //base64(path)
                order.setBase64("");
                order.setUrl("http://13.48.6.120:8000/" + path.substring(path.lastIndexOf("\\") + 1));
                orderDao.update(order);
            }
        }
    }

    private static String base64(String path) {
        FileInputStream inputStream = null;
        try {
            Base64.Encoder encoder = Base64.getEncoder();

            inputStream = new FileInputStream(path);

            int available = inputStream.available();
            byte[] bytes = new byte[available];
            inputStream.read(bytes);

            return encoder.encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void trigger(String content, Scene scene) {
        Map<String, Object> body = new HashMap<>();
        body.put("content", content);
        body.put("type", scene);
        request(body);
    }

    private void request(Map<String, Object> params) {
        webClient.post()
                .uri(midjourneyBotConfiguration.getCallBackConfig().getUrl())
                .bodyValue(params)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(response -> log.info("Upscale response: " + response),
                        error -> log.error("Error: " + error.getMessage()));
    }

    private String saveImage(String imageUrl) {
        StringBuilder builder = new StringBuilder(System.getProperty("user.dir"));
        builder.append(File.separator)
                .append("data")
                .append(File.separator)
                .append(getRandom())
                .append(".png");
        String pathname = builder.toString();
        InputStream inStream = null;
        FileOutputStream fops = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            inStream = conn.getInputStream();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            byte[] btData = outStream.toByteArray();
            File file = new File(pathname);
            File fileParent = file.getParentFile();
            if (!fileParent.exists()) {
                fileParent.mkdirs();
                file.createNewFile();
            }
            fops = new FileOutputStream(file);
            fops.write(btData);
            fops.flush();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("图片保存失败：{}", e.getMessage());
        } finally {
            try {
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fops.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pathname;
    }

    private String getRandom() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String random = RandomStringUtils.random(SIZE, STRING_RANDOM);
        return format.format(new Date()) + random;
    }
}