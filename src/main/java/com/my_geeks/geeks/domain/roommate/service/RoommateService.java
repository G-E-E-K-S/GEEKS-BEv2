package com.my_geeks.geeks.domain.roommate.service;

import com.google.api.core.ApiFuture;
import com.google.firebase.messaging.*;
import com.my_geeks.geeks.actuator.ActuatorCounter;
import com.my_geeks.geeks.customResponse.BaseResponse;
import com.my_geeks.geeks.domain.roommate.entity.Roommate;
import com.my_geeks.geeks.domain.roommate.entity.RoommateBookmark;
import com.my_geeks.geeks.domain.roommate.entity.enumeration.RoommateStatus;
import com.my_geeks.geeks.domain.roommate.repository.RoommateBookmarkRepository;
import com.my_geeks.geeks.domain.roommate.repository.RoommateRepository;
import com.my_geeks.geeks.domain.roommate.requestDto.DeleteBookmarkReq;
import com.my_geeks.geeks.domain.roommate.responseDto.GetApplyList;
import com.my_geeks.geeks.domain.roommate.responseDto.GetBookmarkListRes;
import com.my_geeks.geeks.domain.user.entity.PushDetail;
import com.my_geeks.geeks.domain.user.entity.User;
import com.my_geeks.geeks.domain.user.entity.enumeration.NotifyType;
import com.my_geeks.geeks.domain.user.repository.PushDetailRepository;
import com.my_geeks.geeks.domain.user.repository.UserRepository;
import com.my_geeks.geeks.exception.CustomException;
import com.my_geeks.geeks.exception.ErrorCode;
import com.my_geeks.geeks.redis.CacheRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.my_geeks.geeks.domain.user.entity.enumeration.NotifyType.*;
import static com.my_geeks.geeks.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoommateService {
    private final UserRepository userRepository;
    private final RoommateRepository roommateRepository;

    private final PushDetailRepository pushDetailRepository;

    private final RoommateBookmarkRepository roommateBookmarkRepository;

    private final CacheRepository cacheRepository;

    private final ActuatorCounter actuatorCounter;

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

        User receiver = getUser(receiverId);

        if(receiver.getNotifyAllow().isRoommateNotify()) {
            // 룸메 신청 알림 보내기
            sendPushMessage(receiver, ROOMMATE_NEW_APPLY);
        }
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

        User sender = getUser(roommate.getSenderId());
        if(sender.getNotifyAllow().isRoommateNotify()) {
            // 거절 알림
            sendPushMessage(sender, ROOMMATE_MATCHING_FAIL);
        }
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

        // TODO: 매칭 알림
        if(sender.getNotifyAllow().isRoommateNotify()) {
            sendPushMessage(sender, ROOMMATE_MATCHING_SUCCESS);
        }
        if(receiver.getNotifyAllow().isRoommateNotify()) {
            sendPushMessage(receiver, ROOMMATE_MATCHING_SUCCESS);
        }
        return "success";
    }

    @CacheEvict(value = "UserCache", key = "#userId", cacheManager = "cacheManager")
    @Transactional
    public String roommateSever(Long userId) {
        User user = getUser(userId);
        User myRoommate = roommateRepository.getRoommateUser(user.getRoommateId(), userId);

        // roommateId로 조회하여 룸메 끊기
        Roommate roommate = getRoommate(user.getRoommateId());

        user.severRoommate();
        myRoommate.severRoommate();

        // TODO: 룸메이트 끊기 알림
        if(user.getNotifyAllow().isRoommateNotify()) {
            sendPushMessage(user, ROOMMATE_SEVER);
        }
        if(myRoommate.getNotifyAllow().isRoommateNotify()) {
            sendPushMessage(myRoommate, ROOMMATE_SEVER);
        }

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
        User user = cacheRepository.getUser(userId);
        Roommate roommate = getRoommate(user.getRoommateId());

        User myRoommate = cacheRepository.getUser(roommate.getSenderId() == userId ? roommate.getReceiverId() : roommate.getSenderId());

        if(!myRoommate.getNotifyAllow().isServiceNotify()) {
            throw new CustomException(ROOMMATE_SERVICE_NOTIFY_NOT_ALLOW);
        }

        sendPushMessage(myRoommate, HOMECOMING);
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

    private void sendPushMessage(User user, NotifyType type) {
        Message message = null;
        String title = "";
        String body = "";

        // TODO: 각 푸시 알림 로그 만들기
        if(type.equals(HOMECOMING)) {
            title = "룸메이트 귀가 알림";
            body = user.getNickname() + "님이 곧 귀가해요!";
        } else if(type.equals(ROOMMATE_NEW_APPLY)) {
            title = "새로운 룸메이트 신청";
            body = user.getNickname() + "님이 나에게 룸메이트를 신청했어요!";
        } else if(type.equals(ROOMMATE_MATCHING_SUCCESS)) {
            title = "룸메이트 매칭 성공!";
            body = user.getNickname() + "님과 룸메이트가 맺어졌어요!";
        } else if(type.equals(ROOMMATE_SEVER)) {
            title = "룸메이트가 끊겼어요";
            body = user.getNickname() + "님과의 룸메이트가 끊어졌어요.";
        }

        message = Message.builder()
                .setWebpushConfig(WebpushConfig.builder()
                        .putHeader("Urgency", "high")
                        .build())
                .putData("title", title)
                .putData("body", body)
                .setToken(user.getFcmToken())
                .build();

        FirebaseMessaging.getInstance().sendAsync(message);

        PushDetail pushDetail = PushDetail.builder()
                .title(title)
                .body(body)
                .userId(user.getId())
                .build();

        pushDetailRepository.save(pushDetail);
//        try {
//            FirebaseMessaging.getInstance().send(message, true);
//
//            PushDetail pushDetail = PushDetail.builder()
//                    .title(title)
//                    .body(body)
//                    .userId(userId)
//                    .build();
//            pushDetailRepository.save(pushDetail);
//        } catch (FirebaseMessagingException e) {
//            throw new CustomException(FIREBASE_MESSAGE_ERROR);
//        }
    }
}
