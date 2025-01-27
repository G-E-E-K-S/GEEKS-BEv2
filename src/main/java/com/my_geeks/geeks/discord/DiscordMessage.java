package com.my_geeks.geeks.discord;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscordMessage {
    private String content;

    private List<Embed> embeds;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Embed {
        private String title;

        private String description;
    }
}
