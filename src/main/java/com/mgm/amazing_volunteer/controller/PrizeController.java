package com.mgm.amazing_volunteer.controller;

import com.mgm.amazing_volunteer.dto.prize.PrizeDto;
import com.mgm.amazing_volunteer.service.PrizeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prizes")
public class PrizeController {

    private final PrizeService prizeService;

    @Autowired
    public PrizeController(PrizeService prizeService) {
        this.prizeService = prizeService;
    }

    @GetMapping("/")
    public ResponseEntity<List<PrizeDto>> getAllPrize() {
        final List<PrizeDto> prizeDtoList = prizeService.getAll();
        return new ResponseEntity<>(prizeDtoList, HttpStatus.OK);
    }

    @PostMapping("/create-update")
    public ResponseEntity<PrizeDto> createAndUpdatePrize(@RequestBody final PrizeDto prizeDto) {
        prizeService.createAndUpdatePrize(prizeDto);
        return new ResponseEntity<>(prizeDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePrize(@PathVariable final Long id) {
        return this.prizeService.deletePrizeById(id);
    }
}
