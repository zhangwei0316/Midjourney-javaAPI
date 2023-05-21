package com.qjc.midjourney.config;

import com.qjc.midjourney.event.DiscordEventHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.security.auth.login.LoginException;

/**
 * @author zw
 */
@Component
public class DiscordClientInitializer {
    private final DiscordConfig discordConfig;
    private JDA jda;
    private DiscordEventHandler discordEventHandler;

    @Autowired
    public DiscordClientInitializer(DiscordConfig discordConfig, DiscordEventHandler discordEventHandler) {
        this.discordConfig = discordConfig;
        this.discordEventHandler = discordEventHandler;
    }

    @PostConstruct
    public void init() throws LoginException {
        jda = JDABuilder
                .createDefault(discordConfig.getBotToken())
                .addEventListeners(discordEventHandler).build();
    }

    public JDA getJda() {
        return jda;
    }
}