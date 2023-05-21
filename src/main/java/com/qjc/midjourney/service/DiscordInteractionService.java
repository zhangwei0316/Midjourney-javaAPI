package com.qjc.midjourney.service;

import com.qjc.midjourney.dto.RequestTrigger;

/**
 * @author zw
 */
public interface DiscordInteractionService {

    String generateImage(RequestTrigger requestTrigger);

    String upscale(int index, String messageId);

    void variate(int index, String messageId, String messageHash);

    void maxUpscale(String messageId, String messageHash);

    void reSet(String messageId, String messageHash);
}
