package com.mgm.amazing_volunteer.controller;

import com.mgm.amazing_volunteer.common.EventResponse;
import com.mgm.amazing_volunteer.dto.event.EventDetailDto;
import com.mgm.amazing_volunteer.dto.event.EventDto;
import com.mgm.amazing_volunteer.security.JwtAuthenticateProvider;
import com.mgm.amazing_volunteer.service.EventService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Collections;
import java.util.Map;

import static com.mgm.amazing_volunteer.util.ValidUtils.getMessageBindingResult;

@RestController
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;
    @Autowired
    private JwtAuthenticateProvider jwtAuthenticateProvider;

    @Autowired
    public EventController(final EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/")
    public EventResponse getAllEvent(@RequestParam final int year,
                                     @RequestHeader(value = "Authorization", required = false) final String header,
                                     @RequestParam(defaultValue = "1") Integer pageNo,
                                     @RequestParam(defaultValue = "5") Integer pageSize,
                                     @RequestParam(defaultValue = "event_date") String sortBy,
                                     @RequestParam(defaultValue = "") String status){
        final String token = header.substring(7);
        final String email = jwtAuthenticateProvider.getUsernameFromToken(token);
        return eventService.getAllEventByYear(year, email,pageNo, pageSize, sortBy,status);
    }

    @PostMapping("/admin/submission")
    public ResponseEntity<?> updateEventSubmission(@RequestBody @Valid final EventDetailDto eventDetailDto, final BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            String msg = getMessageBindingResult(bindingResult);
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
        eventService.updateEventSubmission(eventDetailDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/admin/")
    public ResponseEntity<?> createEvent(@RequestBody @Valid EventDto eventDto, final BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            String msg = getMessageBindingResult(bindingResult);
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
        EventDto newEventDto = eventService.createEvent(eventDto);
        return new ResponseEntity(newEventDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEvent(@PathVariable Long id) {
        EventDto eventDto = eventService.getEventById(id);
        return new ResponseEntity(eventDto, HttpStatus.OK);
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<?> updateEvent(@RequestBody @Valid EventDto eventDto, @PathVariable Long id, final BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            String msg = getMessageBindingResult(bindingResult);
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
        EventDto updatedEventDto = eventService.updateById(eventDto, id);
        return new ResponseEntity(updatedEventDto, HttpStatus.OK);
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<EventDto> deleteEvent(@PathVariable Long id) {
        eventService.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/title")
    public ResponseEntity<Map<String,String>> getEventTitle(@RequestBody EventDto eventDto) {
        String eventTitle = eventService.getEventTitle(eventDto.getJotformUrl());
        if (eventTitle == null || eventTitle == "") {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(Collections.singletonMap("title", eventTitle), HttpStatus.OK);
        }
    }
}
