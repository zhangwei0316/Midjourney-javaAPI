package com.qjc.midjourney.service;

import com.alibaba.fastjson.JSONObject;
import com.qjc.midjourney.dto.RequestTrigger;

/**
 * @author zw
 */
public interface MidjourneyService {
    public String executeAction(RequestTrigger requestTrigger) throws Exception;

    String detail(String id) throws Exception;

    String syncUser(JSONObject data);
}
