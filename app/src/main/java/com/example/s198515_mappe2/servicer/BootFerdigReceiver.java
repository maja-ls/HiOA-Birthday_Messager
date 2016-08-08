package com.example.s198515_mappe2.servicer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Broadcastreceiver som kjøres når telefonen booter
 */
public class BootFerdigReceiver extends BroadcastReceiver {

    private static final String MYDEBUG = "BootFerdigReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(MYDEBUG, "\n********\n\n Har mottatt boot *******");
        Intent i = new Intent(context, TidSjekkerService.class);

        context.startService(i);
    }
}
