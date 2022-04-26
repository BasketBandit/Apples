package com.basketbandit.apples.rest;

import com.basketbandit.apples.util.Sanitiser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

@RestController
public class WordleController {
    private static final Logger log = LoggerFactory.getLogger(WordleController.class);
    public static final HashMap<Integer, ArrayList<String>> words = new HashMap<>();

    @GetMapping("/wordle")
    public ModelAndView wordle(@RequestParam(defaultValue = "5") String letters) {
        int type = Sanitiser.isNumeric(letters) ? Integer.parseInt(letters) : 5;
        type = words.containsKey(type) ? type : 5;
        ArrayList<String> list = words.getOrDefault(type, words.get(5)); // this should ensure that all possible inputs will resolve
        ModelAndView modelAndView = new ModelAndView("./wordle/index");
        modelAndView.addObject("letters", words.keySet());
        modelAndView.addObject("word", list.get(new Random(System.currentTimeMillis()).nextInt(list.size()-1)));
        modelAndView.addObject("words", list);
        modelAndView.addObject("length", type);
        return modelAndView;
    }

    @GetMapping("/wordle/list/{x}")
    public ArrayList<String> words(@PathVariable String x) {
        int type = Sanitiser.isNumeric(x) ? Integer.parseInt(x) : 5;
        return words.getOrDefault(type, words.get(5));
    }
}
