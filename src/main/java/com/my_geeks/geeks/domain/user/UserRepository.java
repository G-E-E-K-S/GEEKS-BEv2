package com.my_geeks.geeks.domain.user;

import com.my_geeks.geeks.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
