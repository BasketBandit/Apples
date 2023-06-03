package com.basketbandit.booba.scheduler.tasks;

import com.basketbandit.booba.rest.PlaceController;
import com.basketbandit.booba.scheduler.Task;

public class BackupPlaceTask implements Task {
    @Override
    public void run() {
        try {
            PlaceController.backupToDiscord();
        } catch(Exception e) {
            log.error("Unable to backup, reason: {}", e.getMessage(), e);
        }
    }
}
