package com.basketbandit.words.rest;

import com.basketbandit.words.WordsApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

@org.springframework.web.bind.annotation.RestController
public class RestfulController {
    private static final HashMap<Integer, ArrayList<String>> words = WordsApplication.words;
    private static final Logger log = LoggerFactory.getLogger(RestfulController.class);

    @GetMapping("/")
    public ModelAndView root() {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("letters", words.keySet());
        return modelAndView;
    }

    @GetMapping("/{type}")
    public ModelAndView play(@PathVariable int type) {
        ArrayList<String> list = words.get(type);
        ModelAndView modelAndView = new ModelAndView("play");
        modelAndView.addObject("word", list.get(new Random(System.currentTimeMillis()).nextInt(list.size()-1)));
        modelAndView.addObject("length", type);
        return modelAndView;
    }
}
