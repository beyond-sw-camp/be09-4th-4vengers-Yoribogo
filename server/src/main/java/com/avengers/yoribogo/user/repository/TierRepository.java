package com.avengers.yoribogo.user.repository;

import com.avengers.yoribogo.user.domain.Tier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TierRepository extends JpaRepository<Tier, Long> {
    Optional<Tier> findTopByTierCriteriaLessThanEqualOrderByTierCriteriaDesc(Long userLikes);
}
