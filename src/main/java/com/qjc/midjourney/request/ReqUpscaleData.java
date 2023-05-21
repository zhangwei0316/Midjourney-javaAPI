package com.qjc.midjourney.request;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class ReqUpscaleData {
    @Builder.Default
    private int component_type = 2;
    private String custom_id;

    public ReqUpscaleData(int index, String messageHash) {
        this.custom_id = String.format("MJ::JOB::upsample::%d::%s", index, messageHash);
    }

}