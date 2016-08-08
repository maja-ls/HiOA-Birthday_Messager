package com.example.s198515_mappe2.fragmenter.dialoger;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.s198515_mappe2.R;
import com.example.s198515_mappe2.databasen.Person;

/**
 * Created by Maja on 22.10.2015.
 */
public class YesNoDialog extends DialogFragment {

    private Person personToDelete;
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle b = getArguments();
        personToDelete = b.getParcelable("PERSONEN");
        String tittel = getResources().getString(R.string.person_delete_confirm);

        Dialog d = getDialog();
        d.setTitle(tittel);

        LinearLayout l = new LinearLayout(getActivity());
        l.setGravity(Gravity.CENTER);

        Button yes = new Button(getActivity());
        yes.setText(getResources().getString(R.string.text_yes));
        yes.setOnClickListener(yesKnappelytter);

        Button no = new Button(getActivity());
        no.setText(getResources().getString(R.string.text_no));
        no.setOnClickListener(noKnappelytter);

        l.addView(yes);
        l.addView(no);

        return l;
    }

    private View.OnClickListener yesKnappelytter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            try{
                ((onYesClick)getActivity()).onYesClickToDelete(personToDelete);
            }
            catch (ClassCastException cce){
            }

            dismiss();
        }
    };

    private View.OnClickListener noKnappelytter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    public interface onYesClick{
        public void onYesClickToDelete(Person p);
    }

}