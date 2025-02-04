package com.my_geeks.geeks.domain.matching.repository;

import com.my_geeks.geeks.domain.matching.entity.MatchingPoint;
import com.my_geeks.geeks.domain.matching.responseDto.GetPointRes;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchingPointRepository extends JpaRepository<MatchingPoint, Long> {

    @Query(nativeQuery = true,
            value = """
                    select a.user_id, a.nickname, a.major, a.student_num, a.introduction, a.smoke, a.point
                    from (
                        select u.user_id, u.nickname, u.major, u.student_num, u.introduction, ud.smoke, mp.point 
                        from matching_point as mp
                        left join user as u on u.user_id = mp.large_user_id
                        left join user_detail as ud on ud.user_detail_id = mp.large_user_id
                        where mp.small_user_id = :userId
                        union
                        select u.user_id, u.nickname, u.major, u.student_num, u.introduction, ud.smoke, mp.point
                        from matching_point as mp
                        left join user as u on u.user_id = mp.small_user_id 
                        left join user_detail as ud on ud.user_detail_id = mp.small_user_id 
                        where mp.large_user_id = :userId                                   
                    ) as a
                    order by a.point desc
                    """
    )
    List<GetPointRes.OpponentInfo> getPointList(@Param("userId") Long userId);
}
