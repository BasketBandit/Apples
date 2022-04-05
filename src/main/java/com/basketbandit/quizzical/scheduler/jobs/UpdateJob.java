package com.basketbandit.quizzical.scheduler.jobs;

import com.basketbandit.quizzical.scheduler.Job;
import com.basketbandit.quizzical.scheduler.tasks.UpdateImageTask;

import java.util.concurrent.TimeUnit;

public class UpdateJob extends Job {
    private final UpdateImageTask task;

    public UpdateJob(UpdateImageTask task) {
        super(0, 10, TimeUnit.SECONDS);
        this.task = task;
    }

    @Override
    public void run() {
        handleTask(task);
    }
}
