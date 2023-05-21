package com.qjc.midjourney.dto;

import lombok.Data;

@Data
public class Member1 {

    private String id;

    private String username;

    private String password;

    private Integer amount;

    private Integer normalCount;

    private String created_at;

    private String parentMessageId;
}
