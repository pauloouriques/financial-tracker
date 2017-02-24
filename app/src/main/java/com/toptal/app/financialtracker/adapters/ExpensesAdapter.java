package com.toptal.app.financialtracker.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.toptal.app.financialtracker.R;
import com.toptal.app.financialtracker.entities.Expense;
import com.toptal.app.financialtracker.main.MainActivity;
import com.toptal.app.financialtracker.utils.Constants;
import com.toptal.app.financialtracker.utils.SharedMethods;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


/**
 * Class that represents the statement objects adapter.
 */
public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ViewHolder> {

    private ArrayList<Expense> mExpenses = new ArrayList<Expense>();
    private ArrayList<Expense> mFilteredExpenses = new ArrayList<Expense>();
    private TransactionTextFilter mTextFilter;

    private MainActivity mActivity;
    private View mFailTextFilterLayout;

    public ExpensesAdapter(final MainActivity activity, final ArrayList<Expense> data) {
        Collections.sort(data, new Expense.TransactionComparator());
        this.mExpenses = data;
        this.mFilteredExpenses.addAll(data);
        mActivity = activity;
        this.mTextFilter = new TransactionTextFilter();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.expense_item, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Expense expense = mExpenses.get(position);
        holder.expenseDescription.setText(expense.description);
        holder.expenseComment.setText(expense.comment);
        holder.expenseAmount.setText(SharedMethods.formatMonetaryValue(expense.amount));
        try {
            Date date = Constants.DATE_FORMAT_FROM_SERVER.parse(expense.date);
            holder.expenseDate.setText(Constants.DATE_FORMAT.format(date));
            holder.expenseTime.setText(Constants.TIME_FORMAT.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mFilteredExpenses.size();
    }


    public Filter getTextFilter(final View failFilterLayout) {
        mFailTextFilterLayout = failFilterLayout;
        return mTextFilter;
    }

    /**
     * Clear filter.
     */
    public void clearFilter() {
        if (mFilteredExpenses.size() != mExpenses.size()) {
            mFilteredExpenses.clear();
            mFilteredExpenses.addAll(mExpenses);
            notifyDataSetChanged();
        }
    }

    /**
     * UpdateList.
     */
    public void updateList(final ArrayList<Expense> transactions) {
        Collections.sort(transactions, new Expense.TransactionComparator());
        mExpenses = transactions;
        mFilteredExpenses.clear();
        mFilteredExpenses.addAll(mExpenses);
        notifyDataSetChanged();
    }

    /**
     * Order list by older transactions.
     */
    public void orderByOlder() {
        Collections.sort(mFilteredExpenses, new Expense.TransactionComparator());
        Collections.sort(mExpenses, new Expense.TransactionComparator());
        notifyDataSetChanged();
    }

    /**
     * Order list by recenter transactions.
     */
    public void orderByNewer() {
        Collections.sort(mFilteredExpenses, new Expense.TransactionComparator());
        Collections.sort(mExpenses, new Expense.TransactionComparator());
        Collections.reverse(mFilteredExpenses);
        Collections.reverse(mExpenses);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView expenseAmount;
        TextView expenseDescription;
        TextView expenseComment;
        TextView expenseDate;
        TextView expenseTime;
        ImageView expenseCategoryImage;


        public ViewHolder(View itemView) {
            super(itemView);
            expenseAmount = (TextView) itemView.findViewById(R.id.expense_amount);
            expenseDescription = (TextView) itemView.findViewById(R.id.expense_description);
            expenseComment = (TextView) itemView.findViewById(R.id.expense_comment);
            expenseDate = (TextView) itemView.findViewById(R.id.expense_date);
            expenseTime= (TextView) itemView.findViewById(R.id.expense_time);
            expenseCategoryImage = (ImageView) itemView.findViewById(R.id.statement_item_user_pic);
        }
    }


    /**
     * Class that represents the Expense Filter.
     */
    private class TransactionTextFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            mFilteredExpenses.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() == 0) {
                mFilteredExpenses.addAll(mExpenses);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final Expense transaction : mExpenses) {
                    if (transaction.description.toLowerCase().contains(filterPattern)) {
                        mFilteredExpenses.add(transaction);
                    }
                    if (transaction.comment.toLowerCase().contains(filterPattern)) {
                        mFilteredExpenses.add(transaction);
                    }
                }
            }
            results.values = mFilteredExpenses;
            results.count = mFilteredExpenses.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notifyDataSetChanged();
            if (mFailTextFilterLayout != null) {
                if (results.count > 0) {
                    mFailTextFilterLayout.setVisibility(View.GONE);
                } else {
                    mFailTextFilterLayout.setVisibility(View.VISIBLE);
                }
            }
        }
    }


}
