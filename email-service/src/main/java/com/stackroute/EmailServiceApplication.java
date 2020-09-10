package com.stackroute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootApplication
public class EmailServiceApplication {
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private SendEmail sendEmail;

	public static void main(String[] args) {
		SpringApplication.run(EmailServiceApplication.class, args);
	}
}
