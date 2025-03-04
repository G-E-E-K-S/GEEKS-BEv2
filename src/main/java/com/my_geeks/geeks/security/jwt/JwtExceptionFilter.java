package com.my_geeks.geeks.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my_geeks.geeks.customResponse.BaseResponse;
import com.my_geeks.geeks.discord.DiscordSender;
import com.my_geeks.geeks.exception.CustomException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {
    private final DiscordSender discordSender;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            filterChain.doFilter(request, response);
        } catch (CustomException e){
            response.setStatus(e.getErrorCode().getHttpStatus().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(), BaseResponse.fail(e));
            //discordSender.sendWebhookMessage2(e.getErrorCode().name(), e.getMessage(), request.getMethod() + " " + request.getRequestURL().toString());
        }
    }
}
