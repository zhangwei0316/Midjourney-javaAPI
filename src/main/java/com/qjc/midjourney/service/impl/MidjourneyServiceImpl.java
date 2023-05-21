package com.qjc.midjourney.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.qjc.midjourney.dao.OrderDao;
import com.qjc.midjourney.dto.Order;
import com.qjc.midjourney.dto.RequestTrigger;
import com.qjc.midjourney.service.DiscordInteractionService;
import com.qjc.midjourney.service.MidjourneyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MidjourneyServiceImpl implements MidjourneyService {

    @Autowired
    private DiscordInteractionService discordInteractionService;

    @Autowired
    private OrderDao orderDao;

    @Override
    public String executeAction(RequestTrigger requestTrigger) throws Exception {
        String type = requestTrigger.getType();
        String res = "";
        switch (type) {
            case "generate":
                // Call GenerateImage service
                res = discordInteractionService.generateImage(requestTrigger);
                break;
            case "upscale":
                // Call ImageUpscale service
                discordInteractionService.upscale(requestTrigger.getIndex(),
                        requestTrigger.getId());
                break;
            case "variation":
                // Call ImageVariation service
                discordInteractionService.variate(requestTrigger.getIndex(),
                        requestTrigger.getDiscordMsgId(),
                        requestTrigger.getMsgHash());
                break;
            case "maxUpscale":
                // Call ImageMaxUpscale service
                discordInteractionService.maxUpscale(requestTrigger.getDiscordMsgId(),
                        requestTrigger.getMsgHash());
                break;
            case "reset":
                // Call ImageReset service
                discordInteractionService.reSet(requestTrigger.getDiscordMsgId(),
                        requestTrigger.getMsgHash());
                break;
            default:
                throw new Exception("Invalid type");
        }
        return res;
    }

    @Override
    public String detail(String id) throws Exception {
        Order order = orderDao.findById(id);
        if (order == null) {
            throw new Exception("fail");
        }
        return order.getUrl();
    }

    @Override
    public String syncUser(JSONObject data) {
        log.info("获取数据：{}", data.toJSONString());
        return "success";
    }
}