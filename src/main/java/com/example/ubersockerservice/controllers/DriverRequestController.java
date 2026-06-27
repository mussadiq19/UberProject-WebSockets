package com.example.ubersockerservice.controllers;

import com.example.ubersockerservice.dtos.RideRequestDto;
import com.example.ubersockerservice.dtos.RideResponseDto;
import com.example.ubersockerservice.dtos.UpdateBookingRequestDto;
import com.example.ubersockerservice.dtos.UpdateBookingResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/api/socket")
public class DriverRequestController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RestTemplate restTemplate;

    public DriverRequestController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.restTemplate = new RestTemplate();
    }

    @PostMapping("/newride")
    public ResponseEntity<Boolean> raiseRideRequest(@RequestBody RideRequestDto requestDto){
        sendDriverNewRideRequest(requestDto);
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    }
    public void sendDriverNewRideRequest(RideRequestDto requestDto){
        simpMessagingTemplate.convertAndSend( "/topic/rideRequest",requestDto);
    }
    @MessageMapping("/rideResponse/{userId}")
    public synchronized void rideResponseHandler(@DestinationVariable String userId, RideResponseDto rideResponseDto){
        System.out.println(rideResponseDto.getResponse()+" "+ userId);
        UpdateBookingRequestDto requestDto = UpdateBookingRequestDto.builder()
                .driverId(Long.parseLong(userId))
                .bookingStatus("SCHEDULED")
                .build();
      ResponseEntity<UpdateBookingResponseDto>result= this.restTemplate.postForEntity("http://localhost:8000/api/v1/booking/"+rideResponseDto.bookingId,requestDto,UpdateBookingResponseDto.class);
        System.out.println(result.getStatusCode());
    }
}
