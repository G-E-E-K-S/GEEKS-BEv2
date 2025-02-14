package com.my_geeks.geeks.domain.user.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.my_geeks.geeks.domain.matching.service.MatchingService;
import com.my_geeks.geeks.domain.user.entity.User;
import com.my_geeks.geeks.domain.user.entity.UserDetail;
import com.my_geeks.geeks.domain.user.repository.UserDetailRepository;
import com.my_geeks.geeks.domain.user.repository.UserRepository;
import com.my_geeks.geeks.domain.user.requestDto.CreateUserDetailReq;
import com.my_geeks.geeks.domain.user.requestDto.SignUpReq;
import com.my_geeks.geeks.domain.user.requestDto.UpdateProfileReq;
import com.my_geeks.geeks.domain.user.responseDto.GetUserDetailRes;
import com.my_geeks.geeks.exception.CustomException;
import com.my_geeks.geeks.exception.ErrorCode;
import com.my_geeks.geeks.mail.MailUtil;
import com.my_geeks.geeks.mail.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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

    private final AmazonS3 amazonS3;
    @Value("${aws.s3.bucket}")
    private String bucket;

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
        UserDetail userDetail = findUserDetail(userId);
        return GetUserDetailRes.from(userDetail);
    }

    @Transactional
    public GetUserDetailRes updateUserDetail(Long userId, CreateUserDetailReq req) {
        UserDetail userDetail = findUserDetail(userId);
        userDetail.updateDetail(req);

        // 바뀐 생활 습관으로 다시 점수 계산
        matchingService.recalculate(userId, userDetail);
        return GetUserDetailRes.from(userDetail);
    }

    public UserDetail findUserDetail(Long userId) {
        return userDetailRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
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

    @Transactional
    public String changeImage(Long userId, List<MultipartFile> files) {
        User user = getUser(userId);

        MultipartFile file = files.get(0);
        String fileName = "profile/GEEKS_" + userId;

        try {
            if(user.getImage() != null) {
                amazonS3.deleteObject(bucket, fileName);
            }

            ObjectMetadata metadata = new ObjectMetadata();

            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            user.setImage(fileName);
            amazonS3.putObject(bucket, fileName, file.getInputStream(), metadata);
        } catch (Exception e) {
            throw new CustomException(AWS_S3_UPLOAD_ERROR);
        }

        return "success";
    }

    @Transactional
    public String updateProfile(Long userId, UpdateProfileReq req, List<MultipartFile> files) {
        User user = getUser(userId);

        // 이미지 변경
        if(!files.isEmpty()) {
            changeImage(userId, files);
        }

        user.updateProfile(req);
        return "success";
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }
}
