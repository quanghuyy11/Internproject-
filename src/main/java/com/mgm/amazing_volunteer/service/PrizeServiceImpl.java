package com.mgm.amazing_volunteer.service;

import com.mgm.amazing_volunteer.dto.prize.PrizeDto;
import com.mgm.amazing_volunteer.exception.PrizeException;
import com.mgm.amazing_volunteer.exception.PrizeRequestException;
import com.mgm.amazing_volunteer.model.Prize;
import com.mgm.amazing_volunteer.repository.PrizeRepository;
import com.mgm.amazing_volunteer.util.constants.DeleteResponse;

import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PrizeServiceImpl implements  PrizeService {

    private final PrizeRepository prizeRepository;
    private List<Prize> listPrizeExited;
    private final ModelMapper mapper;

    public PrizeServiceImpl(PrizeRepository prizeRepository, ModelMapper mapper) {
        this.prizeRepository = prizeRepository;
        this.mapper = mapper;
    }

    @Override
    public List<PrizeDto> getAll() {
        final List<Prize> prizeList = prizeRepository.findAll();
        return prizeList.stream().map(prize -> mapper.map(prize, PrizeDto.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ResponseEntity<?> deletePrizeById(Long id) {
        Prize prize;
        Optional<Prize> prizeOptional =  prizeRepository.findById(id);
        List<Prize> prizeListHasRequestPending = prizeRepository.getPrizeHasRequestPending();
        List<Prize> prizeListHasRequestAccepted = prizeRepository.getPrizeHasRequestAccepted();
        List<Prize> prizeListHasRequestDeclined = prizeRepository.getPrizeHasRequestDeclined();
        if (prizeOptional.isEmpty()) {
            throw  new PrizeException("This prize is not exist!");
        }

        prize = prizeOptional.get();
        // Check Prize has request is PENDING
        for (Prize prizes: prizeListHasRequestPending) {
            if(prizes.getId().equals(prize.getId())){
                throw  new PrizeException("This prize has a request, can't be deleted!");
            }
        }
        // Check Prize has request is ACCEPTED
        for (Prize prizes: prizeListHasRequestAccepted) {
            if(prizes.getId().equals(prize.getId())){
                throw  new PrizeException("This prize has a request, can't be deleted!");
            }
        }
        // Check Prize has request is DECLINED
        for (Prize prizes: prizeListHasRequestDeclined) {
            if(prizes.getId().equals(prize.getId())){
                throw  new PrizeException("This prize has a request, can't be deleted!");
            }
        }

        prizeRepository.deleteById(prize.getId());
        return new ResponseEntity<>(new DeleteResponse("Delete Success"),HttpStatus.OK);
    }

    @Override
    @Transactional
    public PrizeDto createAndUpdatePrize(PrizeDto prizeDto) {
        listPrizeExited = prizeRepository.findAll();
        if(listPrizeExited.isEmpty()){
                prizeRepository.save(mapper.map(prizeDto, Prize.class));
        }
        for (Prize prize: listPrizeExited) {
                if(prizeDto.getId().toString().trim().equals("0")){
                    createPrize(prizeDto);
                    break;
                }
                else if(prize.getId().equals(prizeDto.getId())) {
                    updatePrize(prizeDto);
                    break;
                }
        }
        return prizeDto;
    }

    @Override
    @Transactional
    public PrizeDto createPrize(PrizeDto prizeDto) {
        listPrizeExited = prizeRepository.findAll();
        for (Prize prize: listPrizeExited) {
            if (prize.getPrize_name().toLowerCase().trim().equals(prizeDto.getPrize_name().toLowerCase().trim()) && prize.getDescription().toLowerCase().trim().equals(prizeDto.getDescription().toLowerCase().trim())) {
                throw new PrizeException("Prize Name and Prize Description is existed!");
            }
        }
        prizeRepository.save(mapper.map(prizeDto, Prize.class));
        return prizeDto;
    }

    @Override
    @Transactional
    public PrizeDto updatePrize(PrizeDto prizeDto) {
        List<Prize> prizeListHasRequestPending = prizeRepository.getPrizeHasRequestPending();
        // Check Prize has request is PENDING
        for (Prize prize: prizeListHasRequestPending) {
            if(prize.getId().equals(prizeDto.getId())){
                throw  new PrizeRequestException("This prize has request pending , can not update!");
            }
        }
        for (Prize prize: listPrizeExited) {
            if (prize.getPrize_name().toLowerCase().trim().
                    equals(prizeDto.getPrize_name().toLowerCase().trim()) && prize.getDescription().toLowerCase().trim().
                    equals(prizeDto.getDescription().toLowerCase().trim()) && prize.getPoint_archive().equals(prizeDto.getPoint_archive())) {
                throw new PrizeException("Prize Name and Prize Description is existed!");
            }
        }
        // Update prize
        final Prize updatedPrize = prizeRepository.findById(prizeDto.getId())
                .map(prize -> {
                    prize.setDescription(prizeDto.getDescription());
                    prize.setPoint_archive(prizeDto.getPoint_archive());
                    prize.setPrize_name(prizeDto.getPrize_name());
                    return prizeRepository.save(prize);
                })
                .orElseGet(() -> {
                    prizeDto.setId(prizeDto.getId());
                    return prizeRepository.save(mapper.map(prizeDto, Prize.class));
                });
        return mapper.map(updatedPrize, PrizeDto.class);
    }
}
