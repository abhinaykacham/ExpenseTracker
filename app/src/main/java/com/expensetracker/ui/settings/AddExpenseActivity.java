package com.expensetracker.ui.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.expensetracker.DBHelper;
import com.expensetracker.R;

public class AddExpenseActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private DBHelper mDBHelper;
    private EditText mExpenseName, mExpenseAmount;
    private Button mBtnAddExpense;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_expenses);

        mDBHelper = new DBHelper(this);
        prefs = getSharedPreferences("expensetracker", MODE_PRIVATE);
        username = prefs.getString("username", "");
        mBtnAddExpense = findViewById(R.id.button_addexpense);
        mExpenseName = findViewById(R.id.text_expensetitle);
        mExpenseAmount = findViewById(R.id.text_expenseamount);
        mBtnAddExpense.setOnClickListener(new View.OnClickListener() {
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