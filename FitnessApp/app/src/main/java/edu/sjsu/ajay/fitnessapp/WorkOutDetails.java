package edu.sjsu.ajay.fitnessapp;


import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.ajay.fitnessapp.Entities.AllData;
import edu.sjsu.ajay.fitnessapp.Entities.Steps;
import edu.sjsu.ajay.fitnessapp.Helper.DBHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class WorkOutDetails extends Fragment {


    private static final String TAG = WorkOutDetails.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DBHelper dbHelper;

    private OnFragmentInteractionListener onFragmentInteractionListener;

    public WorkOutDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkoutDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkOutDetails newInstance(String param1, String param2) {
        WorkOutDetails fragment = new WorkOutDetails();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        dbHelper = DBHelper.getInstance(getActivity().getApplicationContext());
        Log.d(TAG, dbHelper.getStepsData().toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_work_out_details, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        drawChart();
    }

    private void drawChart() {
        LineChart chart = (LineChart) getView().findViewById(R.id.chart);

        List<Entry> caloriesData = new ArrayList<Entry>();
        List<Entry> stepCountData = new ArrayList<Entry>();

        List<Steps> stepsData = dbHelper.getStepsData();

        if(stepsData.size() <= 0) return;
        Log.d(TAG, "Retrieving step data and converting to chart data: ");
        for (Steps step : stepsData) {
            Log.d(TAG, "Step data row: " + step.toString());
            stepCountData.add(new Entry(Float.parseFloat(step.timestamp), step.count));
            caloriesData.add(new Entry(Float.parseFloat(step.timestamp), step.calories));
        }

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

        LineDataSet dataSet = new LineDataSet(stepCountData, "Steps count"); // add entries to dataset
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(1); // styling, ...
        dataSets.add(dataSet);

        LineDataSet dataSet2 = new LineDataSet(caloriesData, "Calories burnt"); // add entries to dataset
        dataSet.setColor(Color.GREEN);
        dataSet.setValueTextColor(1); // styling, ...
        dataSets.add(dataSet2);

        LineData lineData = new LineData(dataSets);
        chart.setData(lineData);

        chart.invalidate(); // refresh

        updateMinMaxValues();
    }

    public void updateMinMaxValues(){

        AllData allData = dbHelper.getAllTimeData();
        int a = 10;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (onFragmentInteractionListener != null) {
            onFragmentInteractionListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            onFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onFragmentInteractionListener = null;
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

}
