package com.basketbandit.booba;

import com.basketbandit.booba.rest.GenericController;
import com.basketbandit.booba.rest.HeardleController;
import com.basketbandit.booba.rest.PlaceController;
import com.basketbandit.booba.rest.WordleController;
import com.basketbandit.booba.util.Utilities;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApplesApplicationTest {
	@Autowired
	private GenericController genericController;
	@Autowired
	private HeardleController heardleController;
	@Autowired
	private WordleController wordleController;
	@Autowired
	private PlaceController placeController;

	@Test
	void contextLoads() {
		assertThat(genericController).isNotNull();
		assertThat(heardleController).isNotNull();
		assertThat(wordleController).isNotNull();
		assertThat(placeController).isNotNull();
	}

	@Test
	void dataIsLoaded() {
		assertThat(wordleController.getData().isEmpty()).isFalse();
		assertThat(heardleController.getData().isEmpty()).isFalse();
		assertThat(Utilities.image2base64(PlaceController.image()).equals("")).isFalse();
	}
}