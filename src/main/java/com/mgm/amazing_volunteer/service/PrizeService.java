package com.mgm.amazing_volunteer.service;

import com.mgm.amazing_volunteer.dto.prize.PrizeDto;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PrizeService {

    List<PrizeDto> getAll();

    ResponseEntity<?> deletePrizeById(Long id);

    PrizeDto createAndUpdatePrize(PrizeDto prizeDto);

    PrizeDto createPrize(PrizeDto prizeDto);

    PrizeDto updatePrize(PrizeDto prizeDto);
}
