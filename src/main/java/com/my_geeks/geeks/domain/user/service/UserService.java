package com.my_geeks.geeks.domain.user.service;

import com.my_geeks.geeks.domain.user.repository.UserRepository;
import com.my_geeks.geeks.mail.MailUtil;
import com.my_geeks.geeks.mail.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    private final MailUtil mailUtil;

    private final RedisUtil redisUtil;

    public Boolean emailCheck(String email) {
        return userRepository.existsByEmail(email);
    }

    public Boolean nicknameCheck(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public String emailCode(String email) {
        String code = generateRandomCode();
        redisUtil.saveCode(email, code);
        mailUtil.send(email);
        return code;
    }

    public String checkCode(String email, String code) {
        String redisCode = redisUtil.getCode(email);

        if(redisCode.equals(code)) {
            return "success";
        }

        return "fail";
    }

    private String generateRandomCode() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(4);

        for(int i = 0; i < 4; i++) {
            stringBuilder.append(random.nextInt(10));
        }

        return stringBuilder.toString();
    }
}
