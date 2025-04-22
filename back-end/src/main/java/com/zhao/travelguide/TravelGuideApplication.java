package com.zhao.travelguide;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zhao.travelguide.server.mapper")
public class TravelGuideApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelGuideApplication.class, args);
	}

}
