package com.expensetracker.ui.help;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HelpViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HelpViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Please contact ExpenseTracker@gmail.com for any queries");
    }

    public LiveData<String> getText() {
        return mText;
    }
}