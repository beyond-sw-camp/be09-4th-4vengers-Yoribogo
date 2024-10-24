package com.avengers.yoribogo.choice.repository;

import com.avengers.yoribogo.choice.domain.Choice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChoiceRepository extends JpaRepository<Choice, Integer> {
}
