package com.my_geeks.geeks.domain.user.service;

import com.my_geeks.geeks.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    public Boolean emailCheck(String email) {
        return userRepository.existsByEmail(email);
    }

    public Boolean nicknameCheck(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}
