package com.expensetracker.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.expensetracker.DBHelper;
import com.expensetracker.Expense;
import com.expensetracker.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddDailyExpenseActivity extends AppCompatActivity {

    Spinner mDailyExpenseSpinner;
    private SharedPreferences prefs;
    private DBHelper mDBHelper;
    private EditText mDailyExpenseAmount;
    private Button mBtnAddDailyExpense;
    String username;
    ArrayAdapter<Expense> mDailyExpenseAdapter;
    List<Expense> mDailyExpenseArrayList = new ArrayList<>();
    Expense dailyExpense,expenseType;
    DateFormat dateFormat;
    String date;
    boolean result;
    String TAG=this.getClass().getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily_expense);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_daily_expenses);

        dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        date= dateFormat.format(Calendar.getInstance().getTime());
        mDBHelper = new DBHelper(this);
        prefs = getSharedPreferences("expensetracker", MODE_PRIVATE);
        username = prefs.getString("username", "");
        mDailyExpenseArrayList=mDBHelper.fetchSavedExpense(username);
        mBtnAddDailyExpense = findViewById(R.id.button_adddailyexpense);
        mDailyExpenseAmount = findViewById(R.id.text_dailyexpenseamount);
        mDailyExpenseSpinner = findViewById(R.id.spinner_dailyexpense);

        mDailyExpenseAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,mDailyExpenseArrayList);
        mDailyExpenseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDailyExpenseSpinner.setAdapter(mDailyExpenseAdapter);
        mDailyExpenseSpinner.setPrompt("Select Expense Type");
        mDailyExpenseSpinner.setSelection(0);
        mDailyExpenseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Expense selectedExpense=(Expense) parent.getSelectedItem();
                mDailyExpenseAmount.setText(selectedExpense.getExpenseAmount());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mBtnAddDailyExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseType=(Expense)mDailyExpenseSpinner.getSelectedItem();
                dailyExpense=new Expense();
                dailyExpense.setSavedExpenseID(expenseType.getSavedExpenseID());
                dailyExpense.setExpenseAmount(Integer.valueOf(String.valueOf(mDailyExpenseAmount.getText())));
                dailyExpense.setCreatedDate(date);
                result=mDBHelper.addDailyExpense(dailyExpense);
                if(result){
                    Log.d(TAG,"Daily expense added!!");
                    onBackPressed();
                }
                else {
                    Toast.makeText(
                            getApplicationContext()
                            , "Expense addition failed"
                            , Toast.LENGTH_LONG
                    ).show();
                    Log.d(TAG,"Expense addition failed");
                }

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}