package com.mgm.amazing_volunteer.service;

import com.mgm.amazing_volunteer.common.EventResponse;
import com.mgm.amazing_volunteer.dto.event.EventDetailDto;
import com.mgm.amazing_volunteer.dto.event.EventDto;
import com.mgm.amazing_volunteer.dto.submission.SubmissionDto;
import com.mgm.amazing_volunteer.dto.submission.SubmittedUser;
import com.mgm.amazing_volunteer.exception.EmptyFieldException;
import com.mgm.amazing_volunteer.exception.NotFoundException;
import com.mgm.amazing_volunteer.jotform.JSONException;
import com.mgm.amazing_volunteer.jotform.JotFormServices;
import com.mgm.amazing_volunteer.model.Event;
import com.mgm.amazing_volunteer.model.Submission;
import com.mgm.amazing_volunteer.model.User;
import com.mgm.amazing_volunteer.repository.EventRepository;
import com.mgm.amazing_volunteer.repository.SubmissionRepository;
import com.mgm.amazing_volunteer.repository.UserRepository;
import com.mgm.amazing_volunteer.util.StringHelper;
import com.mgm.amazing_volunteer.util.constants.JotformConstants;
import com.mgm.amazing_volunteer.util.enums.EventStatus;

import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.mgm.amazing_volunteer.jotform.JotFormServices.getFormTitle;

