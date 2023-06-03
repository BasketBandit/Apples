package com.basketbandit.booba.scheduler.jobs;

import com.basketbandit.booba.scheduler.Job;
import com.basketbandit.booba.scheduler.tasks.BackupPlaceTask;

import java.util.concurrent.TimeUnit;

public class BackupJob extends Job {
    private final BackupPlaceTask task;

    public BackupJob(BackupPlaceTask task) {
        super(0, 1, TimeUnit.HOURS);
        this.task = task;
    }

    @Override
    public void run() {
        handleTask(task);
    }
}
