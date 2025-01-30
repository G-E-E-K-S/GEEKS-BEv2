package com.my_geeks.geeks.domain.user.repository;

import com.my_geeks.geeks.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(@Param("email") String email);
}
