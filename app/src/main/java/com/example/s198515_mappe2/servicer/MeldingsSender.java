package com.example.s198515_mappe2.servicer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.example.s198515_mappe2.R;
import com.example.s198515_mappe2.databasen.DBHandler;
import com.example.s198515_mappe2.databasen.Person;
import com.example.s198515_mappe2.verktoy.DatoTidFormatter;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Service som sender ut melding til de som har bursdag dagen den kjøres
 */
public class MeldingsSender extends Service {

    private static final String MYDEBUG = "AKTIVSERVICE";

    private DBHandler db;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(MYDEBUG, "\n********\n\n Meldingsender KJØRER NÅ *******");


        db = new DBHandler(this);

        Calendar tidspunkt = Calendar.getInstance();

        String tidString = DatoTidFormatter.getStringDatoUtenAArForMaskin(tidspunkt);

        ArrayList<Person> personerMedBursdag = db.finnAlleEllerNoenPersoner(tidString);

        if (!personerMedBursdag.isEmpty()) {

            SmsManager smsManager = SmsManager.getDefault();

            for (int i = 0; i < personerMedBursdag.size(); i++) {

                String navn = " " + personerMedBursdag.get(i).getFornavn() + " " + personerMedBursdag.get(i).getEtternavn();
                String nr = personerMedBursdag.get(i).getTelefon();
                String melding = personerMedBursdag.get(i).getMelding();

                smsManager.sendTextMessage(nr, null, melding, null, null);

                Toast.makeText(getApplicationContext(), getString(R.string.sms_sent) + navn, Toast.LENGTH_LONG).show();

                Log.d(MYDEBUG, personerMedBursdag.get(i).getFornavn() + personerMedBursdag.get(i).getEtternavn() + " er bursdagsbarn i dag");
            }
        }
        else {
            Log.d(MYDEBUG, "Ingen har bursdag i dag");
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
