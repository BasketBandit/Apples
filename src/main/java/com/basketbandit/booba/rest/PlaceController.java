package com.basketbandit.booba.rest;

import com.basketbandit.booba.scheduler.ScheduleHandler;
import com.basketbandit.booba.scheduler.jobs.BackupJob;
import com.basketbandit.booba.scheduler.jobs.UpdateJob;
import com.basketbandit.booba.scheduler.tasks.BackupPlaceTask;
import com.basketbandit.booba.scheduler.tasks.UpdateImageTask;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

@RestController
public class PlaceController implements Controller {
    private static final BufferedImage image = new BufferedImage(500,282, BufferedImage.TYPE_INT_ARGB);

    public PlaceController() {
        try {
            log.info("Loading image form ./data/canvas.png");
            image.setData(ImageIO.read(new File("./data/canvas.png")).getRaster()); // BufferedImage cannot be cast; we have to set data manually
            ScheduleHandler.registerJob(new UpdateJob(new UpdateImageTask())); // creates a job to save changes of the canvas to disk
            ScheduleHandler.registerJob(new BackupJob(new BackupPlaceTask()));
            log.info("Successfully loaded image.");
        } catch(Exception e) {
            log.warn("Failed to load existing image, reason: {}", e.getMessage());
        }
    }

    public BufferedImage getData() {
        return image;
    }

    @GetMapping("/place")
    public ModelAndView place() {
        return new ModelAndView("./place/index");
    }
}
