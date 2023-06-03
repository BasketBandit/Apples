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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.sql.Time;
import java.time.Instant;
import java.util.Arrays;

@RestController
public class PlaceController implements Controller {
    private static final BufferedImage image = new BufferedImage(500,282, BufferedImage.TYPE_INT_ARGB);
    private static byte[] imageByteArray = new byte[0];

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

    public static BufferedImage image() {
        return image;
    }

    public static void writeToDisk() {
        try {
            ImageIO.write(image, "png", new File("./data/canvas.png"));
        } catch(Exception e) {
            log.error("Something went wrong while saving canvas.png... {}", e.getMessage(), e);
        }
    }

    public static void backupToDiscord() {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, "png", os);
            byte[] newByteArray = os.toByteArray();
            if(!Arrays.equals(imageByteArray, newByteArray)) {
                imageByteArray = newByteArray;
                InputStream inputStream = new ByteArrayInputStream(imageByteArray);
                GumboController.sendFile(1114649660003450910L, inputStream, Time.from(Instant.now()).toString()+".png");
            }
        } catch(Exception e) {
            log.error("Something went wrong! {}", e.getMessage(), e);
        }
    }

    @GetMapping("/place")
    public ModelAndView place() {
        return new ModelAndView("./place/index");
    }
}
