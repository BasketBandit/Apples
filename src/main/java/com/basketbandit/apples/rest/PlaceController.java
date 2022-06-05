package com.basketbandit.apples.rest;

import com.basketbandit.apples.scheduler.ScheduleHandler;
import com.basketbandit.apples.scheduler.jobs.UpdateJob;
import com.basketbandit.apples.scheduler.tasks.UpdateImageTask;
import com.basketbandit.apples.util.Utilities;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

@RestController
@RequestMapping("/place")
public class PlaceController implements Controller<Object> {
    private static final BufferedImage image = new BufferedImage(500,282, BufferedImage.TYPE_INT_ARGB);

    public PlaceController() {
        try {
            log.info("Loading image form ./data/canvas.png");
            image.setData(ImageIO.read(new File("./data/canvas.png")).getRaster()); // BufferedImage cannot be cast; we have to set data manually
            ScheduleHandler.registerJob(new UpdateJob(new UpdateImageTask())); // creates a job to save changes of the canvas to disk
            log.info("Successfully loaded image.");
        } catch(Exception e) {
            log.warn("Failed to load existing image, reason: {}", e.getMessage());
        }
    }

    @Override
    public BufferedImage getData() {
        return image;
    }

    @GetMapping("")
    public ModelAndView place() {
        ModelAndView modelAndView = new ModelAndView("./place/index");
        modelAndView.addObject("image", Utilities.image2base64(image));
        return modelAndView;
    }
}
