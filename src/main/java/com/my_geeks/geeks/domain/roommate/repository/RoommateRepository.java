package com.my_geeks.geeks.domain.roommate.repository;

import com.my_geeks.geeks.domain.roommate.entity.Roommate;
import com.my_geeks.geeks.domain.roommate.responseDto.GetSendList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoommateRepository extends JpaRepository<Roommate, Long> {

    @Query("select new com.my_geeks.geeks.domain.roommate.responseDto.GetSendList(" +
            "rm.id, u.id, u.nickname, u.major, u.studentNum, u.image, ud.smoke, mp.point) " +
            "from Roommate rm " +
            "join User u on u.id = rm.receiverId " +
            "join UserDetail ud on ud.id = rm.receiverId " +
            "join MatchingPoint mp on mp.id = rm.matchingPointId " +
            "where rm.senderId = :senderId")
    List<GetSendList> getSendList(@Param("senderId") Long senderId);
}
