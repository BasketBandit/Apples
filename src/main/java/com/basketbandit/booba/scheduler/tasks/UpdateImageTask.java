package com.basketbandit.booba.scheduler.tasks;

import com.basketbandit.booba.rest.PlaceController;
import com.basketbandit.booba.scheduler.Task;

public class UpdateImageTask implements Task {
    @Override
    public void run() {
        try {
            PlaceController.writeToDisk();
        } catch(Exception e) {
            log.error("Unable to write canvas.png to disk, reason: {}", e.getMessage(), e);
        }
    }
}
