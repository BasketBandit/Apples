package com.basketbandit.apples;

import com.basketbandit.apples.socket.PlaceSocketHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SmokeTest {
    @Autowired
    private PlaceSocketHandler placeSocketHandler;

    @Test
    public void contextLoads() {
        assertThat(placeSocketHandler).isNotNull();
    }
}