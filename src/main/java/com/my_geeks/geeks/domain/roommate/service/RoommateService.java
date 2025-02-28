package com.my_geeks.geeks.domain.roommate.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.my_geeks.geeks.customResponse.BaseResponse;
import com.my_geeks.geeks.domain.roommate.entity.Roommate;
import com.my_geeks.geeks.domain.roommate.entity.RoommateBookmark;
import com.my_geeks.geeks.domain.roommate.entity.enumeration.RoommateStatus;
import com.my_geeks.geeks.domain.roommate.repository.RoommateBookmarkRepository;
import com.my_geeks.geeks.domain.roommate.repository.RoommateRepository;
import com.my_geeks.geeks.domain.roommate.requestDto.DeleteBookmarkReq;
import com.my_geeks.geeks.domain.roommate.responseDto.GetApplyList;
import com.my_geeks.geeks.domain.roommate.responseDto.GetBookmarkListRes;
import com.my_geeks.geeks.domain.user.entity.User;
import com.my_geeks.geeks.domain.user.repository.UserRepository;
import com.my_geeks.geeks.exception.CustomException;
import com.my_geeks.geeks.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.my_geeks.geeks.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoommateService {
    private final UserRepository userRepository;
    private final RoommateRepository roommateRepository;

    private final RoommateBookmarkRepository roommateBookmarkRepository;

    @Transactional
    public String send(Long senderId, Long receiverId, Long matchingPointId) {
        // TODO: 만약에 받는 사람이 보내는 사람에게 이미 신청을 했다면?
        if(roommateRepository.existsBySenderIdAndReceiverId(senderId, receiverId)) {
            throw new CustomException(ALREADY_APPLY_ROOMMATE_ERROR);
        }

        if(roommateRepository.existsBySenderIdAndReceiverId(receiverId, senderId)) {
            throw new CustomException(ALREADY_RECEIVE_APPLY_ROOMMATE_ERROR);
        }

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

    @Transactional
    public String deleteReceiveApply(Long roommateId) {
        Roommate roommate = getRoommate(roommateId);
        roommateRepository.delete(roommate);
        // TODO: 거절 알림
        return "success";
    }

    // TODO: 룸메이트 수락 알림 추가
    @Transactional
    public String acceptReceiveApply(Long roommateId) {
        Roommate roommate = getRoommate(roommateId);

        Long senderId = roommate.getSenderId();
        Long receiverId = roommate.getReceiverId();

        // 서로 상대방 PK 저장, 프로필 비활성화
        User sender = getUser(senderId);
        User receiver = getUser(receiverId);


        // TODO: 두 사용자에게 roommateId가 있는지 확인하도록 변경
//        if(!roommateRepository.existsAcceptRoommate(RoommateStatus.ACCEPT, senderId, receiverId).isEmpty()) {
//            throw new CustomException(ALREADY_ACCEPT_ROOMMATE_ERROR);
//        }
        // 보내거나 받는 사람중 이미 룸메이트가 되어 있다면 오류 발생
        if(sender.getRoommateId() != null || receiver.getRoommateId() != null) {
            throw new CustomException(ALREADY_ACCEPT_ROOMMATE_ERROR);
        }

        // 룸메이트 상태 변경 -> ACCEPT
        roommate.setStatusToAccept();

        sender.changeRoommate(roommateId);
        receiver.changeRoommate(roommateId);

        // 나머지 요청 삭제
        roommateRepository.deleteOtherApply(roommateId, senderId, receiverId);
        return "success";
    }

    @Transactional
    public String roommateSever(Long userId) {
        User user = getUser(userId);
        User myRoommate = roommateRepository.getRoommateUser(user.getRoommateId(), userId);

        // TODO: roommateId로 조회하여 룸메 끊기
        Roommate roommate = getRoommate(user.getRoommateId());

        user.severRoommate();
        myRoommate.severRoommate();

        roommateRepository.delete(roommate);
        return "success";
    }

    @Transactional
    public String bookmarkRoommate(Long myId, Long opponentId, Long matchingPointId) {
        if(roommateBookmarkRepository.existsByMyIdAndOpponentId(myId, opponentId)) {
            throw new CustomException(ALREADY_BOOKMARK_ROOMMATE_ERROR);
        }

        RoommateBookmark bookmark = new RoommateBookmark(myId, opponentId, matchingPointId);
        roommateBookmarkRepository.save(bookmark);
        return "success";
    }

    public List<GetBookmarkListRes> getBookmarkList(Long myId) {
        return roommateBookmarkRepository.getBookmarkList(myId);
    }

    @Transactional
    public String deleteSingleBookmark(Long myId, Long opponentId) {
        roommateBookmarkRepository.deleteByMyIdAndOpponentId(myId, opponentId);
        return "success";
    }

    @Transactional
    public String deleteBulkBookmark(DeleteBookmarkReq req) {
        List<Long> bookmarkIds = req.getBookmarkIds();
        roommateBookmarkRepository.deleteAllByBookmarkId(bookmarkIds);
        return "success";
    }

    @Transactional
    public String homecomingAlarm(Long userId) {
        User user = getUser(userId);
        Roommate roommate = getRoommate(user.getRoommateId());

        User myRoommate = getUser(roommate.getSenderId() == userId ? roommate.getReceiverId() : roommate.getSenderId());

        if(!myRoommate.getNotifyAllow().isService()) {
            throw new CustomException(ROOMMATE_SERVICE_NOTIFY_NOT_ALLOW);
        }

        sendPushMessage("homecoming", myRoommate.getFcmToken());
        return "success";
    }

    private Roommate getRoommate(Long roommateId) {
        return roommateRepository.findById(roommateId)
                .orElseThrow(() -> new CustomException(ROOMMATE_NOT_FOUND));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    private void sendPushMessage(String pushType, String token) {
        Message message = null;

        // TODO: 각 푸시 알림 로그 만들기
        if(pushType.equals("homecoming")) {
            message = Message.builder()
                    .setNotification(
                            Notification.builder()
                                    .setTitle("룸메이트 귀가 알림")
                                    .setBody("룸메이트가 곧 귀가해요!")
                                    .build()
                    )
                    .setToken(token)
                    .build();
        } else if(pushType.equals("supply")) {
            message = Message.builder()
                    .setNotification(
                            Notification.builder()
                                    .setTitle("새로운 룸메이트 신청")
                                    .setBody("'~'님이 나에게 룸메이트를 신청했어요!")
                                    .build()
                    )
                    .setToken(token)
                    .build();
        }

        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            throw new CustomException(FIREBASE_MESSAGE_ERROR);
        }
    }
}
