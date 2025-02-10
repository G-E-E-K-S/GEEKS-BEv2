package com.my_geeks.geeks.domain.roommate.repository;

import com.my_geeks.geeks.domain.roommate.entity.Roommate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoommateRepository extends JpaRepository<Roommate, Long> {
}
