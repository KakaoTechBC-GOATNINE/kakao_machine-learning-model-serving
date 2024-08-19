package com.example.kakao_mlms.intercepter;

import com.example.kakao_mlms.exception.ResponseDto;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice(basePackages = "com.example.sontcamp") //적용할 패키지 경로
public class ResponseInterceptor implements ResponseBodyAdvice {
    //Interceptor 실행할지 결정 -> 항상 true 반환
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    //실제로 응답 데이터 가로채 처리하는 부분
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (returnType.getParameterType() == ResponseDto.class) { //파라미터 타입이 ResponseDto일 경우
            //입력받은 body를 ResponseDto 형식으로 변환하고 그 httspStatus 가져옴
            HttpStatus status = ((ResponseDto<?>) body).httpStatus();
            //status 적용
            response.setStatusCode(status);
        }
        //변환한 body 반환
        return body;
    }
}
