package com.basketbandit.booba.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ChoreController implements Controller {
    @GetMapping("/chores")
    public ModelAndView chores() {
        return new ModelAndView("./chores/index");
    }
}
