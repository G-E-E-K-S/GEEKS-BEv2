package com.my_geeks.geeks;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.TimeZone;

@OpenAPIDefinition(servers = {
		@Server(url = "http://localhost:8080", description = "로컬 URL"),
		@Server(url = "https://server.my-geeks.com", description = "서버 URL")
})
@EnableCaching
@EnableJpaAuditing
@SpringBootApplication
public class GeeksApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
		SpringApplication.run(GeeksApplication.class, args);
	}

}
