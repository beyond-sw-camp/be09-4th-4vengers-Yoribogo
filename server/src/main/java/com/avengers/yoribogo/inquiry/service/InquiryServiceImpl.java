package com.avengers.yoribogo.inquiry.service;

import com.avengers.yoribogo.answer.domain.Answer;
import com.avengers.yoribogo.common.Role;
import com.avengers.yoribogo.common.Status;
import com.avengers.yoribogo.common.Visibility;
import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.common.exception.ExceptionDTO;
import com.avengers.yoribogo.inquiry.domain.Inquiry;
import com.avengers.yoribogo.inquiry.dto.InquiryDTO;
import com.avengers.yoribogo.inquiry.dto.InquiryOnlyDTO;
import com.avengers.yoribogo.inquiry.repository.InquiryRepository;
import com.avengers.yoribogo.user.domain.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Comparator.comparing;

@Service
public class InquiryServiceImpl implements InquiryService {

    private final InquiryRepository inquiryRepository;
    private final ModelMapper modelMapper;

    @PersistenceContext
    private final EntityManager entityManager;


    @Autowired
    public InquiryServiceImpl(InquiryRepository inquiryRepository,
                              ModelMapper modelMapper, EntityManager entityManager) {
        this.inquiryRepository = inquiryRepository;
        this.modelMapper = modelMapper;
        this.entityManager = entityManager;
        this.modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true);
    }

    @Override
    public List<InquiryOnlyDTO> findInquiryOnly(Integer userId, String status) {
        try {
            List<InquiryOnlyDTO> result = modelMapper.map(inquiryRepository.findAll()
                    .stream()
                    .filter(row -> (row.getInquiryStatus()==Status.ACTIVE) &&
                            (status == null || status.toUpperCase().equals(row.getAnswerStatus().name())) &&
                            (userId == null || row.getUser().getUserId().toString().equals(userId.toString())))
                    .toList(), new TypeToken<List<InquiryOnlyDTO>>() {}.getType());
            return result;
        } catch (Exception e) {
            ExceptionDTO.of(ErrorCode.NOT_FOUND_INQUIRY);
            return null;
        }
    }

    @Override
    public List<Inquiry> findInquiry(Integer userId, String status) {
        try {
            List<Inquiry> result = inquiryRepository.findAll()
                    .stream()
                    .filter(row -> (row.getInquiryStatus()==Status.ACTIVE) &&
                            (status == null || status.toUpperCase().equals(row.getAnswerStatus().name())) &&
                            (userId == null || row.getUser().getUserId().toString().equals(userId.toString())))
                    .toList();

            return result;
        } catch (Exception e) {
            ExceptionDTO.of(ErrorCode.NOT_FOUND_INQUIRY);
            return null;
        }
    }

    @Override
    public Inquiry findInquiryById(Integer inquiryId) {
        try {
            return inquiryRepository.findById(inquiryId).get();
        } catch (Exception e) {
            ExceptionDTO.of(ErrorCode.NOT_FOUND_INQUIRY);
            return null;
        }
    }

    @Override
    public Inquiry insertInquiry(InquiryDTO newInquiry) {
        try {
            Inquiry created = modelMapper.map(newInquiry, Inquiry.class);
            if (created.getInquiryVisibility() == null) created.setInquiryVisibility(Visibility.PUBLIC);
            created.setInquiryCreatedAt(LocalDateTime.now());
            created.setInquiryStatus(Status.ACTIVE);
            created.setInquiryCreatedAt(LocalDateTime.now().withNano(0));
            created.setAnswers(0);
            created.setAnswerStatus(Status.PENDING);
            created.setUser(modelMapper.map(newInquiry.getUser(), UserEntity.class));
            return inquiryRepository.save(created);
        } catch (Exception e) {
            ExceptionDTO.of(ErrorCode.NOT_FOUND_INQUIRY);
            return null;
        }
    }

    @Override
    public Inquiry updateInquiry(InquiryDTO modifyInquiry) {
        try {
            Inquiry tmp = modelMapper.map(modifyInquiry, Inquiry.class);
            tmp.setInquiryCreatedAt(LocalDateTime.now());
            tmp.setUser(modelMapper.map(modifyInquiry.getUser(), UserEntity.class));
            return inquiryRepository.saveAndFlush(tmp);
        } catch (Exception e) {
            ExceptionDTO.of(ErrorCode.NOT_FOUND_INQUIRY);
            return null;
        }
    }

    @Override
    public Inquiry removeInquiry(int id) {
        try {
            Inquiry del = inquiryRepository.findById(id).get();
            del.setInquiryStatus(Status.INACTIVE);
            return inquiryRepository.saveAndFlush(del);
        } catch (Exception e) {
            ExceptionDTO.of(ErrorCode.NOT_FOUND_INQUIRY);
            return null;
        }
    }

    @Transactional
    @Override
    public void changeStatus(int inquiryId) {
        try {
            entityManager.flush();
            Inquiry tmp = inquiryRepository.findById(inquiryId).get();

            entityManager.refresh(tmp);

            List<Answer> answers =  tmp.getAnswer();
            answers.sort(comparing(Answer::getAnswerId).reversed());

            tmp.setAnswers(answers.size());

            Role writer = (answers.size() > 0) ? answers.get(0).getWriterType() : null;

            if (writer == Role.ADMIN) tmp.setAnswerStatus(Status.ANSWERED);
            else tmp.setAnswerStatus(Status.PENDING);

            Inquiry result = inquiryRepository.saveAndFlush(tmp);
        } catch (Exception e) {
            ExceptionDTO.of(ErrorCode.NOT_FOUND_INQUIRY);
        }
    }
}
