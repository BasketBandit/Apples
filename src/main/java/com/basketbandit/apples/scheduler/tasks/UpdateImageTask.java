package com.basketbandit.apples.scheduler.tasks;

import com.basketbandit.apples.rest.PlaceController;
import com.basketbandit.apples.scheduler.Task;

import javax.imageio.ImageIO;
import java.io.File;

public class UpdateImageTask implements Task {

    public UpdateImageTask() {
    }

    @Override
    public void run() {
        try {
            ImageIO.write(PlaceController.image, "png", new File("./data/canvas.png"));
        } catch(Exception e) {
            log.error("Unable to write canvas.png to disk, reason: {}", e.getMessage(), e);
        }
    }

}
