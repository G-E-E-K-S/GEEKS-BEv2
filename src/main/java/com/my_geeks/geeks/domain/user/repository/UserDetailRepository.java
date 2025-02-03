package com.my_geeks.geeks.domain.user.repository;

import com.my_geeks.geeks.domain.user.entity.UserDetail;
import com.my_geeks.geeks.domain.user.entity.enumeration.Dormitory;
import com.my_geeks.geeks.domain.user.entity.enumeration.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {

    @Query("select ud from UserDetail ud " +
            "left join User u on ud.id = u.id " +
            "where ud.id != :userId and u.gender = :gender and u.dormitory = :dormitory")
    List<UserDetail> findByGenderAndDormitory(@Param("userId") Long userId,
                                  @Param("gender")Gender gender,
                                  @Param("dormitory")Dormitory dormitory);
}
