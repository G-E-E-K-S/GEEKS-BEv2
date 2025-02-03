package com.my_geeks.geeks.domain.user.service;

import com.my_geeks.geeks.domain.matching.service.MatchingService;
import com.my_geeks.geeks.domain.user.entity.User;
import com.my_geeks.geeks.domain.user.entity.UserDetail;
import com.my_geeks.geeks.domain.user.repository.UserDetailRepository;
import com.my_geeks.geeks.domain.user.repository.UserRepository;
import com.my_geeks.geeks.domain.user.requestDto.CreateUserDetailReq;
import com.my_geeks.geeks.domain.user.requestDto.SignUpReq;
import com.my_geeks.geeks.domain.user.responseDto.GetUserDetailRes;
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

    private final UserDetailRepository userDetailRepository;

    private final MatchingService matchingService;

    private final MailUtil mailUtil;

    private final RedisUtil redisUtil;

    private final BCryptPasswordEncoder encoder;

    @Transactional
    public String signup(SignUpReq req) {
        User user = req.toEntity(encoder);

        userRepository.save(user);
        return "success";
    }

    @Transactional
    public String createDetail(Long userId, CreateUserDetailReq req) {
        UserDetail userDetail = req.toEntity(userId);
        userDetailRepository.save(userDetail);
        matchingService.calculate(userId, userDetail);
        return "success";
    }

    @Transactional
    public String createTotalDetail(CreateUserDetailReq req) {
        for(Long i = 2L; i <= 4L; i++) {
            UserDetail userDetail = req.toEntity(i);
            userDetailRepository.save(userDetail);
            matchingService.calculate(i, userDetail);
        }
        return "success";
    }

    public GetUserDetailRes getUserDetail(Long userId) {
        UserDetail userDetail = userDetailRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        return GetUserDetailRes.from(userDetail);
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
