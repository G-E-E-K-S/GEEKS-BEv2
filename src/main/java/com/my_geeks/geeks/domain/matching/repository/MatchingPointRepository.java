package com.my_geeks.geeks.domain.matching.repository;

import com.my_geeks.geeks.domain.matching.entity.MatchingPoint;
import com.my_geeks.geeks.domain.matching.responseDto.GetPointRes;
import com.my_geeks.geeks.domain.matching.responseDto.GetOpponentRes;
import com.my_geeks.geeks.domain.matching.responseDto.UserDetailAndPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchingPointRepository extends JpaRepository<MatchingPoint, Long> {

    @Query(nativeQuery = true,
            value = """
                    select mp.matching_point_id, u.user_id, u.nickname, u.major, u.student_num, u.introduction, ud.smoke, u.image, mp.point 
                    from matching_point as mp
                    join user as u on u.user_id = mp.large_user_id
                    join user_detail as ud on ud.user_detail_id = mp.large_user_id
                    where mp.small_user_id = :userId and u.is_open = true
                    
                    union all
                    
                    select mp.matching_point_id, u.user_id, u.nickname, u.major, u.student_num, u.introduction, ud.smoke, u.image, mp.point
                    from matching_point as mp
                    join user as u on u.user_id = mp.small_user_id 
                    join user_detail as ud on ud.user_detail_id = mp.small_user_id 
                    where mp.large_user_id = :userId and u.is_open = true
                    order by point desc
                    """
    )
    List<GetPointRes.OpponentInfo> getPointList(@Param("userId") Long userId);

    @Query(nativeQuery = true,
            value = """
                    SELECT mp.matching_point_id, u.user_id, u.nickname, u.major, u.student_num, u.introduction, ud.smoke, u.image, mp.point
                    FROM matching_point as mp
                    LEFT JOIN user as u ON (
                        CASE
                            WHEN mp.small_user_id = :userId THEN u.user_id = mp.large_user_id
                            WHEN mp.large_user_id = :userId THEN u.user_id = mp.small_user_id
                        END
                    )
                    LEFT JOIN user_detail as ud ON (
                        CASE
                            WHEN mp.small_user_id = :userId THEN ud.user_detail_id = mp.large_user_id
                            WHEN mp.large_user_id = :userId THEN ud.user_detail_id = mp.small_user_id
                        END
                    )
                    WHERE (mp.small_user_id = :userId OR mp.large_user_id = :userId) and u.is_open = true
                    ORDER BY mp.point DESC
                    """
    )
    List<GetPointRes.OpponentInfo> test(@Param("userId") Long userId);

    @Query(nativeQuery = true,
            value = """
                    select a.matching_point_id, a.user_id, a.nickname, a.major, a.student_num, a.introduction, a.smoke, a.image, a.point
                    from (
                        select mp.matching_point_id, u.user_id, u.nickname, u.major, u.student_num, u.introduction, ud.smoke, u.image, mp.point 
                        from matching_point as mp
                        join user as u on u.user_id = mp.large_user_id
                        join user_detail as ud on ud.user_detail_id = mp.large_user_id
                        where mp.small_user_id = :userId and u.is_open = true
                        union all
                        select mp.matching_point_id, u.user_id, u.nickname, u.major, u.student_num, u.introduction, ud.smoke, u.image, mp.point
                        from matching_point as mp
                        join user as u on u.user_id = mp.small_user_id 
                        join user_detail as ud on ud.user_detail_id = mp.small_user_id 
                        where mp.large_user_id = :userId and u.is_open = true                                   
                    ) as a
                    order by a.point desc
                    limit 3
                    """
    )
    List<GetPointRes.OpponentInfo> getHomePointList(@Param("userId") Long userId);

    @Query(nativeQuery = true,
            value = """
                    select a.matching_point_id, a.user_detail_id, a.smoke, a.habit, a.ear, a.activity_time, a.outing, a.cleaning, a.tendency, a.point
                    from (
                        select mp.matching_point_id, ud.user_detail_id, ud.smoke, ud.habit, ud.ear, ud.activity_time, ud.outing, ud.cleaning, ud.tendency, mp.point 
                        from matching_point as mp
                        join user_detail as ud on ud.user_detail_id = mp.large_user_id
                        where mp.small_user_id = :userId
                        union all
                        select mp.matching_point_id, ud.user_detail_id, ud.smoke, ud.habit, ud.ear, ud.activity_time, ud.outing, ud.cleaning, ud.tendency, mp.point
                        from matching_point as mp
                        join user_detail as ud on ud.user_detail_id = mp.small_user_id 
                        where mp.large_user_id = :userId                                   
                    ) as a
                    """
    )
    List<UserDetailAndPoint> findByUserDetailAndPoint(@Param("userId") Long userId);

    @Query("select new com.my_geeks.geeks.domain.matching.responseDto.GetOpponentRes(" +
            "u.nickname, u.major, u.studentNum, u.introduction, u.image, mp.point) from MatchingPoint mp " +
            "join User u on u.id = :opponentId " +
            "where mp.id = :matchingId")
    GetOpponentRes findMatchingDetail(@Param("myId") Long myId, @Param("opponentId") Long opponentId,
                                      @Param("matchingId") Long matchingId);

    MatchingPoint findBySmallUserIdAndLargeUserId(Long smallUserId, Long largeUserId);
}
