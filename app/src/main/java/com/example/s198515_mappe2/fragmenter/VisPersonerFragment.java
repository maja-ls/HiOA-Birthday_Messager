package com.example.s198515_mappe2.fragmenter;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import com.example.s198515_mappe2.databasen.Person;
import com.example.s198515_mappe2.R;
import com.example.s198515_mappe2.fragmenter.dialoger.YesNoDialog;


/**
 * Fragment som brukes for å vise lista med personer
 */
public class VisPersonerFragment extends Fragment implements AdapterView.OnItemClickListener {


    private static final String MYDEBUG = "personfragment";

    private ArrayList<Person> personListe;
    private OnListInteraction mListener;

    private ListAdapter mAdapter;


    public interface OnListInteraction {
        public void visListePerson(Person person);
        public void endreListePerson(Person person);
        public void slettListePerson(Person person);
        public void optionNyPerson();
    }

    //Obligatorisk tom konstruktør
    public VisPersonerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


        Bundle b = getArguments();
        personListe = b.getParcelableArrayList("PERSONLISTE");


        mAdapter = new PersonListeAdapter(getActivity(), personListe);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_list, container, false);

        // Set the adapter
        ListView lv = (ListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) lv).setAdapter(mAdapter);
        registerForContextMenu(lv);

        lv.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_personliste, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nyperson:
                mListener.optionNyPerson();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnListInteraction) activity;
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Person person = this.personListe.get(position);
        mListener.visListePerson(person);
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

        menu.setHeaderTitle(personListe.get(info.position).getFornavn() + " " +
                personListe.get(info.position).getEtternavn());

        //Henter itemene som skal i menyen fra arrays.xml
        String[] menuItems = getResources().getStringArray(R.array.vispersoner_contextmenu);
        for (int i = 0; i < menuItems.length; i++) {
            menu.add(Menu.NONE, i, i, menuItems[i]);

        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();


        if (menuItemIndex == 0) {
            //Edit er valgt
            mListener.endreListePerson(personListe.get(info.position));
        }

        if (menuItemIndex == 1) {
            // Delete er valgt
            mListener.slettListePerson(personListe.get(info.position));
        }


        /* BRUKES IKKE LENGER
        String[] menuItems = getResources().getStringArray(R.array.vispersoner_contextmenu);
        String menuItemName = menuItems[menuItemIndex];
        String listItemName = personerNavn[info.position];
        Toast.makeText(getActivity(), String.format("Selected %s for item %s", menuItemName, listItemName)
                , Toast.LENGTH_SHORT).show();
        */

        return true;
    }
}
