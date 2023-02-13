package com.mgm.amazing_volunteer.service;


import com.mgm.amazing_volunteer.dto.request.RequestDto;
import com.mgm.amazing_volunteer.dto.request.RequestFromUserDto;

import java.util.List;

public interface RequestService {

    RequestDto createRequest(String userEmail, RequestFromUserDto request);

    List<RequestDto> getAllRequestByUserEmail(String email);

    List<RequestDto> getAllRequest();

    void cancelRequest(Long requestId);

    void denyRequest(Long requestId);

    void acceptRequest(Long requestId);

    int getTotalPoint(String email);

    List<RequestDto> getAllRequestByYear(int year);
}
