package com.basketbandit.booba.rest;

import com.basketbandit.booba.util.Utilities;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;

@RestController
public class HeardleController implements Controller {
    private static final HashMap<String, String> sounds = new HashMap<>();
    record RandomSound(Collection<String> sounds, String random_sound){}

    public HeardleController() {
        try(BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream("./data/heardle.txt"), StandardCharsets.UTF_8))) {
            log.info("Parsing sounds from ./data/heardle.txt");
            r.lines().forEach(sound -> {
                String[] data = sound.split(",", 2);
                sounds.putIfAbsent(data[0], data[1]);
            });
            log.info("Successfully parsed " + sounds.size() + " sounds." );
        } catch(Exception e) {
            log.warn("There was an issue while reading the heardle data file, reason: {}", e.getMessage(), e);
        }
    }

    public HashMap<String, String> getData() {
        return sounds;
    }

    @GetMapping("/heardle")
    public ModelAndView heardle() {
        return new ModelAndView("./heardle/index");
    }

    @GetMapping("/api/v1/sounds")
    public RandomSound sounds() {
        return new RandomSound(sounds.values(), sounds.keySet().toArray()[Utilities.random(sounds.size())].toString());
    }
}