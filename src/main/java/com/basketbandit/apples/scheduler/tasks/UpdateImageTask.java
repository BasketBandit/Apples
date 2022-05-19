package com.basketbandit.apples.scheduler.tasks;

import com.basketbandit.apples.rest.PlaceController;
import com.basketbandit.apples.scheduler.Task;

import javax.imageio.ImageIO;
import java.io.File;

public class UpdateImageTask implements Task {
    private static final PlaceController placeController = new PlaceController();

    public UpdateImageTask() {
    }

    @Override
    public void run() {
        try {
            ImageIO.write(placeController.getData(), "png", new File("./data/canvas.png"));
        } catch(Exception e) {
            log.error("Unable to write canvas.png to disk, reason: {}", e.getMessage(), e);
        }
    }

}
