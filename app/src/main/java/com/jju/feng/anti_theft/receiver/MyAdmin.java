package com.jju.feng.anti_theft.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import com.jju.feng.anti_theft.R;

public class MyAdmin extends DeviceAdminReceiver {
    private MediaPlayer player;

    @Override
    public void onPasswordFailed(Context ctxt, Intent intent) {
        Toast.makeText(ctxt, "u will never break!", Toast.LENGTH_LONG).show();

        DevicePolicyManager policyManager = (DevicePolicyManager) ctxt.getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (policyManager != null) {
            int attempts = policyManager.getCurrentFailedPasswordAttempts();
            Log.v("TAG", "Attempts = " + attempts);
            if (attempts == 3) {
                player = MediaPlayer.create(ctxt, R.raw.ylzs);
                player.setVolume(1.0f, 1.0f);
                player.start();

            }
        }


    }


    @Override
    public void onPasswordSucceeded(Context ctxt, Intent intent) {
        Toast.makeText(ctxt, "good u enterd", Toast.LENGTH_LONG)
                .show();
        String tag = "tag";
        Log.v(tag, "this massage from success");

    }

}
