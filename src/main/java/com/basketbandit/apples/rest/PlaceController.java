package com.basketbandit.apples.rest;

import com.basketbandit.apples.util.BufferedImageBase64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.awt.image.BufferedImage;

@RestController
public class PlaceController {
    private static final Logger log = LoggerFactory.getLogger(PlaceController.class);
    public static BufferedImageBase64 image = new BufferedImageBase64(500,141, BufferedImage.TYPE_INT_ARGB);

    @GetMapping("/place")
    public ModelAndView place() {
        ModelAndView modelAndView = new ModelAndView("./place/index");
        modelAndView.addObject("image", image.getAsBase64Png());
        return modelAndView;
    }
}
