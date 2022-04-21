package com.basketbandit.apples.rest;

import com.basketbandit.apples.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Random;

@RestController
public class SoundsController {
    private static final Logger log = LoggerFactory.getLogger(WordsController.class);
    private static final HashMap<String, String> sounds = Application.sounds;

    @GetMapping("/sounds/")
    public ModelAndView words() {
        ModelAndView modelAndView = new ModelAndView("./sounds/index");
        modelAndView.addObject("sounds", sounds.values());
        modelAndView.addObject("sound", sounds.keySet().toArray()[new Random(System.currentTimeMillis()).nextInt(sounds.size()-1)]);
        return modelAndView;
    }
}