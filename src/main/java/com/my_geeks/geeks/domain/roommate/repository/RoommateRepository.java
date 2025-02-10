package com.my_geeks.geeks.domain.roommate.repository;

import com.my_geeks.geeks.domain.roommate.entity.Roommate;
import com.my_geeks.geeks.domain.roommate.responseDto.GetApplyList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoommateRepository extends JpaRepository<Roommate, Long> {

    @Query("select new com.my_geeks.geeks.domain.roommate.responseDto.GetApplyList(" +
            "rm.id, u.id, u.nickname, u.major, u.studentNum, u.image, ud.smoke, mp.point, rm.createdDate) " +
            "from Roommate rm " +
            "join User u on u.id = rm.receiverId " +
            "join UserDetail ud on ud.id = rm.receiverId " +
            "join MatchingPoint mp on mp.id = rm.matchingPointId " +
            "where rm.senderId = :senderId")
    List<GetApplyList> getSendList(@Param("senderId") Long senderId);

    @Query("select new com.my_geeks.geeks.domain.roommate.responseDto.GetApplyList(" +
            "rm.id, u.id, u.nickname, u.major, u.studentNum, u.image, ud.smoke, mp.point, rm.createdDate) " +
            "from Roommate rm " +
            "join User u on u.id = rm.senderId " +
            "join UserDetail ud on ud.id = rm.senderId " +
            "join MatchingPoint mp on mp.id = rm.matchingPointId " +
            "where rm.receiverId = :receiverId")
    List<GetApplyList> getReceiveList(@Param("receiverId") Long receiverId);
}
