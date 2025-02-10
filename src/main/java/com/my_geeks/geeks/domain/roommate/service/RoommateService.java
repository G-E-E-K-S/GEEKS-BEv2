package com.my_geeks.geeks.domain.roommate.service;

import com.my_geeks.geeks.domain.roommate.entity.Roommate;
import com.my_geeks.geeks.domain.roommate.repository.RoommateRepository;
import com.my_geeks.geeks.domain.roommate.responseDto.GetApplyList;
import com.my_geeks.geeks.exception.CustomException;
import com.my_geeks.geeks.exception.ErrorCode;
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

    public List<GetApplyList> getSendList(Long senderId) {
        return roommateRepository.getSendList(senderId);
    }

    public List<GetApplyList> getReceiveList(Long receiverId) {
        return roommateRepository.getReceiveList(receiverId);
    }

    @Transactional
    public String deleteSendApply(Long roommateId) {
        Roommate roommate = getRoommate(roommateId);
        roommateRepository.delete(roommate);
        return "success";
    }

    private Roommate getRoommate(Long roommateId) {
        return roommateRepository.findById(roommateId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOMMATE_NOT_FOUND));
    }
}
