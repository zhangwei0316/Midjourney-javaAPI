package com.qjc.midjourney.request;

import lombok.Builder;
import lombok.Data;

@Data

public class ReqMaxUpscaleData {
    @Builder.Default
    private int component_type;
    private String custom_id;

    public ReqMaxUpscaleData(String messageHash) {
        this.component_type = 2;
        this.custom_id = String.format("MJ::JOB::variation::1::%s::SOLO", messageHash);
    }

}