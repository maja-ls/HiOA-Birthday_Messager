package com.example.s198515_mappe2.fragmenter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.s198515_mappe2.R;
import com.example.s198515_mappe2.Startvindu;
import com.example.s198515_mappe2.databasen.Person;


/**
 * Fragment som viser detaljer om en person
 */
public class VisPersonFragment extends Fragment {

    private static final String MYDEBUG = "nypersonfragment";

    private OnPersonDetaljInteraction mListener;
    private Person person;

    public interface OnPersonDetaljInteraction {
        public void rediger(Person p);
        public void slett(Person p);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnPersonDetaljInteraction) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnListInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle b = getArguments();
        person = b.getParcelable("PERSONEN");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_skjermvisperson, null);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_persondetaljer, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.rediger:
                mListener.rediger(person);
                break;

            case R.id.slett:
                mListener.slett(person);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();


        TextView fornavn = (TextView) getActivity().findViewById(R.id.fname);
        TextView etternavn = (TextView) getActivity().findViewById(R.id.lname);
        TextView telefon = (TextView) getActivity().findViewById(R.id.phone);
        TextView dato = (TextView) getActivity().findViewById(R.id.currentdob);
        TextView melding = (TextView) getActivity().findViewById(R.id.message);


        fornavn.setText(person.getFornavn());
        etternavn.setText(person.getEtternavn());
        telefon.setText(person.getTelefon());
        dato.setText(person.getBursdagForMenneske());
        melding.setText(person.getMelding());

    }
}
