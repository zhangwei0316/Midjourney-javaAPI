package com.qjc.midjourney.service.impl;

import com.alibaba.fastjson.JSON;
import com.qjc.midjourney.config.MidjourneyBotConfiguration;
import com.qjc.midjourney.dao.OrderDao;
import com.qjc.midjourney.dto.Order;
import com.qjc.midjourney.dto.RequestTrigger;
import com.qjc.midjourney.request.*;
import com.qjc.midjourney.service.DiscordInteractionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author zw
 */
@Service
@Slf4j
public class DiscordInteractionServiceImpl implements DiscordInteractionService {
    private static final String DISCORD_API_URL = "https://discord.com/api/v9/interactions";
    private final WebClient webClient;
    private final OrderDao orderDao;

    private final MidjourneyBotConfiguration midjourneyBotConfiguration;

    @Autowired
    public DiscordInteractionServiceImpl(MidjourneyBotConfiguration midjourneyBotConfiguration,
                                         WebClient.Builder webClientBuilder, OrderDao orderDao) {
        this.webClient = webClientBuilder.build();
        this.midjourneyBotConfiguration = midjourneyBotConfiguration;
        this.orderDao = orderDao;
    }

    /**
     * Call GenerateImage service
     *
     * @param
     */
    @Override
    public String generateImage(RequestTrigger requestTrigger) {
        sendRequest(RequestMidjourneyDto.builder()
                .guild_id(midjourneyBotConfiguration.getDiscordConfig().getServerId())
                .channel_id(midjourneyBotConfiguration.getDiscordConfig().getChannelId())
                .data(RequestMidjourneyData.builder()
                        .options(Arrays.asList(RequestMidjourneyOptions.builder()
                                .value(requestTrigger.getPrompt())
                                .build()))
                        .application_command(RequestMidjourneyApplicationCommand.builder()
                                .options(Arrays.asList(RequestMidjourneyOptionss.builder().build()))
                                .build())
                        .build())
                .build());
        Order order = new Order();
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();
        order.setId(id);
        order.setPrompt(requestTrigger.getPrompt());
        orderDao.insert(order);
        return id;
    }


    /**
     * Call ImageUpscale service
     *
     * @param
     */
    @Override
    public String upscale(int index, String messageId) {
        Order order = orderDao.findById(messageId);
        sendRequest(ReqUpscaleDiscord.builder()
                .guild_id(midjourneyBotConfiguration.getDiscordConfig().getServerId())
                .channel_id(midjourneyBotConfiguration.getDiscordConfig().getChannelId())
                .message_id(order.getMessageId())
                .data(new ReqUpscaleData(index, order.getMsgHash()).setComponent_type(2))
                .build());
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();
        order.setId(id);
        order.setPrompt(order.getPrompt() + " U" + index);
        orderDao.insert(order);
        return "";
    }

    /**
     * Call ImageVariation service
     *
     * @param index
     * @param messageId
     * @param messageHash
     */
    @Override
    public void variate(int index, String messageId, String messageHash) {
        sendRequest(ReqVariationDiscord.builder()
                .guild_id(midjourneyBotConfiguration.getDiscordConfig().getServerId())
                .channel_id(midjourneyBotConfiguration.getDiscordConfig().getChannelId())
                .message_id(messageId)
                .data(new ReqVariationData(index, messageHash))
                .build()
        );
    }

    @Override
    public void maxUpscale(String messageId, String messageHash) {
        sendRequest(ReqMaxUpscaleDiscord.builder()
                .guild_id(midjourneyBotConfiguration.getDiscordConfig().getServerId())
                .channel_id(midjourneyBotConfiguration.getDiscordConfig().getChannelId())
                .message_id(messageId)
                .data(new ReqMaxUpscaleData(messageHash))
                .build());
    }

    @Override
    public void reSet(String messageId, String messageHash) {
        sendRequest(ReqReSetUpscaleDiscord.builder()
                .guild_id(midjourneyBotConfiguration.getDiscordConfig().getServerId())
                .channel_id(midjourneyBotConfiguration.getDiscordConfig().getChannelId())
                .message_id(messageId)
                .data(new ReqReSetUpscaleData(messageHash))
                .build());
    }


    /**
     * request DISCORD_API_URL
     *
     * @param requestBody
     */
    private void sendRequest(Object requestBody) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", midjourneyBotConfiguration.getDiscordConfig().getUserToken());
            headers.add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36");
            HttpEntity<String> httpEntity = new HttpEntity<>(JSON.toJSONString(requestBody), headers);
            new RestTemplate().postForEntity(DISCORD_API_URL, httpEntity, String.class);
//            webClient.post()
//                    .uri(DISCORD_API_URL)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .header("Authorization", midjourneyBotConfiguration.getDiscordConfig().getUserToken())
//                    .body(BodyInserters.fromValue(requestBody))
//                    .retrieve()
//                    .bodyToMono(String.class)
//                    .subscribe(response -> log.info("Upscale response: " + response),
//                            error -> log.error("Error: " + error.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
