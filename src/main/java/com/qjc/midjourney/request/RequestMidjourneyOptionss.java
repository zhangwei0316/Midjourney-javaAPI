package com.qjc.midjourney.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class RequestMidjourneyOptionss {
    @Builder.Default
    private int type = 3;
    @Builder.Default
    private String name = "prompt";
    @Builder.Default
    private String description = "The prompt to imagine";
    @Builder.Default
    private boolean required = true;

}