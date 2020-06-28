package com.expensetracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.expensetracker.ui.home.AddDailyExpenseActivity;

import java.io.Serializable;
import java.util.List;

public class ListOfExpensesAdapter extends RecyclerView.Adapter<ListOfExpensesAdapter.ViewHolder>
{
    List<Expense> expensesList;
    SharedPreferences sharedPreferences;
    String username;
    DBHelper dbHelper;
    HomeScreenActivity homeScreenActivity;

    public ListOfExpensesAdapter(HomeScreenActivity homeScreenActivity,boolean isDailyExpenseList) {
        this.homeScreenActivity = homeScreenActivity;
        dbHelper=new DBHelper(homeScreenActivity);
        sharedPreferences=homeScreenActivity.getSharedPreferences("expensetracker", Context.MODE_PRIVATE);
        username=sharedPreferences.getString("username","");
        if(isDailyExpenseList)
            expensesList=dbHelper.fetchDailyExpense(username);
        else
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Expense expense=expensesList.get(holder.getAdapterPosition());
        holder.expenseName.setText(expense.getExpenseName());
        holder.expenseAmount.setText(String.valueOf(expense.getExpenseAmount()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = v.getContext();
                Intent intent = new Intent(context, AddDailyExpenseActivity.class);
                intent.putExtra("SAVED_EXPENSE",expense.getExpenseName() );
                intent.putExtra("SAVED_EXPENSE_AMOUNT",expense.getExpenseAmount() );
                context.startActivity(intent);
            }
        });
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
