package com.easyhoon.basic.service.implement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.easyhoon.basic.dto.request.PatchNicknameRequestDto;
import com.easyhoon.basic.dto.request.PostUserRequestDto;
import com.easyhoon.basic.dto.response.DeleteUserResponseDto;
import com.easyhoon.basic.dto.response.PatchNicknameResponseDto;
import com.easyhoon.basic.dto.response.PostUserResponseDto;
import com.easyhoon.basic.dto.response.ResponseDto;
import com.easyhoon.basic.entity.UserEntity;
import com.easyhoon.basic.repository.UserRepository;
import com.easyhoon.basic.service.MainService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
// description: @Component - 해당 클래스를 java bean으로 등록하여 Spring이 인스턴스 생성을 알아서 할 수
// 있도록 하는 어노테이션 //
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

        // INSERT INTO user (email, password, nickname, tel_number, address,
        // addressDetail)
        // VALUES (dto.getEmail(), dto.getPassword(), ...);
        try {
            // description: Create 작업 순서 (INSERT) //
            // description: 1. Entity 인스턴스 생성 //
            UserEntity userEntity = new UserEntity(dto);
            // description: 2. repository의 save 메서드 사용 //
            userRepository.save(userEntity);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("DBE", "데이터베이스 에러"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new PostUserResponseDto("su", "SUCCESS"));
    }

    @Override
    public ResponseEntity<? super PatchNicknameResponseDto> patchNickname(PatchNicknameRequestDto dto) {

        try {
            // UPDATE user SET nickname = dto.getNickname WHERE email = dto.getEmail();

            // description: Update 작업 순서 (UPDATE) //
            // description: 1. Entity 인스턴스 조회 //
            // description: findById() - primary key를 사용하여 레코드를 검색하는 메서드 //
            // SELECT * FROM user WHERE email = ??;
            UserEntity userEntity = userRepository.findById(dto.getEmail()).get();
            // description: 2. Entity 인스턴스 수정 //
            userEntity.updateNickname(dto.getNickname());
            // description: 3. repository의 save 메서드 사용 //
            // description: save() - Entity 객체를 테이블에 저장하는 메서드 //
            // description: 만약 해당 인스턴스의 ID값과 동일한 레코드가 존재하면 해당 레코드를 수정 //
            // description: 그렇지 않다면 레코드를 생성
            userRepository.save(userEntity);
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("DBE", "데이터베이스 에러"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new PatchNicknameResponseDto("SU", "SUCCESS"));
    }

    @Override
    public ResponseEntity<? super DeleteUserResponseDto> deleteUser(String email) {

        // DELETE FROM user WHERE email = email;

        try{
            // description: Delete 작업 순서 (DELETE) //
            // description: 1. repository의 deleteById 메서드 사용 //
            userRepository.deleteById(email);
        } catch(Exception exception){
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("DBE","데이터베이스 에러"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new DeleteUserResponseDto("SU","성공"));
    }

    
}
