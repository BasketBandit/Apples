package com.basketbandit.apples.rest;

import com.basketbandit.apples.util.Sanitiser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

@RestController
public class WordleController implements Controller<Object> {
    private static final HashMap<Integer, ArrayList<String>> words = new HashMap<>();

    @Override
    public void init() {
        try(BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream("./data/wordle.txt"), StandardCharsets.UTF_8))) {
            log.info("Parsing words from ./data/wordle.txt");
            r.lines().forEach(word -> {
                words.computeIfAbsent(word.length(), k -> new ArrayList<>()).add(word);
            });
            log.info("Found words of length " + words.keySet());
        } catch(Exception e) {
            log.warn("There was an issue while reading the wordle data file, reason: {}", e.getMessage(), e);
        }
    }

    @Override
    public HashMap<Integer, ArrayList<String>> getData() {
        return words;
    }

    @GetMapping("/wordle")
    public ModelAndView wordle(@RequestParam(defaultValue = "5") String letters) {
        int type = Sanitiser.isNumeric(letters) ? Integer.parseInt(letters) : 5;
        type = words.containsKey(type) ? type : 5;
        ArrayList<String> list = words.getOrDefault(type, words.get(5)); // this should ensure that all possible inputs will resolve
        ModelAndView modelAndView = new ModelAndView("./wordle/index");
        modelAndView.addObject("letters", words.keySet());
        modelAndView.addObject("word", list.get(new Random(System.currentTimeMillis()).nextInt(list.size())));
        modelAndView.addObject("words", list);
        modelAndView.addObject("length", type);
        return modelAndView;
    }

    @GetMapping("/wordle/list")
    public ArrayList<String> words(@RequestParam(defaultValue = "5") String x) {
        int type = Sanitiser.isNumeric(x) ? Integer.parseInt(x) : 5;
        return words.getOrDefault(type, words.get(5));
    }
}
