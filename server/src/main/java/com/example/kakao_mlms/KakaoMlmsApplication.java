package com.example.kakao_mlms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class KakaoMlmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(KakaoMlmsApplication.class, args);
	}

}
