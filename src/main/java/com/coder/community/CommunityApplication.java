package com.coder.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class CommunityApplication {

	@PostConstruct
	public void init() {
		// Solve netty activation conflict
		System.setProperty("es.set.netty.runtime.available.processors", "false");
	}
	
	public static void main(String[] args) {
		SpringApplication.run(CommunityApplication.class, args);
	}

}
