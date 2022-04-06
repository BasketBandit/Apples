package com.basketbandit.apples.scheduler.jobs;

import com.basketbandit.apples.scheduler.Job;
import com.basketbandit.apples.scheduler.tasks.UpdateImageTask;

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
