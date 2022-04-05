package com.basketbandit.quizzical.scheduler.tasks;

import com.basketbandit.quizzical.Application;
import com.basketbandit.quizzical.scheduler.Task;

import javax.imageio.ImageIO;
import java.io.File;

public class UpdateImageTask implements Task {

    public UpdateImageTask() {
    }

    @Override
    public void run() {
        try {
            ImageIO.write(Application.image, "png", new File("background"));
        } catch(Exception e) {
            //
        }
    }

}
