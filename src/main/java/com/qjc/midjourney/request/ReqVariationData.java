package com.qjc.midjourney.request;

import lombok.Data;

@Data
public  class ReqVariationData {
        private int component_type;
        private String custom_id;
        public ReqVariationData(int index, String messageHash) {
            this.component_type = 2;
            this.custom_id = String.format("MJ::JOB::variation::%d::%s", index, messageHash);
     }
}