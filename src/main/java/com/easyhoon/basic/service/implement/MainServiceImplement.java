package com.easyhoon.basic.service.implement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.easyhoon.basic.dto.request.PostUserRequestDto;
import com.easyhoon.basic.dto.response.PostUserResponseDto;
import com.easyhoon.basic.entity.UserEntity;
import com.easyhoon.basic.repository.UserRepository;
import com.easyhoon.basic.service.MainService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
// description: @Component - 해당 클래스를 java bean으로 등록하여 Spring이 인스턴스 생성을 알아서 할 수 있도록 하는 어노테이션 //
// description: @Service - @Component와 동일한 작업을 수행하지만 가독성을 위해 Service라는 이름을 가짐 //
@Service
public class MainServiceImplement implements MainService {

    private final UserRepository userRepository;

    @Override
    public String getMethod() {
        return "This Method is getMethod";
    }

    @Override
    public ResponseEntity<? super PostUserResponseDto> postUser(PostUserRequestDto dto) {
    
        // INSERT INTO user (email, password, nickname, tel_number, address, addressDetail)
        // VALUES (dto.getEmail(), dto.getPassword(), ...);

        // description: Create 작업 순서 (INSERT) //
        // description: 1. Entity 인스턴스 생성 //
        UserEntity userEntity = new UserEntity(dto);
        // description: 2. repository의 save 메서드 사용 //
        userRepository.save(userEntity);

        return ResponseEntity.status(HttpStatus.OK).body(new PostUserResponseDto("su", "SUCCESS"));
    }
    
}
