package com.basketbandit.quizzical;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

@SpringBootApplication
public class Application {
	private static final Logger log = LoggerFactory.getLogger(Application.class);
	public static final HashMap<Integer, ArrayList<String>> words = new HashMap<>();

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	public Application() throws IOException {
		log.info("Parsing words from /static/data/2019scrabble3plus.txt");
		new BufferedReader(new InputStreamReader(new ClassPathResource("static/data/2019scrabble3plus.txt").getInputStream())).lines().forEach(word -> {
			words.computeIfAbsent(word.length(), k -> new ArrayList<>()).add(word);
		});
		log.info("Found words of length " + words.keySet());
	}
}
