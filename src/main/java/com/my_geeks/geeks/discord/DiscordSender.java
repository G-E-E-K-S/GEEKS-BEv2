package com.my_geeks.geeks.discord;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my_geeks.geeks.exception.CustomException;
import com.my_geeks.geeks.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DiscordSender {
    private final String webhookURL;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public DiscordSender(@Value("${discord.webhook}") String URL) {
        this.webhookURL = URL;
    }

    public void sendWebhookMessage(String code, String message, WebRequest webRequest) {
        DiscordMessage discordMessage = DiscordMessage.builder()
                .content("## Spring Exception")
                .embeds(
                        List.of(
                                DiscordMessage.Embed.builder()
                                .title("예외 발생")
                                .description(
                                        "### ⏰ 발생 시간\n"
                                        + "**" + LocalDateTime.now() + "**\n"
                                        + "### 🔗 요청 URL\n"
                                        + "**" + requestUrl(webRequest) + "**\n"
                                        + "### 🚨 오류 코드\n"
                                        + "**" + code + "**\n"
                                        + "### 📑 오류 내용\n"
                                        + "**" + message + "**\n"
                                )
                                .build()
                        )
                )
                .build();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> httpEntity = new HttpEntity<>(convertToJson(discordMessage), headers);
            restTemplate.postForObject(webhookURL, httpEntity, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.DISCORD_WEBHOOK_ERROR);
        }
    }

    public void sendWebhookMessage2(String code, String message, String url) {
        DiscordMessage discordMessage = DiscordMessage.builder()
                .content("## Spring Exception")
                .embeds(
                        List.of(
                                DiscordMessage.Embed.builder()
                                        .title("예외 발생")
                                        .description(
                                                "### ⏰ 발생 시간\n"
                                                        + "**" + LocalDateTime.now() + "**\n"
                                                        + "### 🔗 요청 URL\n"
                                                        + "**" + url + "**\n"
                                                        + "### 🚨 오류 코드\n"
                                                        + "**" + code + "**\n"
                                                        + "### 📑 오류 내용\n"
                                                        + "**" + message + "**\n"
                                        )
                                        .build()
                        )
                )
                .build();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> httpEntity = new HttpEntity<>(convertToJson(discordMessage), headers);
            restTemplate.postForObject(webhookURL, httpEntity, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.DISCORD_WEBHOOK_ERROR);
        }
    }

    private String convertToJson(DiscordMessage message) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.DISCORD_CONVERT_JSON_ERROR);
        }
    }

    private String requestUrl(WebRequest webRequest) {
        HttpServletRequest request = ((ServletWebRequest) webRequest).getRequest();
        String fullPath = request.getMethod() + " " + request.getRequestURL();

        String queryString = request.getQueryString();
        if (queryString != null) {
            fullPath += "?" + queryString;
        }

        return fullPath;
    }
}
