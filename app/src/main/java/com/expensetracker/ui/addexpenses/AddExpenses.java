/* Fragment which provides helps the user for adding daily expenses */

package com.expensetracker.ui.addexpenses;

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

import com.expensetracker.R;

public class AddExpenses extends Fragment {

    private AddExpensesViewModel mAddExpensesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mAddExpensesViewModel =
                ViewModelProviders.of(this).get(AddExpensesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_addexpenses, container, false);
        final TextView textView = root.findViewById(R.id.text_addexpenses);
        mAddExpensesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}