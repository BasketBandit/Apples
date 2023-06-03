package com.basketbandit.booba.scheduler.tasks;

import com.basketbandit.booba.rest.GumboController;
import com.basketbandit.booba.scheduler.Task;

public class BackupPlaceTask implements Task {

    public BackupPlaceTask() {
    }

    @Override
    public void run() {
        try {
            GumboController.backupPlace();
        } catch(Exception e) {
            log.error("Unable to backup, reason: {}", e.getMessage(), e);
        }
    }

}
