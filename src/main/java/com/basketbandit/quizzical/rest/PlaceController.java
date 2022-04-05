package com.basketbandit.quizzical.rest;

import com.basketbandit.quizzical.util.BufferedImageHex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.awt.image.BufferedImage;

@RestController
public class PlaceController {
    private static final Logger log = LoggerFactory.getLogger(PlaceController.class);
    public static final BufferedImageHex image = new BufferedImageHex(250,141, BufferedImage.TYPE_INT_ARGB);

    @GetMapping("/place/")
    public ModelAndView place() {
        ModelAndView modelAndView = new ModelAndView("./place/index");
        modelAndView.addObject("image", image);
        return modelAndView;
    }
}
