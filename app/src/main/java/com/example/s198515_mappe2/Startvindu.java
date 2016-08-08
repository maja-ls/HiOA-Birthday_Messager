package com.example.s198515_mappe2;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.s198515_mappe2.databasen.*;
import com.example.s198515_mappe2.fragmenter.*;
import com.example.s198515_mappe2.fragmenter.dialoger.*;
import com.example.s198515_mappe2.servicer.TidSjekkerService;
import com.example.s198515_mappe2.verktoy.DatoTidFormatter;

import java.util.ArrayList;
import java.util.Calendar;

/*
    Laget av Maja Sæterstøen
    Student nr. s198515

    Hovedaktiviteten som er ansvarlig for alle fragmentene, har databasen, etc
  */
public class Startvindu extends AppCompatActivity implements
        VisPersonerFragment.OnListInteraction,
        DatePickerFragment.OnDatePickerInteraction,
        TimePickerFragment.OnTimePickerInteraction,
        VisPersonFragment.OnPersonDetaljInteraction,
        NyPersonFragment.OnNyPersonInteraction,
        YesNoDialog.onYesClick {

    private static final String MYDEBUG = "Startvindu.java";
    private String PREFS;


    ArrayList<Person> plist;
    private DBHandler db;
    private Calendar valgtDato;
    private Calendar valgtTid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startvindu);

        PREFS = getPackageName() + "_preferences";
        db = new DBHandler(this);
        valgtDato = Calendar.getInstance();
        plist = db.finnAlleEllerNoenPersoner(null);

        // Setter appen til å alltid være i portrettmodus, da jeg føler det gir mest mening layoutmessig
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        if (findViewById(R.id.fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }

            Startfragment startfragment = new Startfragment();
            Startfragment.setNotis(getNotifikasjonNesteBursdag());

            startfragment.setArguments(getIntent().getExtras());

            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, startfragment)
                    .commit();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        Startfragment.setNotis(getNotifikasjonNesteBursdag());

        int time = getSharedPreferences(PREFS, MODE_PRIVATE).getInt(getString(R.string.pref_hour_key), 12);
        int min = getSharedPreferences(PREFS, MODE_PRIVATE).getInt(getString(R.string.pref_minute_key), 00);

        valgtTid = Calendar.getInstance();
        valgtTid.set(Calendar.HOUR_OF_DAY, time);
        valgtTid.set(Calendar.MINUTE, min);


        Log.d(MYDEBUG, "I onresume");

        Intent i = new Intent(this, TidSjekkerService.class);
        startService(i);

        getNotifikasjonNesteBursdag();

        //BRUKT FOR TESTING
        // boolean sjekk = getSharedPreferences(PREFS, MODE_PRIVATE).getBoolean(getString(R.string.pref_service_key), false);
        //Toast.makeText(this, ""+sjekk
        //        , Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_startvindu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_settings:

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                PrefsFragment pf = new PrefsFragment();

                transaction.replace(R.id.fragment_container, pf);
                transaction.addToBackStack(null);
                transaction.commit();

                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount == 0) {
            getFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Gjemmer keyboardet
     */
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }


    // Metode som setter tekst til notifikasjonsfeltet i appen. Dessverre hadde jeg ikke tid til å gjøre at denne oppdaterte seg dynamisk med endringer
    public String getNotifikasjonNesteBursdag() {
        String notis;

        if (!plist.isEmpty()) {
            Calendar datoIDag = Calendar.getInstance();
            int detteAAr = datoIDag.get(Calendar.YEAR);
            boolean noenHarBursdagIdag = false;

            Person nestemann = null;

            for (int i = 0; i < plist.size(); i++) {

                // Finner ut om noen har bursdag i dag, eller hvem som er neste til å ha bursdag
                if (!noenHarBursdagIdag) {

                    Calendar personenSinBursdag = plist.get(i).getBursdagCalendar();
                    personenSinBursdag.set(Calendar.YEAR, detteAAr);

                    if (nestemann == null)
                        nestemann = plist.get(i);

                    if (personenSinBursdag.after(datoIDag) && personenSinBursdag.before(nestemann.getBursdagCalendar())) {
                        nestemann = plist.get(i);
                    }

                    if (personenSinBursdag.get(Calendar.MONTH) == datoIDag.get(Calendar.MONTH) &&
                            personenSinBursdag.get(Calendar.DAY_OF_MONTH) == datoIDag.get(Calendar.DAY_OF_MONTH)) {
                        nestemann = plist.get(i);
                        noenHarBursdagIdag = true;
                    }
                }
            }

            ArrayList<Person> personerMedNesteBursdag = new ArrayList<>();

            // Løper gjennom og sjekker om noen andre har bursdag samme dag som nestemann
            for (int i = 0; i < plist.size(); i++) {
                if (nestemann.getBursdagCalendar().get(Calendar.MONTH) == plist.get(i).getBursdagCalendar().get(Calendar.MONTH) &&
                        nestemann.getBursdagCalendar().get(Calendar.DAY_OF_MONTH) == plist.get(i).getBursdagCalendar().get(Calendar.DAY_OF_MONTH)) {
                    personerMedNesteBursdag.add(plist.get(i));
                }
            }

            if (personerMedNesteBursdag.size() > 1) {
                notis = getString(R.string.multi_intro);
            }
            else {
                notis = getString(R.string.single_intro);
            }
            if (noenHarBursdagIdag) {
                notis += " " + getString(R.string.today);
            }
            else {
                notis += " " + DatoTidFormatter.getStringDatoUtenAArForMenneske(nestemann.getBursdagCalendar()) + "\n";
            }

            notis += getString(R.string.will_message);

            for (int i = 0; i < personerMedNesteBursdag.size(); i++) {
                notis += personerMedNesteBursdag.get(i).getFornavn();
                notis += " " + personerMedNesteBursdag.get(i).getEtternavn() + "\n";
            }
        }
        else {
            notis = getString(R.string.emptyarray);
        }

        return notis;
    }



    /*
    Klasse og metoder for Preferanser
     */
    public static class PrefsFragment extends PreferenceFragment implements
            SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }

        @Override
        public void onResume() {
            super.onResume();

            getPreferenceScreen()
                    .getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen()
                    .getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = super.onCreateView(inflater, container, savedInstanceState);
            if (view != null)
            view.setBackgroundColor(getResources().getColor(R.color.background2_dullest));

            return view;
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            // Oppdaterer servicen når en innstiling i preferansene har blitt endret
            Intent i = new Intent(getActivity(), TidSjekkerService.class);
            getActivity().startService(i);

        }
    }

    // Funksjon som kalles av pref_set_time_button.xml som brukes i preferences.xml
    public void setTimeForPref(View v) {

        DialogFragment picker = new TimePickerFragment();
        picker.show(getFragmentManager(), "datePicker");
    }

    /*
    Preferanser slutt
     */



    /*///////////
    // FØLGENDE METODER KALLES FRA STARTFRAGMENTETS XML LAYOUT SIDEN DE IKKE KREVER PARAMETRE
    /////////////*/

    public void visPersoner(View v) {
        visPersonerFragment();
    }

    public void nyPerson(View v) {
        visNyPersonFragment();
    }
    /////////////
    // SLUTT STARTFRAGMENT METODER
    /////////////

    private void visPersonerFragment() {
        plist = db.finnAlleEllerNoenPersoner(null);

        if (plist.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.emptyarray)
                    , Toast.LENGTH_SHORT).show();
        } else {
            Log.d(MYDEBUG, "\n********\n\n Alt ok hittil *******");


            VisPersonerFragment nyListeFrag = new VisPersonerFragment();

            Bundle args = new Bundle();
            args.putParcelableArrayList("PERSONLISTE", plist);

            nyListeFrag.setArguments(args);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            transaction.replace(R.id.fragment_container, nyListeFrag);
            transaction.addToBackStack(null);
            transaction.commit();

        }
    }

    private void visNyPersonFragment() {
        valgtDato = null;
        NyPersonFragment nyPersFrag = new NyPersonFragment();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, nyPersFrag);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    /*
    * UNDER FØLGER METODER SOM ER IMPLEMENTERT VED BRUK AV INTERFACE
    * */


    /////////////
    // Kommunikasjon mellom NyPersonFragment og aktiviteten ved bruk av interface
    /////////////
    // Lagrer personen til db
    @Override
    public void lagreNyPerson(Person person) {

        hideSoftKeyboard();
        person.setBursdagMedCal(valgtDato);

        boolean ok = db.leggTilPerson(person);

        if (!ok) {
            Toast.makeText(this, getString(R.string.new_person_db_error)
                    , Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, person.getFornavn() + " " +
                    person.getEtternavn() + " " +
                    getString(R.string.new_person_saved)
                    , Toast.LENGTH_SHORT).show();
        }
    }

    // Endrer personen i db
    @Override
    public void endrePerson(Person person) {

        hideSoftKeyboard();
        person.setBursdagMedCal(valgtDato);

        boolean ok = db.endrePerson(person);

        if (!ok) {
            Toast.makeText(this, getString(R.string.new_person_db_error)
                    , Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, person.getFornavn() + " " +
                    person.getEtternavn() + " " +
                    getString(R.string.new_person_saved)
                    , Toast.LENGTH_SHORT).show();
        }
    }

    /////////////
    // SLUTT  NyPersonFragment
    /////////////


    //////////////
    // Kommunikasjon mellom vispersonerfragment og aktiviteten ved bruk av interface
    ////////////
    // Viser detaljer om en person som er valgt i lista
    @Override
    public void visListePerson(Person person) {

        VisPersonFragment visPersFrag = new VisPersonFragment();

        Bundle args = new Bundle();
        args.putParcelable("PERSONEN", person);

        visPersFrag.setArguments(args);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, visPersFrag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Viser fragment for endring av person valgt i lista
    @Override
    public void endreListePerson(Person person) {

        valgtDato = person.getBursdagCalendar();
        NyPersonFragment endrePersFrag = new NyPersonFragment();

        Bundle args = new Bundle();
        args.putParcelable("PERSONEN", person);

        endrePersFrag.setArguments(args);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, endrePersFrag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void slettListePerson(Person person) {
        Bundle b = new Bundle();
        b.putParcelable("PERSONEN", person);

        YesNoDialog conf = new YesNoDialog();
        conf.setArguments(b);
        conf.show(getFragmentManager(), "Dialog");
    }

    @Override
    public void optionNyPerson() {
        visNyPersonFragment();
    }

    //////////////
    // SLUTT vispersonerfragment
    ////////////

    //////////////
    // Kommunikasjon mellom vispersonfragment og aktiviteten ved bruk av interface
    ////////////
    @Override
    public void rediger(Person person) {
        endreListePerson(person);
    }

    @Override
    public void slett(Person person) {
        slettListePerson(person);
    }

    //////////////
    // SLUTT vispersonfragment
    ////////////

    //////////////
    // Kommunikasjon mellom DatePickerFragment og aktiviteten ved bruk av interface
    ////////////
    @Override
    public void setDatoTilAktivitet(Calendar dato) {
        valgtDato = dato;
        TextView tv = (TextView) findViewById(R.id.currentdob);

        tv.setText(getResources().getString(R.string.datesetto) +
                " " +
                DatoTidFormatter.getStringDatoForMenneske(dato));

        if (dato.after(Calendar.getInstance())) {
            Toast.makeText(this, getString(R.string.new_person_warning_date)
                    , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Calendar hentDatoFraAktivitet() {
        return valgtDato;
    }

    //////////////
    // SLUTT DatePickerFragment
    ////////////


    //////////////
    // Kommunikasjon mellom TimePickerFragment og aktiviteten ved bruk av interface
    ////////////

    @Override
    public Calendar hentTidFraAktivitet() {
        return valgtTid;
    }

    @Override
    public void setTidTilAktivitet(Calendar tiden) {

        valgtTid = tiden;

        int time = tiden.get(Calendar.HOUR_OF_DAY);
        int min = tiden.get(Calendar.MINUTE);

        getSharedPreferences(PREFS, MODE_PRIVATE)
                .edit()
                .putInt(getString(R.string.pref_hour_key), time)
                .putInt(getString(R.string.pref_minute_key), min)
                .commit();

    }
    //////////////
    // SLUTT TimePickerFragment
    ////////////

    //////////////
    // Kommunikasjon mellom YesNoDialog og aktiviteten ved bruk av interface
    ////////////
    // Sletter person valgt i lista
    @Override
    public void onYesClickToDelete(Person person) {
        boolean ok = db.slettPerson(person);

        if (!ok) {
            Toast.makeText(this, getString(R.string.new_person_db_error)
                    , Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, person.getFornavn() + " " +
                    person.getEtternavn() + " " +
                    getString(R.string.person_deleted)
                    , Toast.LENGTH_SHORT).show();

            FragmentManager fm = getFragmentManager();
            fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            visPersonerFragment();
        }
    }
    //////////////
    // SLUTT YesNoDialog
    ////////////
}
