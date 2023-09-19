package com.easyhoon.basic.service;

import org.springframework.http.ResponseEntity;

import com.easyhoon.basic.dto.request.PatchNicknameRequestDto;
import com.easyhoon.basic.dto.request.PostUserRequestDto;
import com.easyhoon.basic.dto.response.DeleteUserResponseDto;
import com.easyhoon.basic.dto.response.PatchNicknameResponseDto;
import com.easyhoon.basic.dto.response.PostUserResponseDto;

public interface MainService {
    
    String getMethod();
    ResponseEntity<? super PostUserResponseDto> postUser(PostUserRequestDto dto);
    ResponseEntity<? super PatchNicknameResponseDto> patchNickname(PatchNicknameRequestDto dto);
    ResponseEntity<? super DeleteUserResponseDto> deleteUser(String email);

}
