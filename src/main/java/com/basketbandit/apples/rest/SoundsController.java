package com.basketbandit.apples.rest;

import com.basketbandit.apples.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class SoundsController {
    private static final Logger log = LoggerFactory.getLogger(WordsController.class);
    private static final HashMap<Integer, ArrayList<String>> words = Application.words;

    @GetMapping("/sounds/")
    public ModelAndView words() {
        ModelAndView modelAndView = new ModelAndView("./sounds/index");
        return modelAndView;
    }
}