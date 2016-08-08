package com.example.s198515_mappe2.fragmenter.dialoger;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Maja on 22.10.2015.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {


private static final String MYDEBUG = "Timepickerfragment";

private Calendar tid;

private OnTimePickerInteraction mListener;

public interface OnTimePickerInteraction {
    public void setTidTilAktivitet(Calendar tiden);
    public Calendar hentTidFraAktivitet();
}


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnTimePickerInteraction) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTimePickerInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        tid = mListener.hentTidFraAktivitet();

        if (tid == null)
            tid = Calendar.getInstance();

        int time = tid.get(Calendar.HOUR_OF_DAY);
        int minutt = tid.get(Calendar.MINUTE);


        // Create a new instance of TimePickerDialog and return it

        return new TimePickerDialog(getActivity(), this, time, minutt, true);

    }

    @Override
    public void onTimeSet(TimePicker view, int time, int minutt) {
        Log.d(MYDEBUG, "\n********\n\n ER I ONDATESET *******");
        tid.set(Calendar.HOUR_OF_DAY, time);
        tid.set(Calendar.MINUTE, minutt);


        mListener.setTidTilAktivitet(tid);
    }

}
