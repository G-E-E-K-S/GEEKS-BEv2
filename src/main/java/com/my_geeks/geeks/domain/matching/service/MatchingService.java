package com.my_geeks.geeks.domain.matching.service;

import com.my_geeks.geeks.domain.matching.entity.MatchingPoint;
import com.my_geeks.geeks.domain.matching.repository.MatchingPointRepository;
import com.my_geeks.geeks.domain.matching.responseDto.GetPointRes;
import com.my_geeks.geeks.domain.user.entity.User;
import com.my_geeks.geeks.domain.user.entity.UserDetail;
import com.my_geeks.geeks.domain.user.entity.enumeration.Outing;
import com.my_geeks.geeks.domain.user.repository.UserDetailRepository;
import com.my_geeks.geeks.domain.user.repository.UserRepository;
import com.my_geeks.geeks.exception.CustomException;
import com.my_geeks.geeks.exception.ErrorCode;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.my_geeks.geeks.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchingService {

    private final UserRepository userRepository;

    private final UserDetailRepository userDetailRepository;

    private final MatchingPointRepository matchingPointRepository;

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