@Service
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final SubmissionRepository submissionRepository;
    private final ModelMapper mapper;
    private final String apiKey;

    @Autowired
    public EventServiceImpl(final EventRepository eventRepository, final UserRepository userRepository, final SubmissionRepository submissionRepository, final ModelMapper mapper, @Value("${amazing-volunteer.jotform.api-key}") final String apiKey) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.submissionRepository = submissionRepository;
        this.mapper = mapper;
        this.apiKey = apiKey;
        this.mapper.typeMap(Submission.class, SubmissionDto.class).addMappings(m -> m.map(src -> src.getUser().getEmail(),
                SubmissionDto::setEmail));
    }

    @Override
    public void updateAllEventStatus() {
        log.info("update event service running");
        final List<Event> eventList = eventRepository.findNotDeletedEvents();

        final Map<String, String> formStatusMap = JotFormServices.getFormStatus(apiKey);
        eventList.forEach(event -> {
            updateEventStatus(event, formStatusMap);
        });
    }

    @Override
    public void updateEventStatusByEventId(Long eventId) {
        log.info("Updating event {} status...", eventId);
        final Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event {} not found", eventId)));
        final Map<String, String> formStatusMap = JotFormServices.getFormStatus(apiKey);
        updateEventStatus(event, formStatusMap);
    }

    @Transactional
    protected void updateEventStatus(Event event, Map<String, String> formStatusMap) {
        try {
            LocalDate now = Instant.now().atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDate();
            LocalDate date = event.getEventDate().toInstant().atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDate();
            final boolean isEventPassed = now.isAfter(date);
            String formStatus = "";
            if (!isEventPassed) {
                if (StringUtils.isEmpty(event.getJotformUrl())) {
                    return;
                }

                formStatus = formStatusMap.getOrDefault(event.getJotformUrl(), "");
                if (event.getStatus().equals(EventStatus.IN_PROGRESS) && formStatus.equals(JotformConstants.FORM_DISABLED)) {
                    event.setStatus(EventStatus.DONE);
                }
                if (event.getStatus().equals(EventStatus.DONE) && formStatus.equals(JotformConstants.FORM_ENABLE)) {
                    event.setStatus(EventStatus.IN_PROGRESS);
                }
            }
            if (isEventPassed || formStatus.equals(JotformConstants.FORM_DISABLED)) {
                event.setStatus(EventStatus.DONE);
            }
            eventRepository.save(event);
        } catch (EmptyFieldException e) {
            log.error(String.valueOf(e));
        }
    }

    @Override
    @Transactional
    public void createSubmission(final Event event, final User user) {
        final Submission newSubmission = new Submission();
        newSubmission.setEvent(event);
        newSubmission.setUser(user);
        var createdSubmission = submissionRepository.saveAndFlush(newSubmission);
        var eventSubmission = event.getSubmissions();
        eventSubmission.add(createdSubmission);
        event.setSubmissions(eventSubmission);
        eventRepository.saveAndFlush(event);
    }

    @Override
    @Transactional
    public void updateJotformSubmissionByEventId(final Long eventId) {
        log.info("get event {} submissions running...", eventId);
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(String.format("Event {} not found", eventId)));
        try {
            log.info("Getting event: {} submissions...", event.getTitle());
            final List<SubmittedUser> eventSubmittedUsers = submissionRepository.getSubmittedUserByEventId(event.getId());
            final Long eventFormId = StringHelper.getFormIdFromJotformUrl(event.getJotformUrl());
            final List<SubmittedUser> listEmail = JotFormServices.getFormSubmissionUsers(apiKey, eventFormId);
            final Map<String, Boolean> submittedUserMap = new HashMap<>();
            for (SubmittedUser s : eventSubmittedUsers) {
                var email = s.getEmail();
                if (submittedUserMap.getOrDefault(email, null) == null) {
                    submittedUserMap.put(email, s.getIsParticipated());
                }
            }
            for (int i = 0; i < listEmail.size(); i++) {
                if (submittedUserMap.getOrDefault(listEmail.get(i).getEmail(), null) == null) {
                    log.info("Insert new submission: {}...", listEmail.get(i));
                    final Optional<User> userSubmit = userRepository.findById(listEmail.get(i).getEmail());
                    userSubmit.ifPresent(user -> createSubmission(event, user));
                    submittedUserMap.put(listEmail.get(i).getEmail(), false);
                }
            }
        } catch (JSONException | EmptyFieldException e) {
            log.error(String.valueOf(e));
        }
    }

    @Override
    @Transactional
    public void updateEventSubmission(final EventDetailDto eventDetailDto) {
        final Event event = eventRepository.findById(eventDetailDto.getEvent_id()).orElseThrow(() -> new NotFoundException("Event not found"));
        final List<Submission> eventSubmissions = event.getSubmissions();
        eventDetailDto.getSubmissions().forEach(submissionDto -> {
            final Submission submission = eventSubmissions.stream()
                    .filter(eventSubmission -> eventSubmission.getId().equals(submissionDto.getId()))
                    .findFirst().orElseThrow(() -> new NotFoundException("Submission not found"));
            User user = submission.getUser();
            submission.setParticipated(submissionDto.isParticipated());
            submissionRepository.save(submission);
        });
        eventRepository.save(event);
    }

    public String getEventTitle(final String jotformUrl) {
        String joformTitle = "";
        if (jotformUrl == null || jotformUrl == "") {
            log.info("INVALID JOTFORM URL");
        } else {
            try {
                StringBuilder formId = new StringBuilder();
                for (int i = jotformUrl.length() - 1; i >= 0; i--) {
                    if (jotformUrl.charAt(i) == '/') {
                        break;
                    }
                    formId.append(jotformUrl.charAt(i));
                }
                joformTitle = getFormTitle(apiKey, Long.valueOf(String.valueOf(formId.reverse())));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return joformTitle;
    }

    @Override
    public EventResponse getAllEventByYear(final int year, final String email,
                                           final Integer pageNo, final Integer pageSize, final String sortBy, final String status) {
        Pageable paging = null;
        if (sortBy.equals("event_date_asc")) {
            paging = PageRequest.of(pageNo - 1, pageSize, Sort.by("event_date").ascending());
        }
        if (sortBy.equals("event_date_desc")) {
            paging = PageRequest.of(pageNo - 1, pageSize, Sort.by("event_date").descending());
        }
        if (year == 0) throw new EmptyFieldException("Request must contain a year");

        updateAllEventStatus();

        Page<Event> eventList = null;

        final EventResponse eventResponse = new EventResponse();

        final Map<String, Supplier<Page<Event>>> map = new HashMap<>();

        Pageable finalPaging = paging;
        map.put("ALL", () -> eventRepository.findAllByYear(year, finalPaging));
        map.put("DELETED", () -> eventRepository.findAllByStatusDelete(year, finalPaging));
        map.put(EventStatus.UPCOMING.name(), () -> eventRepository.findAllByYearAndStatus(status, year, finalPaging));
        map.put(EventStatus.DONE.name(), () -> eventRepository.findAllByYearAndStatus(status, year, finalPaging));
        map.put(EventStatus.IN_PROGRESS.name(), () -> eventRepository.findAllByYearAndStatus(status, year, finalPaging));

        eventList = map.get(status).get();
        eventList = new PageImpl<>(eventList.toList(), eventList.getPageable(), eventList.getTotalElements());

        final Map<String, Integer> eventSubmissionCount = JotFormServices.countEventSubmission(apiKey);

        List<EventDto> eventDtoList = eventList.stream().map(event -> {
            final EventDto eventDto = mapper.map(event, EventDto.class);
            final String eventUrl = event.getJotformUrl();
            if (!event.getIsDeleted()) {
                final List<SubmittedUser> submittedUser = submissionRepository.getSubmittedUserByEventId(event.getId());
                eventDto.setParticipated(submittedUser.stream().anyMatch(i -> i.getEmail().equals(email) && i.getIsParticipated()));
            }
            int limit = -1;
            int count = 0;
            try {
                limit = JotFormServices.getLitmitSubmission(apiKey, StringHelper.getFormIdFromJotformUrl(eventUrl));
                count = eventSubmissionCount.getOrDefault(eventUrl, 0);
                eventDto.setLimitSubmission(limit);
                eventDto.setCountSubmission(count);
            } catch (JSONException | EmptyFieldException e) {
                log.error(e.getMessage());
            } finally {
                eventDto.setLimitSubmission(limit);
                eventDto.setCountSubmission(count);
            }
            return eventDto;
        }).collect(Collectors.toList());
        eventResponse.setContent(eventDtoList);
        eventResponse.setPageNo(eventList.getNumber() + 1);
        eventResponse.setPageSize(eventList.getSize());
        eventResponse.setTotalElements(eventList.getTotalElements());
        eventResponse.setTotalPages(eventList.getTotalPages());
        return eventResponse;
    }

    @Override
    public EventDto getEventById(final Long id) {
        updateEventStatusByEventId(id);
        updateJotformSubmissionByEventId(id);
        final Event event = eventRepository.findById(id).orElseThrow(() -> new NotFoundException("Event not found"));
        return mapper.map(event, EventDto.class);
    }

    @Override
    @Transactional
    public EventDto createEvent(final EventDto eventDto) {
        Event event = mapper.map(eventDto, Event.class);
        if (event.getJotformUrl().isEmpty() && !event.getStatus().equals(EventStatus.UPCOMING)) {
            throw new RuntimeException("Events without url must have UPCOMING status");
        }
        eventRepository.save(event);
        return eventDto;
    }

    @Transactional
    public EventDto updateById(final EventDto eventDto, final Long id) {
        final Event updatedEvent = eventRepository.findById(id)
                .map(event -> {
                    event.setJotformUrl(eventDto.getJotformUrl());
                    event.setTitle(eventDto.getTitle());
                    event.setPoint(eventDto.getPoint());
                    event.setStatus(eventDto.getStatus());
                    event.setEventDate(eventDto.getEventDate());
                    return eventRepository.save(event);
                })
                .orElseGet(() -> {
                    eventDto.setId(id);
                    return eventRepository.save(mapper.map(eventDto, Event.class));
                });

        return mapper.map(updatedEvent, EventDto.class);
    }

    @Override
    @Transactional
    public void deleteById(final Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new NotFoundException("Event not found"));
        if (event.getStatus().equals(EventStatus.DONE)) {
            throw new RuntimeException("Events with status DONE cannot be deleted");
        }
        eventRepository.deleteById(id);
    }
}
