package com.easyhoon.basic.service;

import org.springframework.http.ResponseEntity;

import com.easyhoon.basic.dto.request.PostUserRequestDto;
import com.easyhoon.basic.dto.response.PostUserResponseDto;

public interface MainService {
    
    String getMethod();
    ResponseEntity<? super PostUserResponseDto> postUser(PostUserRequestDto dto);

}
