package com.basketbandit.apples.rest;

import com.basketbandit.apples.Application;
import com.basketbandit.apples.util.Sanitiser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

@RestController
public class WordsController {
    private static final Logger log = LoggerFactory.getLogger(WordsController.class);
    private static final HashMap<Integer, ArrayList<String>> words = Application.words;

    @GetMapping("/words/")
    public ModelAndView words() {
        ModelAndView modelAndView = new ModelAndView("./words/index");
        modelAndView.addObject("letters", words.keySet());
        return modelAndView;
    }

    @GetMapping("/words/{x}")
    public ModelAndView play(@PathVariable String x) {
        int type = Sanitiser.isNumeric(x) ? Integer.parseInt(x) : 5;
        ArrayList<String> list = words.getOrDefault(type, words.get(5)); // this should ensure that all possible inputs will resolve
        ModelAndView modelAndView = new ModelAndView("./words/play");
        modelAndView.addObject("words", list);
        modelAndView.addObject("word", list.get(new Random(System.currentTimeMillis()).nextInt(list.size()-1)));
        modelAndView.addObject("length", type);
        return modelAndView;
    }

    @GetMapping("/words/list/{x}")
    public ArrayList<String> words(@PathVariable String x) {
        int type = Sanitiser.isNumeric(x) ? Integer.parseInt(x) : 5;
        return words.getOrDefault(type, words.get(5));
    }
}