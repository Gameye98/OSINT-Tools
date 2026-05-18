package com.gameye98.osint;

import android.content.Context;
import android.content.pm.PackageManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.app.Activity;
import android.Manifest;

public class PermissionUtils {
    Activity MainActivity;

    private static final int PERMISSION_REQUEST_CODE = 1;
    
    //checkInstallPermission();
    //checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
    /*
     public void checkPermission(String permission, int requestCode) {
     if(ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
     ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
     }
     }
     */
    public PermissionUtils(Activity activity) {
        MainActivity = activity;
    }
    public void checkAllPermissions() {
        String[] allPermissions = {
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.KILL_BACKGROUND_PROCESSES,
            Manifest.permission.VIBRATE,
            Manifest.permission.REQUEST_INSTALL_PACKAGES,
            Manifest.permission.REQUEST_DELETE_PACKAGES,
            //Manifest.permission.MANAGE_EXTERNAL_STORAGE,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_MEDIA_LOCATION,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            android.provider.Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INSTALL_SHORTCUT,
            Manifest.permission.CAMERA,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG,
        };
        Integer[] intPermissions = {
            1006,
            1007,
            1008
        };
        int permissionIndex = 0;
        for(String permissionx : allPermissions) {
            requestPermissionIfNeeded(MainActivity, permissionx);
            /*
            if(ActivityCompat.checkSelfPermission(this, permissionx) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{permissionx}, intPermissions[permissionIndex]);
            }
            permissionIndex += 1;
            */
        }
    }
    /*
     private void checkInstallPermission() {
     try {
     Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
     intent.setData(Uri.parse("package:sec.blackhole.app"));
     startActivity(intent);
     } catch(Exception e) {
     e.printStackTrace();
     }
     }
     */
    /*
    private void checkAudioPermission() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 10);
        }
    }
    private boolean checkPermission() {
        /*
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
         return Environment.isExternalStorageManager();
         } else {
        int result = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int result1 = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
        //}
    }
    */
    /*
    private void requestPermission() {
        // Storage External Permission
        /*
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
         try {
         Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
         intent.addCategory("android.intent.category.DEFAULT");
         intent.setData(Uri.parse(String.format("package:%s",getApplicationInfo().packageName)));
         startActivityForResult(intent, STORAGE_PERMISSION_CODE); //2296);
         } catch (Exception e) {
         Intent intent = new Intent();
         intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
         startActivityForResult(intent, STORAGE_PERMISSION_CODE); //2296);
         }
         } else {
         //below android 11
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{}, STORAGE_PERMISSION_CODE);
        //}
	}*/
    public static boolean isPermissionGranted(Context context, String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
    public static void requestPermissionIfNeeded(Activity activity, String permission) {
        if (!isPermissionGranted(activity, permission)) {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, PERMISSION_REQUEST_CODE);
        }
	}
}
