package com.example.s198515_mappe2.servicer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.s198515_mappe2.R;
import com.example.s198515_mappe2.verktoy.DatoTidFormatter;

import java.util.Calendar;

/**
 * Service som aktiveres hver gang telefonen rebooter, eller endringer blir gjort i preferansene,
 * eller hovedaktiviteten kjører onresume. Sjekker om meldinger automatisk skal sendes og setter
 * opp når hvis de skal.
 */
public class TidSjekkerService extends Service {

    private String PREFS;
    private static final String MYDEBUG = "TidSjekkerService";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PREFS = getPackageName()+"_preferences";
        Log.d(MYDEBUG, "I settperiodiskservice");

        boolean serviceShouldStart = getSharedPreferences(PREFS, MODE_PRIVATE).getBoolean(getString(R.string.pref_service_key), false);

        Intent i = new Intent(this, MeldingsSender.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, i, 0);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.cancel(pintent);

        if (serviceShouldStart) {
            Log.d(MYDEBUG, "\n********\n\n SERVICE ER AKTIV *******");
            int time = getSharedPreferences(PREFS, MODE_PRIVATE).getInt(getString(R.string.pref_hour_key), 12);
            int min = getSharedPreferences(PREFS, MODE_PRIVATE).getInt(getString(R.string.pref_minute_key), 00);

            Calendar tidspunkt = Calendar.getInstance();
            tidspunkt.set(Calendar.HOUR_OF_DAY, time);
            tidspunkt.set(Calendar.MINUTE, min);
            Log.d(MYDEBUG, "Kalender satt til: " + DatoTidFormatter.getStringDatoForMenneske(tidspunkt));

            // Sjekker om utsendingstidspunktet har vært allerede og øker så tid for sjekk til dagen etter
            if (tidspunkt.before(Calendar.getInstance())) {
                tidspunkt.add(Calendar.DATE, 1);
            }

            Log.d(MYDEBUG, "Kalender endret til: " + DatoTidFormatter.getStringDatoForMenneske(tidspunkt));

            am.setRepeating(AlarmManager.RTC_WAKEUP, tidspunkt.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pintent);
        }
        else {
            Log.d(MYDEBUG, "\n********\n\n SERVICE ER IKKE AKTIV *******");
        }

        return super.onStartCommand(intent, flags, startId);
    }
}
