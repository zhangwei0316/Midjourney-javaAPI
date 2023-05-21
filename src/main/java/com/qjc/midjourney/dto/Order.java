package com.qjc.midjourney.dto;

import lombok.Data;

@Data
public class Order {

    private String id;

    private String messageId;

    private String msgHash;

    private String base64;

    private String url;

    private String prompt;
}
