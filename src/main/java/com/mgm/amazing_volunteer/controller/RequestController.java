package com.mgm.amazing_volunteer.controller;

import com.mgm.amazing_volunteer.dto.request.RequestFromUserDto;
import com.mgm.amazing_volunteer.service.RequestService;

import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.security.Principal;

import static com.mgm.amazing_volunteer.util.ValidUtils.getMessageBindingResult;

@RestController
@RequestMapping("/request")
public class RequestController {

    private final RequestService requestService;

    @Autowired
    public RequestController(final RequestService requestService) {
        this.requestService = requestService;
    }


    @ApiOperation(value = "Get all request")
    @GetMapping("/admin")
    public ResponseEntity<?> getAllRequests() {
        return new ResponseEntity<>(requestService.getAllRequest(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get all request of a user")
    @GetMapping("")
    public ResponseEntity<?> getRequestsByUser(Principal principal) {
        String userEmail = principal.getName();
        return new ResponseEntity<>(requestService.getAllRequestByUserEmail(userEmail), HttpStatus.OK);
    }

    @ApiOperation(value = "Create a new request")
    @PostMapping("")
    public ResponseEntity<?> createNewRequest(Principal principal, @Valid @RequestBody final RequestFromUserDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String msg = getMessageBindingResult(bindingResult);
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
        String userEmail = principal.getName();
        return new ResponseEntity<>(requestService.createRequest(userEmail, requestDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Cancel a request")
    @DeleteMapping("/{requestId}/cancel")
    public ResponseEntity<?> cancelRequest(@PathVariable final Long requestId) {
        requestService.cancelRequest(requestId);
        return new ResponseEntity<>("Cancel request success!", HttpStatus.OK);
    }

    @ApiOperation(value = "Decline user's request")
    @PutMapping("/admin/{requestId}/decline")
    public ResponseEntity<?> declineRequest(@PathVariable final Long requestId) {
        requestService.denyRequest(requestId);
        return new ResponseEntity<>("Decline request success!", HttpStatus.OK);
    }

    @ApiOperation(value = "Accept user's request")
    @PutMapping("/admin/{requestId}/accept")
    public ResponseEntity<?> acceptRequest(@PathVariable final Long requestId) {
        requestService.acceptRequest(requestId);
        return new ResponseEntity<>("Accept request success!", HttpStatus.OK);
    }

    @ApiOperation(value = "Get Total Point")
    @GetMapping("/total-point")
    public ResponseEntity<?> getToTalPoint(Principal principal) {
        String userEmail = principal.getName();
        return new ResponseEntity<>(requestService.getTotalPoint(userEmail), HttpStatus.OK);
    }

    @ApiOperation(value = "Get All Request by Year")
    @GetMapping("/admin/{year}")
    public ResponseEntity<?> getAllRequestYear(@PathVariable final int year) {
        return new ResponseEntity<>(requestService.getAllRequestByYear(year), HttpStatus.OK);
    }
}
