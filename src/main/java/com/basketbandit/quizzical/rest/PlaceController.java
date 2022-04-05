package com.basketbandit.quizzical.rest;

import com.basketbandit.quizzical.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;

@RestController
public class PlaceController {
    private static final Logger log = LoggerFactory.getLogger(PlaceController.class);

    @GetMapping("/place/")
    public ModelAndView place() {
        ModelAndView modelAndView = new ModelAndView("./place/index");
        modelAndView.addObject("image", Application.image);
        return modelAndView;
    }

    @GetMapping("/place/current")
    public ResponseEntity<byte[]> image() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(Application.image, "png", byteArrayOutputStream);
        } catch(Exception e) {
            log.error(e.getMessage());
        }
        byte[] imageInByte = byteArrayOutputStream.toByteArray();
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.CONTENT_DISPOSITION, "filename=\""+System.currentTimeMillis()+"-discord_place.png\"").contentType(MediaType.IMAGE_PNG) .body(imageInByte);
    }
}
