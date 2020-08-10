package com.concrete.poletime;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(value = "test")
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class PoletimeApplicationTests {

	@PostConstruct
	public void init(){
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Budapest"));
	}

	@Test
	void contextLoads() {
	}

}
