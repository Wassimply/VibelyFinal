package com.appteve.ViberlyApp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.awareness.fence.FenceState;

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by joao on 01/10/16.
 */

class HeadphoneFenceBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        FenceState fenceState = FenceState.extract(intent);

        Log.d(TAG, "Fence Receiver Received");

        if (TextUtils.equals(fenceState.getFenceKey(), "headphoneFenceKey")) {
            switch (fenceState.getCurrentState()) {
                case FenceState.TRUE:
                    Log.i(TAG, "Fence > Headphones are plugged in.");
                    break;
                case FenceState.FALSE:
                    Log.i(TAG, "Fence > Headphones are NOT plugged in.");
                    break;
                case FenceState.UNKNOWN:
                    Log.i(TAG, "Fence > The headphone fence is in an unknown state.");
                    break;
            }
        }
    }

}