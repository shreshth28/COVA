package com.shreshth.cova.notification;

import com.firebase.jobdispatcher.JobParameters;

public class ReminderFirebaseJobService extends com.firebase.jobdispatcher.JobService {
    @Override
    public boolean onStartJob(JobParameters job) {
        NotificationUtils.remindUserBecauseCharging(this);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}
