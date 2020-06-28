package com.expensetracker.ui.reporting;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Bar;
import com.expensetracker.ChartDataUnit;
import com.expensetracker.DBHelper;
import com.expensetracker.R;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DailyExpensesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyExpensesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button mSelectDate;
    private AnyChartView anyChartView;
    DBHelper mDBHelper;
    SharedPreferences sharedPreferences;
    List<ChartDataUnit> chartDataUnits;
    MaterialDatePicker picker;
    String fromDate,toDate;
    public DailyExpensesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DailyExpensesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DailyExpensesFragment newInstance(String param1, String param2) {
        DailyExpensesFragment fragment = new DailyExpensesFragment();
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

        final View root  = inflater.inflate(R.layout.fragment_daily_expenses, container, false);
        mSelectDate = root.findViewById(R.id.button_select_date);
        final Cartesian cartesian = AnyChart.bar();
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
        Bar bar = cartesian.bar(data);
        anyChartView = (AnyChartView) root.findViewById(R.id.any_chart_view);
        anyChartView.setChart(cartesian);
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.dateRangePicker();
        picker = builder.build();
        mSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Hellp",Toast.LENGTH_LONG).show();
                picker.show(getActivity().getSupportFragmentManager(), picker.toString());
            }
        });

        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {

                Pair<Long, Long> t = (Pair<Long, Long>) picker.getSelection();
                fromDate= new SimpleDateFormat("dd/MM/yyyy").format(new Date(t.first));
                toDate= new SimpleDateFormat("dd/MM/yyyy").format(new Date(t.second));
                Toast.makeText(getContext(),fromDate+" "+toDate,Toast.LENGTH_LONG).show();
              /*  anyChartView.clear();
                Pie pie = AnyChart.pie();

                List<DataEntry> data = new ArrayList<>();
                data.add(new ValueDataEntry("John", 10000));
                data.add(new ValueDataEntry("Jake", 12000));
                data.add(new ValueDataEntry("Peter", 18000));

                anyChartView = (AnyChartView) root.findViewById(R.id.any_chart_view);
                anyChartView.setChart(pie);*/
            }
        });

        return root;
    }

}