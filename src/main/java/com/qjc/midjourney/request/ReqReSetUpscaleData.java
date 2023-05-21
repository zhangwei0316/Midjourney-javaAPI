package com.qjc.midjourney.request;

import lombok.Builder;
import lombok.Data;

@Data
public class ReqReSetUpscaleData {
    @Builder.Default
    private int component_type = 2;
    private String custom_id;

    public ReqReSetUpscaleData(String messageHash) {
        this.custom_id = String.format("MJ::JOB::reroll::0::%s::SOLO", messageHash);
    }

}