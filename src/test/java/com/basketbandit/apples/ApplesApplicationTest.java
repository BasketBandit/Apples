package com.basketbandit.apples;

import com.basketbandit.apples.rest.HeardleController;
import com.basketbandit.apples.rest.PlaceController;
import com.basketbandit.apples.rest.WordleController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApplesApplicationTest {
	@Autowired
	private HeardleController heardleController;
	@Autowired
	private WordleController wordleController;
	@Autowired
	private PlaceController placeController;

	@Test
	void contextLoads() {
		assertThat(heardleController).isNotNull();
		assertThat(wordleController).isNotNull();
		assertThat(placeController).isNotNull();
	}

	@Test
	void dataIsLoaded() {
		// Gets a list of 5-letter words.
		assertThat(((List<?>)wordleController.wordle("5").getModel().get("words")).isEmpty()).isFalse();
		// Gets a collection of sounds (String).
		assertThat(((Collection<?>)heardleController.heardle().getModel().get("sounds")).isEmpty()).isFalse();
		// Gets a BASE64 encoded image.
		assertThat(placeController.place().getModel().get("image").equals("")).isFalse();
	}
}