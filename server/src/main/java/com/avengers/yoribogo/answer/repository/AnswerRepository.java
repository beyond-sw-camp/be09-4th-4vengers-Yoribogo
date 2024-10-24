package com.avengers.yoribogo.answer.repository;

import com.avengers.yoribogo.answer.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
}
