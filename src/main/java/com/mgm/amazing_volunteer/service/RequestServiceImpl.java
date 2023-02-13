package com.mgm.amazing_volunteer.service;

import com.mgm.amazing_volunteer.dto.user.UserDto;
import com.mgm.amazing_volunteer.dto.request.RequestDto;
import com.mgm.amazing_volunteer.dto.request.RequestFromUserDto;
import com.mgm.amazing_volunteer.exception.EmptyFieldException;
import com.mgm.amazing_volunteer.exception.NotFoundException;
import com.mgm.amazing_volunteer.exception.RequestException;
import com.mgm.amazing_volunteer.model.BaseEntity;
import com.mgm.amazing_volunteer.model.Request;
import com.mgm.amazing_volunteer.model.User;
import com.mgm.amazing_volunteer.repository.PrizeRepository;
import com.mgm.amazing_volunteer.repository.RequestRepository;
import com.mgm.amazing_volunteer.repository.UserRepository;
import com.mgm.amazing_volunteer.util.enums.RequestStatus;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final PrizeRepository prizeRepository;
    private final ModelMapper mapper = new ModelMapper();

    public RequestServiceImpl(final RequestRepository requestRepository, final UserRepository userRepository, final PrizeRepository prizeRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.prizeRepository = prizeRepository;
        mapper.typeMap(Request.class, RequestDto.class).addMappings(mapper -> {
            mapper.map(BaseEntity::getCreatedAt, RequestDto::setCreateAt);
            mapper.map(Request::getUser, RequestDto::setUser);
        });
        Converter<String, Integer> userDtoPointConverter = u -> userRepository.getCurrentPointByEmail(u.getSource()).orElse(Integer.valueOf(0));
        mapper.typeMap(User.class, UserDto.class).addMappings(mapper -> {
            mapper.map(User::getEmail, UserDto::setEmail);
            mapper.map(User::getUsername, UserDto::setName);
            mapper.using(userDtoPointConverter).map(User::getEmail, UserDto::setPoint);
        });
    }

    @Override
    @Transactional
    public RequestDto createRequest(String userEmail, final RequestFromUserDto request) {
        var user = userRepository.findById(userEmail)
                .orElseThrow(() -> new NotFoundException(String.format("Can't find user has email: %s", userEmail)));
        var prize = prizeRepository.findById(request.getPrizeId())
                .orElseThrow(() -> new NotFoundException(String.format("Can't find prize has id: %s", request.getPrizeId())));
        int prizePoint = prize.getPoint_archive();
        int requestQuantity = request.getQuantity();
        int userPoint = userRepository.getCurrentPointByEmail(userEmail).orElse(0);
        if ((prizePoint * requestQuantity) > userPoint)
            throw new RequestException("Your points are not enough to make this request");
        Request newRequest = new Request();
        newRequest.setUser(user);
        newRequest.setPrize(prize);
        newRequest.setStatus(RequestStatus.PENDING);
        newRequest.setQuantity(requestQuantity);
        var createdRequest = requestRepository.save(newRequest);
        userRepository.save(user);
        return mapper.map(createdRequest, RequestDto.class);
    }

    @Override
    public List<RequestDto> getAllRequestByUserEmail(final String email) {
        var requests = requestRepository.findByUser_Email(email);
        return requests.stream().map(request -> mapper.map(request, RequestDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<RequestDto> getAllRequest() {
        var requests = requestRepository.findAllPendingRequest();
        return requests.stream().map(request -> mapper.map(request, RequestDto.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void cancelRequest(final Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Can't find request has id: " + requestId));
        if (request.getStatus().equals(RequestStatus.ACCEPTED)) {
            throw new RequestException("Request is accepted");
        } else if (request.getStatus().equals(RequestStatus.DECLINED)) {
            throw new RequestException("Request is declined");
        }
        requestRepository.deleteById(requestId);
    }

    @Override
    @Transactional
    public void denyRequest(final Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Can't find request has id: " + requestId));
        request.setStatus(RequestStatus.DECLINED);
        requestRepository.save(request);
    }

    @Override
    @Transactional
    public void acceptRequest(final Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Can't find request has id: " + requestId));
        request.setStatus(RequestStatus.ACCEPTED);
        requestRepository.save(request);
    }

    @Override
    public int getTotalPoint(final String email) {
        User user = userRepository.findById(email)
                .orElseThrow(() -> new NotFoundException("Can't find user by: "+email));
        return userRepository.getCurrentPointByEmail(email).orElse(0);
    }

    @Override
    public List<RequestDto> getAllRequestByYear(final int year) {
        if (year == 0) throw new EmptyFieldException("Request must contain a year");
        List<Request> requestList = requestRepository.findAllRequestByYear(year);
        return requestList.stream().map(request -> mapper.map(request, RequestDto.class)).collect(Collectors.toList());
    }
}
