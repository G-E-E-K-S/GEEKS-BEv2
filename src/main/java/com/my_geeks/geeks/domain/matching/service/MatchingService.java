package com.my_geeks.geeks.domain.matching.service;

import com.my_geeks.geeks.domain.matching.entity.MatchingPoint;
import com.my_geeks.geeks.domain.matching.repository.MatchingPointRepository;
import com.my_geeks.geeks.domain.matching.responseDto.GetMatchingDetailRes;
import com.my_geeks.geeks.domain.matching.responseDto.GetPointRes;
import com.my_geeks.geeks.domain.matching.responseDto.GetOpponentRes;
import com.my_geeks.geeks.domain.matching.responseDto.UserDetailAndPoint;
import com.my_geeks.geeks.domain.roommate.entity.Roommate;
import com.my_geeks.geeks.domain.roommate.entity.RoommateBookmark;
import com.my_geeks.geeks.domain.roommate.entity.enumeration.RoommateStatus;
import com.my_geeks.geeks.domain.roommate.repository.RoommateBookmarkRepository;
import com.my_geeks.geeks.domain.roommate.repository.RoommateRepository;
import com.my_geeks.geeks.domain.user.entity.User;
import com.my_geeks.geeks.domain.user.entity.UserDetail;
import com.my_geeks.geeks.domain.user.entity.enumeration.Outing;
import com.my_geeks.geeks.domain.user.repository.UserDetailRepository;
import com.my_geeks.geeks.domain.user.repository.UserRepository;
import com.my_geeks.geeks.domain.user.responseDto.GetUserDetailRes;
import com.my_geeks.geeks.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.my_geeks.geeks.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchingService {

    private final UserRepository userRepository;

    private final UserDetailRepository userDetailRepository;

    private final MatchingPointRepository matchingPointRepository;

    private final RoommateRepository roommateRepository;

    private final RoommateBookmarkRepository roommateBookmarkRepository;

    @Transactional
    public GetPointRes getPoints(Long userId) {
        boolean exists = userDetailRepository.existsById(userId);
        List<GetPointRes.OpponentInfo> opponentInfos = new ArrayList<>();

        if(exists) {
            opponentInfos.addAll(matchingPointRepository.getPointList(userId));
        }

        return new GetPointRes(exists, opponentInfos);
    }

    @Transactional
    public GetPointRes getPointsTop3(Long userId) {
        boolean exists = userDetailRepository.existsById(userId);
        List<GetPointRes.OpponentInfo> opponentInfos = new ArrayList<>();

        if(exists) {
            opponentInfos.addAll(matchingPointRepository.getHomePointList(userId));
        }

        return new GetPointRes(exists, opponentInfos);
    }

    @Transactional
    public void calculate(Long userId, UserDetail myDetail) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        List<MatchingPoint> points = new ArrayList<>();
        List<UserDetail> otherDetails = userDetailRepository.findByGenderAndDormitory(userId, user.getGender(), user.getDormitory());

        otherDetails.forEach(
                otherDetail -> {
                    int count = 0;
                    Long otherId = otherDetail.getId();

                    if(myDetail.getSmoke().equals(otherDetail.getSmoke())) count++;
                    if(myDetail.getHabit().equals(otherDetail.getHabit())) count++;
                    if(myDetail.getEar().equals(otherDetail.getEar())) count++;
                    if(myDetail.getActivityTime().equals(otherDetail.getActivityTime())) count++;
                    if(myDetail.getCleaning().equals(otherDetail.getCleaning())) count++;
                    if(myDetail.getTendency().equals(otherDetail.getTendency())) count++;
                    if(myDetail.getOuting().equals(Outing.INSIDE) ||
                            otherDetail.getOuting().equals(Outing.INSIDE)) count++;

                    MatchingPoint matchingPoint = MatchingPoint.builder()
                            .smallUserId(userId < otherId ? userId : otherId)
                            .largeUserId(userId < otherId ? otherId : userId)
                            .point(point(count))
                            .build();

                    points.add(matchingPoint);
                }
        );

        matchingPointRepository.saveAll(points);
    }

    @Transactional
    public void recalculate(Long userId, UserDetail myDetail) {
        List<MatchingPoint> points = new ArrayList<>();
        List<UserDetailAndPoint> detailAndPoint = matchingPointRepository.findByUserDetailAndPoint(userId);

        for (UserDetailAndPoint userDetailAndPoint : detailAndPoint) {
            System.out.println(userDetailAndPoint.toString());

            int count = 0;
            Long otherId = userDetailAndPoint.getOtherId();

            if(myDetail.getSmoke().toString().equals(userDetailAndPoint.getSmoke())) count++;
            if(myDetail.getHabit().toString().equals(userDetailAndPoint.getHabit())) count++;
            if(myDetail.getEar().toString().equals(userDetailAndPoint.getEar())) count++;
            if(myDetail.getActivityTime().toString().equals(userDetailAndPoint.getActivityTime())) count++;
            if(myDetail.getCleaning().toString().equals(userDetailAndPoint.getCleaning())) count++;
            if(myDetail.getTendency().toString().equals(userDetailAndPoint.getTendency())) count++;
            if(myDetail.getOuting().equals(Outing.INSIDE) ||
                    userDetailAndPoint.getOuting().toString().equals(Outing.INSIDE)) count++;

            MatchingPoint matchingPoint = MatchingPoint.builder()
                    .smallUserId(userId < otherId ? userId : otherId)
                    .largeUserId(userId < otherId ? otherId : userId)
                    .point(point(count))
                    .build();
            matchingPoint.setId(userDetailAndPoint.getMatchingPointId());

            points.add(matchingPoint);
        }

        matchingPointRepository.saveAll(points);
    }

    public GetMatchingDetailRes getMatchingDetail(Long myId, Long opponentId, Long matchingId) {
        User user = getUser(myId);
        GetUserDetailRes myDetail = getUserDetail(myId);
        GetUserDetailRes opponentDetail = getUserDetail(opponentId);

        GetOpponentRes opponentRes = matchingPointRepository.findMatchingDetail(myId, opponentId, matchingId);

        RoommateStatus roommateStatus = RoommateStatus.NONE;
        Optional<Roommate> roommate = roommateRepository.findBySenderIdAndReceiverId(myId, opponentId);
        Optional<RoommateBookmark> bookmark = roommateBookmarkRepository.findByMyIdAndOpponentId(myId, opponentId);

        if(roommate.isPresent()) {
            roommateStatus = roommate.get().getStatus();
        } else if(user.getMyRoommateId() == opponentId) {
            roommateStatus = RoommateStatus.ACCEPT;
        }
        return GetMatchingDetailRes.builder()
                .roommateStatus(roommateStatus)
                .bookmarkStatus(bookmark.isPresent())
                .opponent(opponentRes)
                .myDetail(myDetail)
                .opponentDetail(opponentDetail)
                .build();
    }

    private GetUserDetailRes getUserDetail(Long userId) {
        UserDetail userDetail = userDetailRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        return GetUserDetailRes.from(userDetail);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    private int point(int count) {
        int point = 0;

        switch (count) {
            case 0:
                break;
            case 1:
                point = 10;
                break;
            case 2:
                point = 25;
                break;
            case 3:
                point = 40;
                break;
            case 4:
                point = 55;
                break;
            case 5:
                point = 70;
                break;
            case 6:
                point = 85;
                break;
            case 7:
                point = 100;
                break;
        }
        return point;
    }
}
