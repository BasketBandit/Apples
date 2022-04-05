package com.basketbandit.quizzical.rest;

import com.basketbandit.quizzical.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class PlaceController {
    private static final Logger log = LoggerFactory.getLogger(PlaceController.class);

    @GetMapping("/place/")
    public ModelAndView place() {
        ModelAndView modelAndView = new ModelAndView("./place/index");
        modelAndView.addObject("image", Application.image);
        return modelAndView;
    }
}
