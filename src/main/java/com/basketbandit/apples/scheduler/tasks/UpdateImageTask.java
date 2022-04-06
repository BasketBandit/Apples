package com.basketbandit.apples.scheduler.tasks;

import com.basketbandit.apples.Application;
import com.basketbandit.apples.scheduler.Task;

import javax.imageio.ImageIO;
import java.io.File;

public class UpdateImageTask implements Task {

    public UpdateImageTask() {
    }

    @Override
    public void run() {
        try {
            ImageIO.write(Application.image, "png", new File("background.png"));
        } catch(Exception e) {
            //
        }
    }

}
