package com.expensetracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListOfExpensesAdapter extends RecyclerView.Adapter<ListOfExpensesAdapter.ViewHolder>
{
    List<Expense> expensesList;
    SharedPreferences sharedPreferences;
    String username;
    DBHelper dbHelper;
    HomeScreenActivity homeScreenActivity;

    public ListOfExpensesAdapter(HomeScreenActivity homeScreenActivity) {
        this.homeScreenActivity = homeScreenActivity;
        dbHelper=new DBHelper(homeScreenActivity);
        sharedPreferences=homeScreenActivity.getSharedPreferences("expensetracker", Context.MODE_PRIVATE);
        username=sharedPreferences.getString("username","");
        expensesList=dbHelper.fetchSavedExpense(username);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.make_model_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expense expense=expensesList.get(holder.getAdapterPosition());
        holder.expenseName.setText(expense.getExpenseName());
        holder.expenseAmount.setText(String.valueOf(expense.getExpenseAmount()));

    }

    @Override
    public int getItemCount() {
        return expensesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView expenseName,expenseAmount;
        LinearLayout expenseEntry;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseAmount=itemView.findViewById(R.id.expense_name);
            expenseName=itemView.findViewById(R.id.expense_amount);
            expenseEntry=itemView.findViewById(R.id.cv_layout);
        }


    }
}
