package com.my_geeks.geeks.domain.user.repository;

import com.my_geeks.geeks.domain.user.entity.User;
import com.my_geeks.geeks.domain.user.entity.enumeration.Dormitory;
import com.my_geeks.geeks.domain.user.entity.enumeration.Gender;
import com.my_geeks.geeks.domain.user.responseDto.GetUserSearchRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(@Param("email") String email);
    Boolean existsByNickname(@Param("nickname") String nickname);

    @Modifying(clearAutomatically = true)
    @Query("update User u set u.isOpen = false " +
            "where u.id = :senderId or u.id = :receiverId")
    void updateIsOpen(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);

    Optional<User> findByEmail(String email);


    @Query("select new com.my_geeks.geeks.domain.user.responseDto.GetUserSearchRes(" +
            "u.id, u.nickname, u.major, u.studentNum, ud.smoke, u.image) from User u " +
            "join UserDetail ud on ud.id = u.id " +
            "where u.id != :userId " +
            "and u.nickname like concat('%', :keyword, '%') " +
            "and u.gender = :gender and u.dormitory = :dormitory")
    List<GetUserSearchRes> searchByKeyword(@Param("userId") Long userId,
                                           @Param("gender") Gender gender,
                                           @Param("dormitory") Dormitory dormitory,
                                           @Param("keyword") String keyword);
}
