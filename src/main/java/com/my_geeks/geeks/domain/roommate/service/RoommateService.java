package com.my_geeks.geeks.domain.roommate.service;

import com.my_geeks.geeks.domain.roommate.entity.Roommate;
import com.my_geeks.geeks.domain.roommate.repository.RoommateRepository;
import com.my_geeks.geeks.domain.roommate.responseDto.GetSendList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoommateService {
    private final RoommateRepository roommateRepository;

    @Transactional
    public String send(Long senderId, Long receiverId, Long matchingPointId) {
        // TODO: 만약에 받는 사람이 보내는 사람에게 이미 신청을 했다면?
        Roommate roommate = new Roommate(senderId, receiverId, matchingPointId);
        roommateRepository.save(roommate);
        return "success";
    }

    public List<GetSendList> getSendList(Long senderId) {
        return roommateRepository.getSendList(senderId);
    }
}
