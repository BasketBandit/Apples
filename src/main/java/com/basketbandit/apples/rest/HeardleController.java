package com.basketbandit.apples.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Random;

@RestController
public class HeardleController implements Controller<Object> {
    private static final Logger log = LoggerFactory.getLogger(HeardleController.class);
    private static final HashMap<String, String> sounds = new HashMap<>();

    @Override
    public void init() {
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

    @Override
    public HashMap<String, String> getData() {
        return sounds;
    }

    @GetMapping("/heardle")
    public ModelAndView heardle() {
        ModelAndView modelAndView = new ModelAndView("./heardle/index");
        modelAndView.addObject("sounds", sounds.values());
        modelAndView.addObject("sound", sounds.keySet().toArray()[new Random(System.currentTimeMillis()).nextInt(sounds.size()-1)]);
        return modelAndView;
    }
}