package com.qjc.midjourney.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Auto-generated: 2023-04-25 9:7:8
 *
 * @author zw
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class RequestMidjourneyOptions {
    @Builder.Default
    private int type = 3;
    @Builder.Default
    private String name = "prompt";
    private String value;

}