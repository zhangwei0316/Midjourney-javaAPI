package com.qjc.midjourney.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * The type CallBackConfig  config.
 *
 * @author zw
 */
@Configuration
@ConfigurationProperties(prefix = "call-back")
@Data
public class CallBackConfig {
    private String url;
}