package com.example.medfriend;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlarmListRow.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlarmListRow#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlarmListRow extends Fragment {
    private static final String ARG_PARAM1 = "string1";

    private static MedAlarm instanceAlarm;

    private MedAlarm alarm;

    private String mParam1;

    private OnFragmentInteractionListener mListener;

    public AlarmListRow() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AlarmListRow.
     */
    public static AlarmListRow newInstance(MedAlarm medalarm) {
        instanceAlarm = medalarm;
        AlarmListRow fragment = new AlarmListRow();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final boolean setView = false;
        final View alarmView = inflater.inflate(R.layout.fragment_alarm_list_row, null);
        FrameLayout fl = alarmView.findViewById(R.id.layout);
        final ViewGroup parent = (ViewGroup) fl.getParent();
        TextView dateText = (TextView) alarmView.findViewById(R.id.time);
        Switch enableButton = alarmView.findViewById(R.id.switch1);
        ImageButton deleteButton = alarmView.findViewById(R.id.delete);

        dateText.setText(instanceAlarm.getMonth() + "/" + instanceAlarm.getDay() + "/" + instanceAlarm.getYear() + " " + instanceAlarm.getHour() + ":" + instanceAlarm.getMinute() + " " + instanceAlarm.getName());


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Homepage)getActivity()).deleteAlarm(alarm, alarm.toString());

                onDetach();


            }
        });

        enableButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    ((Homepage)getActivity()).enableAlarm(alarm);
                } else {
                    ((Homepage)getActivity()).disableAlarm(alarm);
                }
            }
        });

        return alarmView;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {

        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void setAl() {
        alarm = new MedAlarm(instanceAlarm.getYear(), instanceAlarm.getMonth(), instanceAlarm.getDay(), instanceAlarm.getHour(), instanceAlarm.getMinute(), instanceAlarm.getInterval(), instanceAlarm.getID(), instanceAlarm.getName());
        //Log.d(TAG, "" + alarm.getMinute());
    }
}
