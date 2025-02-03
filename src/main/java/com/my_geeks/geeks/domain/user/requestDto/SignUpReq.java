package com.my_geeks.geeks.domain.user.requestDto;

import com.my_geeks.geeks.domain.user.entity.User;
import com.my_geeks.geeks.domain.user.entity.enumeration.Dormitory;
import com.my_geeks.geeks.domain.user.entity.enumeration.Gender;
import com.my_geeks.geeks.domain.user.entity.enumeration.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static jakarta.persistence.EnumType.STRING;

@Getter
@Schema(description = "회원가입 요청 DTO")
public class SignUpReq {
    @Schema(description = "이메일", defaultValue = "test@sangmyung.kr")
    private String email;

    @Schema(description = "비밀번호", defaultValue = "1234")
    private String password;

    @Schema(description = "닉네임", defaultValue = "TEST_5")
    private String nickname;

    @Schema(description = "전공", defaultValue = "소프트웨어학과")
    private String major;

    @Schema(description = "학번", defaultValue = "19")
    private int studentNum;

    @Schema(description = "기숙사 종류", defaultValue = "NEW")
    private Dormitory dormitory;

    @Schema(description = "성별", defaultValue = "MALE")
    private Gender gender;

    public User toEntity(BCryptPasswordEncoder encoder) {
        return User.builder()
                .email(email)
                .password(encoder.encode(password))
                .nickname(nickname)
                .major(major)
                .studentNum(studentNum)
                .dormitory(dormitory)
                .isOpen(true)
                .roleType(RoleType.ROLE_USER)
                .gender(gender)
                .build();
    }
}
