package com.my_geeks.geeks.domain.user.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.my_geeks.geeks.domain.matching.repository.MatchingPointRepository;
import com.my_geeks.geeks.domain.matching.service.MatchingService;
import com.my_geeks.geeks.domain.roommate.entity.Roommate;
import com.my_geeks.geeks.domain.roommate.repository.RoommateRepository;
import com.my_geeks.geeks.domain.user.entity.User;
import com.my_geeks.geeks.domain.user.entity.UserDetail;
import com.my_geeks.geeks.domain.user.entity.embedded.NotifyAllow;
import com.my_geeks.geeks.domain.user.repository.UserDetailRepository;
import com.my_geeks.geeks.domain.user.repository.UserRepository;
import com.my_geeks.geeks.domain.user.requestDto.CreateUserDetailReq;
import com.my_geeks.geeks.domain.user.requestDto.LoginReq;
import com.my_geeks.geeks.domain.user.requestDto.SignUpReq;
import com.my_geeks.geeks.domain.user.requestDto.UpdateProfileReq;
import com.my_geeks.geeks.domain.user.responseDto.*;
import com.my_geeks.geeks.exception.CustomException;
import com.my_geeks.geeks.exception.ErrorCode;
import com.my_geeks.geeks.mail.MailUtil;
import com.my_geeks.geeks.mail.RedisUtil;
import com.my_geeks.geeks.redis.CacheRepository;
import com.my_geeks.geeks.security.custom.CustomUserInfoDto;
import com.my_geeks.geeks.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private final MatchingPointRepository matchingPointRepository;

    private final RoommateRepository roommateRepository;

    private final CacheRepository cacheRepository;

    private final MailUtil mailUtil;

    private final RedisUtil redisUtil;

    private final JwtUtil jwtUtil;

    private final AmazonS3 amazonS3;
    @Value("${aws.s3.bucket}")
    private String bucket;

    private final BCryptPasswordEncoder encoder;

    @Transactional
    public String signup(SignUpReq req, HttpServletResponse response) {
        User user = req.toEntity(encoder);
        userRepository.save(user);

        // 사용자 jwt 토큰 생성하기
        CustomUserInfoDto customUserInfoDto = new CustomUserInfoDto(user.getId(), user.getRoleType());
        String accessToken = jwtUtil.createAccessToken(customUserInfoDto);
        ResponseCookie cookie = jwtUtil.createCookie(accessToken);

        response.addHeader("Set-Cookie", cookie.toString());
        return accessToken;
    }

    public String login(LoginReq req, HttpServletResponse response) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new CustomException(EMAIL_NOT_FOUND));

        if(!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new CustomException(PASSWORD_NOT_ALLOWED);
        }

        // 사용자 jwt 토큰 생성하기
        CustomUserInfoDto customUserInfoDto = new CustomUserInfoDto(user.getId(), user.getRoleType());
        String accessToken = jwtUtil.createAccessToken(customUserInfoDto);
        ResponseCookie cookie = jwtUtil.createCookie(accessToken);

        response.addHeader("Set-Cookie", cookie.toString());
        return accessToken;
    }

    public String logout(HttpServletResponse response) {
        ResponseCookie cookie = jwtUtil.deleteCookie();
        response.addHeader("Set-Cookie", cookie.toString());
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
            return "duplicate";
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

        if(!redisCode.equals(code)) {
            throw new CustomException(INVALID_CODE_ERROR);
        }

        return "success";
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
        String fileName = "profile/GEEKS_" + userId + "_" + LocalDate.now();

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

    public GetUserProfileRes getProfile(Long userId) {
        User user = getUser(userId);
        return GetUserProfileRes.from(user);
    }

    @CacheEvict(value = "UserCache", key = "#userId", cacheManager = "cacheManager")
    @Transactional
    public String updateProfile(Long userId, UpdateProfileReq req, List<MultipartFile> files) {
        User user = getUser(userId);

        // 이미지 변경
        if(files != null) {
            MultipartFile file = files.get(0);
            String fileName = "profile/GEEKS_" + userId + "_" + LocalDateTime.now();

            try {
                if(user.getImage() != null) {
                    amazonS3.deleteObject(bucket, user.getImage());
                }

                ObjectMetadata metadata = new ObjectMetadata();

                metadata.setContentLength(file.getSize());
                metadata.setContentType(file.getContentType());

                user.setImage(fileName);
                amazonS3.putObject(bucket, fileName, file.getInputStream(), metadata);
            } catch (Exception e) {
                throw new CustomException(AWS_S3_UPLOAD_ERROR);
            }
        }

        user.updateProfile(req);
        return "success";
    }

    public GetMyPageRes getMyPage(Long userId) {
        User user = getUser(userId);
        User userRoommate = user.getRoommateId() != null ?
                roommateRepository.getRoommateUser(user.getRoommateId(), userId) : null;

        Long matchingPointId = null;

        if(userRoommate != null) {
            matchingPointId = matchingPointRepository.findBySmallUserIdAndLargeUserId(
                    userId < userRoommate.getId() ? userId : userRoommate.getId(),
                    userId < userRoommate.getId() ? userRoommate.getId() : userId
            ).getId();
        }

        GetMyPageRes myPageRes = GetMyPageRes.from(user, userRoommate, matchingPointId);

        return myPageRes;
    }

    public GetUserInfoRes getUserInfo(Long userId) {
        User user = getUser(userId);

        return GetUserInfoRes.builder()
                .email(user.getEmail())
                .createdDate(user.getCreatedDate())
                .build();
    }

    public String validateCookie(String accessToken) {
        if(accessToken == null || !jwtUtil.validateToken(accessToken)) {
            throw new CustomException(JWT_EXPIRED_TOKEN_ERROR);
        }

        return accessToken;
    }

    @Transactional
    public Boolean changeOpen(Long userId) {
        User user = getUser(userId);

        if(user.isOpen()) {
            user.setOpen(false);
        } else {
            user.setOpen(true);
        }

        return user.isOpen();
    }

    @CacheEvict(value = "UserCache", key = "#userId", cacheManager = "cacheManager")
    @Transactional
    public String saveFcmToken(Long userId, String fcmToken) {
        User user = getUser(userId);
        user.setFcmToken(fcmToken);
        return "success";
    }

    @Transactional
    public Boolean changeRoommateNotify(Long userId) {
        User user = getUser(userId);
        NotifyAllow notifyAllow = user.getNotifyAllow();
        boolean notifyStatus = notifyAllow.isRoommateNotify();
        notifyAllow.setRoommateNotify(!notifyStatus);
        return !notifyStatus;
    }

    @Transactional
    public Boolean changeServiceNotify(Long userId) {
        User user = getUser(userId);
        NotifyAllow notifyAllow = user.getNotifyAllow();

        boolean notifyStatus = notifyAllow.isServiceNotify();
        notifyAllow.setServiceNotify(!notifyStatus);
        return !notifyStatus;
    }

    public GetNotifyStateRes getNotifyState(Long userId) {
        User user = getUser(userId);
        NotifyAllow notify = user.getNotifyAllow();

        return GetNotifyStateRes.builder()
                .roommate(notify.isRoommateNotify())
                .chat(notify.isChatNotify())
                .service(notify.isServiceNotify())
                .marketing(notify.isMarketingNotify())
                .build();
    }

    public List<GetUserSearchRes> userSearch(Long userId, String keyword) {
        User user = getUser(userId);
        return userRepository.searchByKeyword(userId, user.getGender(), user.getDormitory(), keyword);
    }

    public Boolean checkPassword(Long userId, String password) {
        User user = getUser(userId);

        if(!encoder.matches(password, user.getPassword())) {
            return false;
        }

        return true;
    }

    @CacheEvict(value = "UserCache", key = "#userId", cacheManager = "cacheManager")
    @Transactional
    public String changePassword(Long userId, String password) {
        User user = getUser(userId);
        user.setPassword(encoder.encode(password));
        return "success";
    }

    public Boolean checkFcmToken(Long userId) {
        User user = getUser(userId);
        if(user.getFcmToken() != null) return true;
        return false;
    }

    //@Cacheable(value = "UserCache", key = "#userId", cacheManager = "cacheManager")
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
