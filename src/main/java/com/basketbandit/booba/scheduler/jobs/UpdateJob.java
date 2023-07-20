package com.basketbandit.booba.scheduler.jobs;

import com.basketbandit.booba.scheduler.Job;
import com.basketbandit.booba.scheduler.tasks.UpdateImageTask;

import java.util.concurrent.TimeUnit;

public class UpdateJob extends Job {
    private final UpdateImageTask task;

    public UpdateJob(UpdateImageTask task) {
        super(0, 1, TimeUnit.MINUTES);
        this.task = task;
    }

    @Override
    public void run() {
        handleTask(task);
    }
}
