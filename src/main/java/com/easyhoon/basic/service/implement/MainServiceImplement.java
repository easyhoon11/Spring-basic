package com.easyhoon.basic.service.implement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.easyhoon.basic.dto.request.PatchNicknameRequestDto;
import com.easyhoon.basic.dto.request.PostUserRequestDto;
import com.easyhoon.basic.dto.request.SingInRequestDto;
import com.easyhoon.basic.dto.response.DeleteUserResponseDto;
import com.easyhoon.basic.dto.response.PatchNicknameResponseDto;
import com.easyhoon.basic.dto.response.PostUserResponseDto;
import com.easyhoon.basic.dto.response.ResponseDto;
import com.easyhoon.basic.dto.response.SignInResponseDto;
import com.easyhoon.basic.entity.UserEntity;
import com.easyhoon.basic.provider.JwtProvider;
import com.easyhoon.basic.repository.UserRepository;
import com.easyhoon.basic.service.MainService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
// description: @Component - 해당 클래스를 java bean으로 등록하여 Spring이 인스턴스 생성을 알아서 할 수 있도록 하는 어노테이션 //
// description: @Service - @Component와 동일한 작업을 수행하지만 가독성을 위해 Service라는 이름을 가짐 //
@Service
public class MainServiceImplement implements MainService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    // description: PasswordEncoder - 비밀번호를 안전하게 암호화하고 검증하는 인터페이스 //
    // description: BCryptPasswordEncoder - BCrypt 해시 알고리즘을 사용하는 PasswordEncoder 구현 클래스 //
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
            // description: 비밀번호 암호화 작업//
            // description: 1. dto로부터 평문의 비밀번호(암호화할 문자열)를 가져온다 //
            String password = dto.getPassword();
            // description: 2. PasswordEncoder의 인스턴스의 encode() 메서드로 평문을 암호화 //
            String encodedPassword = passwordEncoder.encode(password);
            // description: 3. dto에 다시 주입 //
            dto.setPassword(encodedPassword);
            
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

    @Override
    public ResponseEntity<? super SignInResponseDto> signIn(SingInRequestDto dto) {

        SignInResponseDto responseBody = null;

        try {
            
            String email = dto.getEmail();

            // description: 1. dto로 받은 email을 이용하여 데이터베이스에서 조회 //
            UserEntity userEntity = userRepository.findByEmail(email);
            // description: 2. email에 해당하는 레코드가 존재하는지 확인 //
            if(userEntity == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDto("SF","Sign In Failed"));
            // description: 3. userEntity에서 암호화 되어 있는 password 추출 //
            String encodedPassword = userEntity.getPassword();
            // description: 4. dto에서 평문의 password 추출 //
            String password = dto.getPassword();
            // description: 5. 암호화되어 있는 password와 평문의 password를 비교 //
            // description: matches() - 평문의 문자열과 암호화된 문자열을 비교 //
            boolean isMatched = passwordEncoder.matches(password, encodedPassword);
            if(!isMatched) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDto("SF","Sign In Failed"));

            // description: 6. 토큰 생성 //
            String token = jwtProvider.create(email);

            responseBody = new SignInResponseDto("SU","SUCCESS", token);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("DBE","데이터 베이스 에러"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    
}
