package com.basketbandit.apples.rest;

import com.basketbandit.apples.scheduler.ScheduleHandler;
import com.basketbandit.apples.scheduler.jobs.UpdateJob;
import com.basketbandit.apples.scheduler.tasks.UpdateImageTask;
import com.basketbandit.apples.util.BufferedImageBase64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

@RestController
public class PlaceController implements Controller {
    private static final Logger log = LoggerFactory.getLogger(PlaceController.class);
    public static BufferedImageBase64 image = new BufferedImageBase64(500,141, BufferedImage.TYPE_INT_ARGB);

    @Override
    public void init() {
        try {
            log.info("Loading image form ./data/canvas.png");
            image.setData(ImageIO.read(new File("./data/canvas.png")).getRaster()); // BufferedImage cannot be cast; we have to set data manually
            ScheduleHandler.registerJob(new UpdateJob(new UpdateImageTask())); // creates a job to save changes of the canvas to disk
            log.info("Successfully loaded image.");
        } catch(Exception e) {
            log.warn("Failed to load existing image, reason: {}", e.getMessage());
        }
    }

    @GetMapping("/place")
    public ModelAndView place() {
        ModelAndView modelAndView = new ModelAndView("./place/index");
        modelAndView.addObject("image", image.getAsBase64Png());
        return modelAndView;
    }
}
