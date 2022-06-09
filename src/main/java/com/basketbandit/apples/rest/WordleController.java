package com.basketbandit.apples.rest;

import com.basketbandit.apples.util.Utilities;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class WordleController implements Controller<Object> {
    private static final HashMap<Integer, ArrayList<String>> words = new HashMap<>();
    record RandomWord(ArrayList<String> words, String random_word){}

    public WordleController() {
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
    public ModelAndView wordle(@RequestParam(defaultValue = "5") int letters) {
        return new ModelAndView("./wordle/index")
                .addObject("keys", words.keySet())
                .addObject("example", words.getOrDefault(letters, words.get(5)).get(0));
    }

    @GetMapping("/api/v1/words/{letters}")
    public RandomWord words(@PathVariable("letters") int letters) {
        ArrayList<String> wordList = words.getOrDefault(letters, words.get(5));
        return new RandomWord(wordList, wordList.get(Utilities.random(wordList.size())));
    }
}
