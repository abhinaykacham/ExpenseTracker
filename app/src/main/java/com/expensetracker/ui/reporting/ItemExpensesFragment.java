package com.expensetracker.ui.reporting;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.expensetracker.ChartDataUnit;
import com.expensetracker.DBHelper;
import com.expensetracker.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemExpensesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemExpensesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private AnyChartView anyChartView;
    Column column;

    DatePickerDialog picker;
    DBHelper mDBHelper;
    SharedPreferences sharedPreferences;
    List<ChartDataUnit> chartDataUnits;
    public ItemExpensesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItemExpensesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemExpensesFragment newInstance(String param1, String param2) {
        ItemExpensesFragment fragment = new ItemExpensesFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root  = inflater.inflate(R.layout.fragment_item_expenses, container, false);
        Cartesian cartesian = AnyChart.column();


        mDBHelper = new DBHelper(getContext());
        sharedPreferences=getActivity().getSharedPreferences("expensetracker", Context.MODE_PRIVATE);

        try {
            //TODO: Change inputs to dynamic in DD/MM/YYYY format
            chartDataUnits=mDBHelper.fetchItemizedReport("26/06/2020","28/06/2020",sharedPreferences.getString("username",""));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<DataEntry> data = new ArrayList<>();
        for(ChartDataUnit chartDataUnit:chartDataUnits) {
            data.add(new ValueDataEntry(chartDataUnit.getExpenseName(), chartDataUnit.getExpenseAmount()));
        }
        column=cartesian.column(data);
        anyChartView=root.findViewById(R.id.itemized_chart);
        anyChartView.setChart(cartesian);
        return root;
    }
}