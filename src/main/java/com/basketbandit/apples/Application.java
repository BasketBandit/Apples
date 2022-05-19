package com.basketbandit.apples;

import com.basketbandit.apples.rest.GenericController;
import com.basketbandit.apples.rest.HeardleController;
import com.basketbandit.apples.rest.PlaceController;
import com.basketbandit.apples.rest.WordleController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	public Application() {
		new GenericController().init();
		new WordleController().init();
		new HeardleController().init();
		new PlaceController().init();
	}
}
