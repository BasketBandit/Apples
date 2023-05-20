package com.basketbandit.booba;

import com.basketbandit.discord.Gumbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
	Gumbo gumbo = new Gumbo();

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
