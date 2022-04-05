package com.basketbandit.quizzical.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@org.springframework.web.bind.annotation.RestController
public class GenericController {
    private static final Logger log = LoggerFactory.getLogger(GenericController.class);

    @GetMapping("/")
    public ModelAndView root() {
        return new ModelAndView("index");
    }
}