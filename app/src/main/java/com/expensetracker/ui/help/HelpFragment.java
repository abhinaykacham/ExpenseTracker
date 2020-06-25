/* Help Fragment which helps the user to contact team */
package com.expensetracker.ui.help;

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

public class HelpFragment extends Fragment {

    private HelpViewModel mHelpViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mHelpViewModel =
                ViewModelProviders.of(this).get(HelpViewModel.class);
        View root = inflater.inflate(R.layout.fragment_help, container, false);
        final TextView textView = root.findViewById(R.id.text_help);
        mHelpViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}