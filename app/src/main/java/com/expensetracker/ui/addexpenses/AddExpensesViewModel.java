package com.expensetracker.ui.addexpenses;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddExpensesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AddExpensesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Add Expenses fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}