package com.example.s198515_mappe2.fragmenter;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.s198515_mappe2.R;
import com.example.s198515_mappe2.Startvindu;

/**
 * Fragmentet som vises ved start
 */
public class Startfragment extends Fragment {

    private static final String MYDEBUG = "Startfragment";
    private static String notis;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_startvindu, null);

    }


    @Override
    public void onResume() {
        super.onResume();

        TextView tv = (TextView) getActivity().findViewById(R.id.notification);
        tv.setText(notis);
    }

    public static void setNotis(String s) {
        notis = s;
    }
}
