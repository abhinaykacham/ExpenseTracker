/* Home Fragment which displays saved preferences and user daily expenses*/

package com.expensetracker.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.expensetracker.HomeScreenActivity;
import com.expensetracker.ListOfExpensesAdapter;
import com.expensetracker.R;
import com.expensetracker.ui.settings.AddExpenseActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment{

    FloatingActionButton addDailyExpenseButton;
    RecyclerView dailyExpensesRecyclerView;
    boolean dailyExpense=true;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        addDailyExpenseButton=root.findViewById(R.id.add_daily_expense);

        dailyExpensesRecyclerView=root.findViewById(R.id.daily_expenses_list);
        dailyExpensesRecyclerView.setAdapter(new ListOfExpensesAdapter((HomeScreenActivity) getActivity(),dailyExpense));

        addDailyExpenseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddDailyExpenseActivity.class));
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        dailyExpensesRecyclerView.setAdapter(new ListOfExpensesAdapter((HomeScreenActivity) getActivity(),dailyExpense));
    }

}