package com.my_geeks.geeks.domain.roommate.repository;

import com.my_geeks.geeks.domain.roommate.entity.RoommateBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoommateBookmarkRepository extends JpaRepository<RoommateBookmark, Long> {
    boolean existsByMyIdAndOpponentId(Long myId, Long opponentId);
}
