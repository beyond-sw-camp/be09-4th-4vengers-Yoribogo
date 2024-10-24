package com.avengers.yoribogo.choice.service;

import com.avengers.yoribogo.choice.repository.ChoiceRepository;
import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.common.exception.ExceptionDTO;
import com.avengers.yoribogo.choice.domain.Choice;
import com.avengers.yoribogo.choice.dto.ChoiceDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChoiceServiceImpl implements ChoiceService {

    private final ChoiceRepository choiceRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ChoiceServiceImpl(ChoiceRepository choiceRepository,
                             ModelMapper modelMapper) {
        this.choiceRepository = choiceRepository;
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true);
    }

    @Override
    public List<Choice> findChoice(int mainQuestionId) {
        try{
            return choiceRepository.findAll()
                    .stream()
                    .filter(choice -> choice.getMainQuestionId() == mainQuestionId)
                    .toList();
        } catch (Exception e) {
            ExceptionDTO.of(ErrorCode.NOT_FOUND_CHOICE); // not found
            return null;
        }
    }

    @Override
    public Choice insertChoice(ChoiceDTO newChoice) {
        try {
            return choiceRepository.save(
                    modelMapper.map(newChoice, Choice.class));
        } catch (Exception e) {
            ExceptionDTO.of(ErrorCode.NOT_FOUND_CHOICE);    // insert fail
            return null;
        }
    }

    @Override
    public Choice updateChoice(ChoiceDTO modifyChoice) {
        try {
            return choiceRepository.saveAndFlush(
                    modelMapper.map(modifyChoice, Choice.class));
        } catch (Exception e) {
            ExceptionDTO.of(ErrorCode.NOT_FOUND_CHOICE);    // update fail
            return null;
        }
    }

    @Override
    public boolean removeChoice(int id) {
        try {
            choiceRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            ExceptionDTO.of(ErrorCode.NOT_FOUND_CHOICE);    // delete fail
            return false;
        }
    }
}
