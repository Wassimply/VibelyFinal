package com.appteve.ViberlyApp.receivers;

/**
 * Created by joao on 01/10/16.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.awareness.fence.FenceState;
import com.moolajoo.joao.runmusicplayer.BuildConfig;

/**
 * A basic BroadcastReceiver to handle intents from from the Awareness API.
 */
public class FenceReceiver extends BroadcastReceiver {
    // The fence key is how callback code determines which fence fired.
    private final String FENCE_KEY = "fence_key";

    private final String TAG = getClass().getSimpleName();

    // The intent action which will be fired when your fence is triggered.
    private final String FENCE_RECEIVER_ACTION =
            BuildConfig.APPLICATION_ID + "FENCE_RECEIVER_ACTION";



    @Override
    public void onReceive(Context context, Intent intent) {
        if (!TextUtils.equals(FENCE_RECEIVER_ACTION, intent.getAction())) {
            Log.d(TAG, "Received an unsupported action in FenceReceiver: action="
                    + intent.getAction());
            return;
        }

        // The state information for the given fence is em
        FenceState fenceState = FenceState.extract(intent);

        if (TextUtils.equals(fenceState.getFenceKey(), FENCE_KEY)) {
            String fenceStateStr;
            Log.d("current", String.valueOf(fenceState.getCurrentState()));
            switch (fenceState.getCurrentState()) {
                case FenceState.TRUE:
                    fenceStateStr = "true";
                    break;
                case FenceState.FALSE:
                    fenceStateStr = "false";
                    break;
                case FenceState.UNKNOWN:
                    fenceStateStr = "unknown";
                    break;
                default:
                    fenceStateStr = "unknown value";
            }
            Log.d(TAG, "Fence state: " + fenceStateStr);
        }
    }
}