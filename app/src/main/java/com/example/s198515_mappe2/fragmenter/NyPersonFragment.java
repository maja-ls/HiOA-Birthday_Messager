package com.example.s198515_mappe2.fragmenter;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.s198515_mappe2.databasen.Person;
import com.example.s198515_mappe2.R;
import com.example.s198515_mappe2.fragmenter.dialoger.DatePickerFragment;
import com.example.s198515_mappe2.verktoy.RegexSjekker;


/**
 * Fragment for å registrere ny person eller endre person
 */
public class NyPersonFragment extends Fragment {

    private Button save;
    private Button velgDato;

    private boolean datoErValgt;

    private OnNyPersonInteraction mListener;

    private static final String MYDEBUG = "nypersonfragment";

    private boolean heltNyPerson;
    private Person personForEndring;


    public interface OnNyPersonInteraction {
        public void lagreNyPerson(Person p);
        public void endrePerson(Person p);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_skjermnyperson, null);

        datoErValgt = false;

        save = (Button) v.findViewById(R.id.knapp_lagre_ny_person);
        velgDato = (Button) v.findViewById(R.id.knapp_velg_dato);

        save.setOnClickListener(knappelytter);

        velgDato.setOnClickListener(knappelytter);

        return v;
    }

    // Fyller feltene med data hvis det er en person som skal endres
    @Override
    public void onStart() {
        super.onStart();

        Bundle b = getArguments();

        if (b != null) {
            heltNyPerson = false;
            datoErValgt = true;

            personForEndring = b.getParcelable("PERSONEN");

            EditText fornavn = (EditText) getActivity().findViewById(R.id.fname);
            EditText etternavn = (EditText) getActivity().findViewById(R.id.lname);
            EditText telefon = (EditText) getActivity().findViewById(R.id.phone);
            EditText melding = (EditText) getActivity().findViewById(R.id.message);
            TextView bursdag = (TextView) getActivity().findViewById(R.id.currentdob);

            fornavn.setText(personForEndring.getFornavn());
            etternavn.setText(personForEndring.getEtternavn());
            telefon.setText(personForEndring.getTelefon());
            melding.setText(personForEndring.getMelding());
            bursdag.setText(getResources().getString(R.string.datesetto) + " " + personForEndring.getBursdagForMenneske());

        }
        else {
            heltNyPerson = true;
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnNyPersonInteraction) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNyPersonInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private View.OnClickListener knappelytter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.knapp_velg_dato:

                    DialogFragment picker = new DatePickerFragment();
                    picker.show(getFragmentManager(), "datePicker");

                    datoErValgt = true;

                    break;


                case R.id.knapp_lagre_ny_person:

                    boolean ok = true;
                    String feilmelding = getString(R.string.new_person_error_init);

                    // Fordi brukere ikke nødvendigvis ønsker å bare bruke ordentlige navn på kontaktene sine
                    // Har jeg ikke lagt inn noen validering på navnet utenom å sjekke at noe er fylt ut.
                    // Tlf sjekkes mot regex
                    // Contentvalues bruker uansett prepared statements og sikrer mot injections

                    EditText fornavn = (EditText) getActivity().findViewById(R.id.fname);
                    String fnavn = fornavn.getText().toString();
                    if (!RegexSjekker.sjekkStringHarSynligChars(fnavn)) {
                        ok = false;
                        feilmelding += "\n" + getString(R.string.new_person_error_fname);
                    }

                    EditText etternavn = (EditText) getActivity().findViewById(R.id.lname);
                    String enavn = etternavn.getText().toString();
                    if (!RegexSjekker.sjekkStringHarSynligChars(enavn)) {
                        ok = false;
                        feilmelding += "\n" + getString(R.string.new_person_error_lname);
                    }


                    EditText telefon = (EditText) getActivity().findViewById(R.id.phone);
                    String tlf = telefon.getText().toString();
                    if (!RegexSjekker.sjekkTlfRegex(tlf)) {
                        ok = false;
                        feilmelding += "\n" + getString(R.string.new_person_error_phone);
                    }



                    EditText melding = (EditText) getActivity().findViewById(R.id.message);
                    String sms = melding.getText().toString();
                    if (!RegexSjekker.sjekkStringHarSynligChars(sms)) {
                        ok = false;
                        feilmelding += "\n" + getString(R.string.new_person_error_sms);
                    }


                    if (!datoErValgt) {
                        ok = false;
                        feilmelding += "\n" + getString(R.string.new_person_error_date);
                    }

                    if(ok) {

                        fnavn = fnavn.trim();
                        enavn = enavn.trim();
                        sms = sms.trim();

                        if (heltNyPerson) {
                            Person nyPerson = new Person(fnavn, enavn, sms, tlf);

                            mListener.lagreNyPerson(nyPerson);
                        }
                        else {

                            personForEndring.setFornavn(fnavn);
                            personForEndring.setEtternavn(enavn);
                            personForEndring.setTelefon(tlf);
                            personForEndring.setMelding(sms);

                            mListener.endrePerson(personForEndring);

                        }

                        getFragmentManager().popBackStackImmediate();

                    }
                    else {
                        Toast.makeText(getActivity(),  feilmelding
                                , Toast.LENGTH_LONG).show();
                    }

                    break;
            }
        }
    };

}
