package com.revosleap.text;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by carlos on 1/30/18.
 */

public class BaseActivity extends AppCompatActivity {
    private static final String PARAM_REQUEST_IN_PROCESS = "requestPermissionsInProcess";

    private static final int REQUEST_PERMISSION = 3;
    private static final String PREFERENCE_PERMISSION_DENIED = "PREFERENCE_PERMISSION_DENIED";

    private AtomicBoolean mRequestPermissionsInProcess = new AtomicBoolean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            boolean tmp = savedInstanceState.getBoolean(PARAM_REQUEST_IN_PROCESS, false);
            mRequestPermissionsInProcess.set(tmp);
        }

        checkPermissions(new String[]{Manifest.permission.READ_CONTACTS});
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(PARAM_REQUEST_IN_PROCESS, mRequestPermissionsInProcess.get());
    }

    private void checkPermissions(String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissionInternal(permissions);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkPermissionInternal(String[] permissions) {
        ArrayList<String> requestPerms = new ArrayList<String>();
        for (String permission : permissions) {
            if (checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED && !userDeniedPermissionAfterRationale(permission)) {
                requestPerms.add(permission);
            }
        }
        if (requestPerms.size() > 0 && !mRequestPermissionsInProcess.getAndSet(true)) {
            //  We do not have this essential permission, ask for it
            requestPermissions(requestPerms.toArray(new String[requestPerms.size()]), REQUEST_PERMISSION);
            return true;
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    if (Manifest.permission.READ_CONTACTS.equals(permission)) {
                        showRationale(permission);
                    }
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void showRationale(final String permission) {
        if (shouldShowRequestPermissionRationale(permission) && !userDeniedPermissionAfterRationale(permission)) {

            //  Notify the user of the reduction in functionality and possibly exit (app dependent)
            new AlertDialog.Builder(this)
                    .setTitle(R.string.permission_denied)
                    .setMessage(R.string.permission_denied_contacts)
                    .setPositiveButton(R.string.permission_deny, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                dialog.dismiss();
                            } catch (Exception ignore) {
                            }
                            setUserDeniedPermissionAfterRationale(permission);
                            mRequestPermissionsInProcess.set(false);
                        }
                    })
                    .setNegativeButton(R.string.permission_retry, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                dialog.dismiss();
                            } catch (Exception ignore) {
                            }
                            mRequestPermissionsInProcess.set(false);
                            checkPermissions(new String[]{permission});
                        }
                    })
                    .show();
        } else {
            mRequestPermissionsInProcess.set(false);
        }
    }

    private boolean userDeniedPermissionAfterRationale(String permission) {
        SharedPreferences sharedPrefs = getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean(PREFERENCE_PERMISSION_DENIED + permission, false);
    }

    private void setUserDeniedPermissionAfterRationale(String permission) {
        SharedPreferences.Editor editor = getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).edit();
        editor.putBoolean(PREFERENCE_PERMISSION_DENIED + permission, true).commit();
    }
}
