package com.my_geeks.geeks.domain.roommate.repository;

import com.my_geeks.geeks.domain.roommate.entity.RoommateBookmark;
import com.my_geeks.geeks.domain.roommate.responseDto.GetBookmarkListRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoommateBookmarkRepository extends JpaRepository<RoommateBookmark, Long> {
    boolean existsByMyIdAndOpponentId(Long myId, Long opponentId);

    @Query("select new com.my_geeks.geeks.domain.roommate.responseDto.GetBookmarkListRes(" +
            "rb.id, u.id, u.nickname, u.major, u.studentNum, u.image, ud.smoke, mp.point, rb.createdDate) " +
            "from RoommateBookmark rb " +
            "join User u on u.id = rb.opponentId " +
            "join UserDetail ud on ud.id = rb.opponentId " +
            "join MatchingPoint mp on mp.id = rb.matchingPointId " +
            "where rb.myId = :myId and u.isOpen = true")
    List<GetBookmarkListRes> getBookmarkList(@Param("myId") Long myId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from RoommateBookmark rm where rm.id in :ids")
    void deleteAllByBookmarkId(@Param("ids") List<Long> ids);
}
