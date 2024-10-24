package com.avengers.yoribogo.mainquestion.service;

import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.common.exception.ExceptionDTO;
import com.avengers.yoribogo.mainquestion.domain.MainQuestion;
import com.avengers.yoribogo.mainquestion.dto.MainQuestionDTO;
import com.avengers.yoribogo.mainquestion.repository.MainQuestionRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MainQuestionServiceImpl implements MainQuestionService {

    private final MainQuestionRepository mainQuestionRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public MainQuestionServiceImpl(MainQuestionRepository choiceRepository,
                                   ModelMapper modelMapper) {
        this.mainQuestionRepository = choiceRepository;
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true);
    }
    
    @Override
    public List<MainQuestion> findQuestion(int userId) {
        try{
            return mainQuestionRepository.findAll()
                    .stream()
                    .filter(question -> question.getUserId() == userId)
                    .toList();
        } catch (Exception e) {
            ExceptionDTO.of(ErrorCode.NOT_FOUND_MAIN_QUESTION); // not found
            return null;
        }
    }

    @Override
    public MainQuestion insertQuestion(MainQuestionDTO newQuestion) {
        try {
            return mainQuestionRepository.save(
                    modelMapper.map(newQuestion, MainQuestion.class));
        } catch (Exception e) {
            ExceptionDTO.of(ErrorCode.NOT_FOUND_MAIN_QUESTION);    // insert fail
            return null;
        }
    }

    @Override
    public MainQuestion updateQuestion(MainQuestionDTO modifyQuestion) {
        try {
            return mainQuestionRepository.saveAndFlush(
                    modelMapper.map(modifyQuestion, MainQuestion.class));
        } catch (Exception e) {
            ExceptionDTO.of(ErrorCode.NOT_FOUND_MAIN_QUESTION);    // update fail
            return null;
        }
    }

    @Override
    public boolean removeQuestion(int id) {
        try {
            mainQuestionRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            ExceptionDTO.of(ErrorCode.NOT_FOUND_MAIN_QUESTION);    // delete fail
            return false;
        }
        
    }
    
}
