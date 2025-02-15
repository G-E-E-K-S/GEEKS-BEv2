package com.my_geeks.geeks.domain.user.repository;

import com.my_geeks.geeks.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(@Param("email") String email);
    Boolean existsByNickname(@Param("nickname") String nickname);

    @Modifying(clearAutomatically = true)
    @Query("update User u set u.isOpen = false " +
            "where u.id = :senderId or u.id = :receiverId")
    void updateIsOpen(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);

    Optional<User> findByEmail(String email);
}
