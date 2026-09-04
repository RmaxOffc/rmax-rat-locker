package com.rmax.locker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()) ||
            Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
            
            // Start lock activity
            Intent activityIntent = new Intent(context, MainActivity.class);
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(activityIntent);
            
            // Start lock service
            Intent serviceIntent = new Intent(context, LockService.class);
            context.startService(serviceIntent);
        }
    }
}
