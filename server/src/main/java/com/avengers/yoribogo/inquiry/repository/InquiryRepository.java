package com.avengers.yoribogo.inquiry.repository;

import com.avengers.yoribogo.inquiry.domain.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InquiryRepository extends JpaRepository<Inquiry, Integer> {
}
