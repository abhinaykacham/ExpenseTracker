package com.expensetracker.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.expensetracker.DBHelper;
import com.expensetracker.Expense;
import com.expensetracker.R;

import java.util.ArrayList;

public class AddDailyExpenseActivity extends AppCompatActivity {

    Spinner mDailyExpenseSpinner;
    private SharedPreferences prefs;
    private DBHelper mDBHelper;
    private EditText mDailyExpenseAmount;
    private Button mBtnAddDailyExpense;
    String username;
    ArrayAdapter<Expense> mDailyExpenseAdapter;
    ArrayList<Expense> mDailyExpenseArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily_expense);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_daily_expenses);

        mDBHelper = new DBHelper(this);
        prefs = getSharedPreferences("expensetracker", MODE_PRIVATE);
        username = prefs.getString("username", "");
        mBtnAddDailyExpense = findViewById(R.id.button_adddailyexpense);
        mDailyExpenseAmount = findViewById(R.id.text_dailyexpenseamount);
        mDailyExpenseSpinner = findViewById(R.id.spinner_dailyexpense);

        //Fetch daily expenses from db
        mDailyExpenseAdapter = new ArrayAdapter<Expense>(this,android.R.layout.simple_spinner_item,mDailyExpenseArrayList);
        mDailyExpenseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDailyExpenseSpinner.setAdapter(mDailyExpenseAdapter);
        mDailyExpenseSpinner.setPrompt("Select Expense Type");
        // Add the default prompt type(Choose Expense Type)


        mBtnAddDailyExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //add entry in db
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}