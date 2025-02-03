package com.my_geeks.geeks.domain.user.repository;

import com.my_geeks.geeks.domain.user.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {
}
