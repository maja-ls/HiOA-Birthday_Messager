package com.example.s198515_mappe2.fragmenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.app.Activity;

import java.util.ArrayList;

import com.example.s198515_mappe2.*;
import com.example.s198515_mappe2.databasen.Person;

/**
 * Adapter som bestemmer hvordan personene skal vises
 */
public class PersonListeAdapter extends ArrayAdapter {
    private Context context;
    private boolean useList = true;

    public PersonListeAdapter(Context context, ArrayList personer) {
        super(context, android.R.layout.simple_selectable_list_item, personer);
        this.context = context;
    }

    /**
     * Holder for the list items.
     */
    private class ViewHolder{
        TextView navneText;
        TextView bursdagsText;
    }

    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Person person = (Person)getItem(position);
        View viewToUse = null;

        // This block exists to inflate the settings list item conditionally based on whether
        // we want to support a grid or list view.
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        /*
        Her bestemmes det hvorvidt appen skal bruke grid for å vise personene eller liste.
        Jeg har ikke brukt grid i denne appen, men har valgt å beholde koden og layoutfilene i tilfelle det
        skulle være ønskelig med grid ved videreutvikling.
         */
        if (convertView == null) {
            if(useList){
                viewToUse = mInflater.inflate(R.layout.person_list_item, null);
            } else {
                viewToUse = mInflater.inflate(R.layout.person_grid_item, null);
            }

            holder = new ViewHolder();
            holder.navneText = (TextView)viewToUse.findViewById(R.id.personNavnTextView);
            holder.bursdagsText = (TextView)viewToUse.findViewById(R.id.personBursdagTextView);
            viewToUse.setTag(holder);
        } else {
            viewToUse = convertView;
            holder = (ViewHolder) viewToUse.getTag();
        }

        holder.navneText.setText(person.getFornavn() + " " + person.getEtternavn());
        holder.bursdagsText.setText(person.getBursdagForMenneske());
        return viewToUse;
    }
}