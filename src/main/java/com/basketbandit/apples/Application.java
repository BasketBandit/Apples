package com.basketbandit.apples;

import com.basketbandit.apples.rest.HeardleController;
import com.basketbandit.apples.rest.PlaceController;
import com.basketbandit.apples.rest.WordleController;
import com.basketbandit.apples.scheduler.ScheduleHandler;
import com.basketbandit.apples.scheduler.jobs.UpdateJob;
import com.basketbandit.apples.scheduler.tasks.UpdateImageTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@SpringBootApplication
public class Application {
	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	public Application() throws IOException {
		log.info("Parsing words from /static/data/2019scrabble3plus.txt");
		new BufferedReader(new InputStreamReader(new ClassPathResource("static/data/2019scrabble3plus.txt").getInputStream(), StandardCharsets.UTF_8)).lines().forEach(word -> {
			WordleController.words.computeIfAbsent(word.length(), k -> new ArrayList<>()).add(word);
		});
		log.info("Found words of length " + WordleController.words.keySet());

		log.info("Parsing sounds from /static/data/250soundcloud.txt");
		new BufferedReader(new InputStreamReader(new ClassPathResource("static/data/250soundcloud.txt").getInputStream(), StandardCharsets.UTF_8)).lines().forEach(sound -> {
			String[] data = sound.split(",", 2);
			HeardleController.sounds.putIfAbsent(data[0], data[1]);
		});
		log.info("Found " + HeardleController.sounds.size() + " sounds." );

		try {
			BufferedImage tmp = ImageIO.read(new File("./data/canvas.png")); // BufferedImage cannot be cast; we have to set data manually.
			PlaceController.image.setData(tmp.getRaster());
		} catch(Exception e) {
			log.warn("Failed to load existing image, reason: {}", e.getMessage());
		}

		ScheduleHandler.registerJob(new UpdateJob(new UpdateImageTask()));
	}
}
