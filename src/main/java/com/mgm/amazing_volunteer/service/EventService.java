package com.mgm.amazing_volunteer.service;

import com.mgm.amazing_volunteer.common.EventResponse;
import com.mgm.amazing_volunteer.dto.event.EventDetailDto;
import com.mgm.amazing_volunteer.dto.event.EventDto;
import com.mgm.amazing_volunteer.model.Event;
import com.mgm.amazing_volunteer.model.User;

public interface EventService {

    void updateAllEventStatus();

    void updateEventStatusByEventId(Long eventId);

    void createSubmission(Event event, User user);

    void updateJotformSubmissionByEventId(Long eventId);

    void updateEventSubmission(EventDetailDto eventDetailDto);

    EventResponse getAllEventByYear(int year, String email, Integer pageNo, Integer pageSize, String sortBy, String status);

    EventDto createEvent(EventDto eventDto);

    EventDto getEventById(Long id);

    EventDto updateById(EventDto eventDto, Long id);

    void deleteById(Long id);

    String getEventTitle(String jotformUrl);

}
