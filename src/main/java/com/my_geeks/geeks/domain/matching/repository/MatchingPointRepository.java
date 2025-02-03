package com.my_geeks.geeks.domain.matching.repository;

import com.my_geeks.geeks.domain.matching.entity.MatchingPoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingPointRepository extends JpaRepository<MatchingPoint, Long> {
}
