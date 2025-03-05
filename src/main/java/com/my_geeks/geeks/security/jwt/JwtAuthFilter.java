package com.my_geeks.geeks.security.jwt;

import com.my_geeks.geeks.exception.CustomException;
import com.my_geeks.geeks.exception.ErrorCode;
import com.my_geeks.geeks.security.custom.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    private static final String[] EXCLUDE_PATHS = {
            "/api/v1/user/check/email/**",
            "/api/v1/user/auth/code/**",
            "/api/v1/user/check/nickname/**",
            "/api/v1/user/signup",
            "/api/v1/user/login",
            "/swagger-ui/**",
            "/prometheus/**"
    };

    /**
     * JWT 검증, 유효하다면 UserDetailService 의 loadByUserName 으로 해당 유저가 데이터베이스에 존재하는지 판단
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        String accessToken = request.getHeader("Authorization");
        System.out.println("security jwt auth filter");
        try {
            if (accessToken != null && accessToken.startsWith("Bearer ")) {
                // String token = accessToken;
                String token = accessToken.substring(7);
                //JWT 유효성 검증
                if (jwtUtil.validateToken(token)) {
                    Long userId = jwtUtil.getUserId(token);

                    //유저와 토큰 일치 시 userDetails 생성
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId.toString());

                    if (userDetails != null) {
                        //UserDetsils, Password, Role -> 접근권한 인증 Token 생성
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        //현재 Request의 Security Context에 접근권한 설정
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
            }

            filterChain.doFilter(request, response); // 다음 필터로 넘기기
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.JWT_INVALID_TOKEN_ERROR);
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.JWT_EXPIRED_TOKEN_ERROR);
        } catch (UnsupportedJwtException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.JWT_UNSUPPORTED_TOKEN_ERROR);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.JWT_CLAIMS_EMPTY_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.JWT_FILTER_ERROR);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        AntPathMatcher antPathMatcher = new AntPathMatcher();

        return Arrays.stream(EXCLUDE_PATHS)
                .anyMatch(path -> antPathMatcher.match(path, requestURI));
    }
}
