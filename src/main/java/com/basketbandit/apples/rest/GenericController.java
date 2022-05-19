package com.basketbandit.apples.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class GenericController implements Controller<Object> {
    private static final Logger log = LoggerFactory.getLogger(GenericController.class);

    @Override
    public void init() {
    }

    @Override
    public Object getData() {
        return null;
    }

    @GetMapping("/")
    public ModelAndView root() {
        return new ModelAndView("index");
    }
}
