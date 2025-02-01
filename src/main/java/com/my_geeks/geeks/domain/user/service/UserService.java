package com.my_geeks.geeks.domain.user.service;

import com.my_geeks.geeks.domain.user.entity.User;
import com.my_geeks.geeks.domain.user.entity.enumeration.RoleType;
import com.my_geeks.geeks.domain.user.repository.UserRepository;
import com.my_geeks.geeks.domain.user.requestDto.SignUpReq;
import com.my_geeks.geeks.exception.CustomException;
import com.my_geeks.geeks.exception.ErrorCode;
import com.my_geeks.geeks.mail.MailUtil;
import com.my_geeks.geeks.mail.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static com.my_geeks.geeks.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    private final MailUtil mailUtil;

    private final RedisUtil redisUtil;

    private final BCryptPasswordEncoder encoder;

    public String signup(SignUpReq req) {
        User user = User.builder()
                .email(req.getEmail())
                .password(encoder.encode(req.getPassword()))
                .nickname(req.getNickname())
                .major(req.getMajor())
                .studentNum(req.getStudentNum())
                .dormitory(req.getDormitory())
                .isOpen(true)
                .roleType(RoleType.ROLE_USER)
                .gender(req.getGender())
                .build();

        userRepository.save(user);
        return "success";
    }

    public String emailCheck(String email) {
        Boolean exists = userRepository.existsByEmail(email);

        if(exists) {
            throw new CustomException(DUPLICATE_EMAIL_ERROR);
        }

        emailCode(email);
        return "available";
    }

    public String nicknameCheck(String nickname) {
        Boolean exists = userRepository.existsByNickname(nickname);

        if(exists) {
            throw new CustomException(DUPLICATE_NICKNAME_ERROR);
        }

        return "available";
    }

    public String emailCode(String email) {
        String code = generateRandomCode();
        redisUtil.saveCode(email, code);
        mailUtil.send(email, code);
        return code;
    }

    public String checkCode(String email, String code) {
        String redisCode = redisUtil.getCode(email);

        if(redisCode == null) {
            throw new CustomException(INVALID_CODE_ERROR);
        }

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
