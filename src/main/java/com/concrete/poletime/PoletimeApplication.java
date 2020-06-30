package com.concrete.poletime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class PoletimeApplication {

	@PostConstruct
	public void init(){
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Budapest"));
	}

	public static void main(String[] args) {
		SpringApplication.run(PoletimeApplication.class, args);
	}

}
