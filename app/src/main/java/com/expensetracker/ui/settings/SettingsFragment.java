/* Fragment which provides helps the user for adding daily expenses */

package com.expensetracker.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.expensetracker.DBHelper;
import com.expensetracker.R;
import com.expensetracker.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends Fragment {

    private DBHelper mDBHelper;
    EditText mEdtDesiredSaving;
    EditText mEdtAnnualIncome;
    EditText mEdtMaximumDailyExpense;
    Button   mBtnSubmitFinances;
    RecyclerView savedExpensesRecyclerView;
    FloatingActionButton addSavedExpenseButton;
    private SharedPreferences prefs;
    String username;
    User userDetails;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        savedExpensesRecyclerView=root.findViewById(R.id.saved_expense_list);
        addSavedExpenseButton=root.findViewById(R.id.add_saved_expense);
        mDBHelper = new DBHelper(getContext());
        mEdtAnnualIncome=root.findViewById(R.id.m_edt_annual_income);
        mEdtDesiredSaving=root.findViewById(R.id.m_edt_desired_saving);
        mBtnSubmitFinances=root.findViewById(R.id.m_btn_submit_finances);
        mEdtMaximumDailyExpense=root.findViewById(R.id.m_edt_maximum_daily_expense);

        prefs = this.getActivity().getSharedPreferences("expensetracker", MODE_PRIVATE);

        username= prefs.getString("username", "");
        userDetails=mDBHelper.fetchUserDetails(username);
        mEdtDesiredSaving.setText(String.valueOf(userDetails.getDesiredSaving()));
        mEdtAnnualIncome.setText(String.valueOf(userDetails.getAnnualIncome()));
        mEdtMaximumDailyExpense.setText(String.valueOf(userDetails.getMaximumDailyExpense()));
        mBtnSubmitFinances.setOnClickListener(new View.OnClickListener() {
            boolean result;
            @Override
            public void onClick(View v) {
                result=mDBHelper.updateFinances(username,Integer.parseInt(String.valueOf(mEdtAnnualIncome.getText()))
                        ,Integer.parseInt(String.valueOf(mEdtDesiredSaving.getText()))
                        ,Integer.parseInt(String.valueOf(mEdtMaximumDailyExpense.getText())));
                if(result)
                    Toast.makeText(getContext(),"Update is successful",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getContext(),"Update failed",Toast.LENGTH_LONG).show();
                userDetails=mDBHelper.fetchUserDetails(username);
                mEdtDesiredSaving.setText(String.valueOf(userDetails.getDesiredSaving()));
                mEdtAnnualIncome.setText(String.valueOf(userDetails.getAnnualIncome()));
                mEdtMaximumDailyExpense.setText(String.valueOf(userDetails.getMaximumDailyExpense()));
            }
        });

        return root;
    }
}