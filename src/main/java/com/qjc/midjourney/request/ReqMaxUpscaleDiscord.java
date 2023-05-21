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
public class ReqMaxUpscaleDiscord {
    @Builder.Default
    private int type = 3;
    private String guild_id;
    private String channel_id;
    @Builder.Default
    private int message_flags = 0;
    private String message_id;
    @Builder.Default
    private String application_id = "936929561302675456";
    @Builder.Default
    private String session_id = "1f3dbdf09efdf93d81a3a6420882c92c";
    private ReqMaxUpscaleData data;


}