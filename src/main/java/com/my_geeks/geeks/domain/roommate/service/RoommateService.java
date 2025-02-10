package com.my_geeks.geeks.domain.roommate.service;

import com.my_geeks.geeks.domain.roommate.entity.Roommate;
import com.my_geeks.geeks.domain.roommate.repository.RoommateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoommateService {
    private final RoommateRepository roommateRepository;

    @Transactional
    public String send(Long senderId, Long receiverId) {
        // TODO: 만약에 받는 사람이 보내는 사람에게 이미 신청을 했다면?
        Roommate roommate = new Roommate(senderId, receiverId);
        roommateRepository.save(roommate);
        return "success";
    }
}
