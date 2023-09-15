package com.easyhoon.basic.service.implement;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.easyhoon.basic.service.MainService;

// description: @Component - 해당 클래스를 java bean으로 등록하여 Spring이 인스턴스 생성을 알아서 할 수 있도록 하는 어노테이션 //
// description: @Service - @Component와 동일한 작업을 수행하지만 가독성을 위해 Service라는 이름을 가짐 //
@Service
public class MainServiceImplement implements MainService {

    @Override
    public String getMethod() {
        return "This Method is getMethod";
    }
    
}
