package com.toptal.app.financialtracker.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.toptal.app.financialtracker.R;
import com.toptal.app.financialtracker.entities.User;
import com.toptal.app.financialtracker.main.AdminMainActivity;
import com.toptal.app.financialtracker.main.EditExpenseActivity;
import com.toptal.app.financialtracker.main.EditUserActivity;
import com.toptal.app.financialtracker.main.MainActivity;
import com.toptal.app.financialtracker.persistence.PrefsHelper;
import com.toptal.app.financialtracker.utils.Constants;
import com.toptal.app.financialtracker.utils.SharedMethods;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


/**
 * Class that represents the statement objects adapter.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private ArrayList<User> mUsers = new ArrayList<User>();
    private ArrayList<User> mFilteredUsers = new ArrayList<User>();
    private UserTextFilter mTextFilter;

    private AdminMainActivity mActivity;
    private View mFailTextFilterLayout;

    public UsersAdapter(final AdminMainActivity activity, final ArrayList<User> data) {
        Collections.sort(data, new User.nameComparator());
        this.mUsers = data;
        this.mFilteredUsers.addAll(data);
        mActivity = activity;
        this.mTextFilter = new UserTextFilter();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.user_list_item, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        User user = mFilteredUsers.get(position);
        holder.userName.setText(user.name);
        holder.userRole.setText(user.type);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, EditUserActivity.class);
                intent.putExtra(
                        Constants.USER_TO_EDIT,
                        mFilteredUsers.get(position).toJsonString());
                mActivity.startActivityForResult(
                        intent,
                        mActivity.EDIT_USER_REQUEST_CODE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mFilteredUsers.size();
    }


    /**
     * Get the text filter.
     * @return the text filter.
     */
    public Filter getTextFilter() {
        return mTextFilter;
    }


    /**
     * Clear filter.
     */
    public void clearFilter() {
        if (mFilteredUsers.size() != mUsers.size()) {
            mFilteredUsers.clear();
            mFilteredUsers.addAll(mUsers);
            notifyDataSetChanged();
        }
    }

    /**
     * UpdateList.
     */
    public void updateList(final ArrayList<User> transactions) {
        Collections.sort(transactions, new User.nameComparator());
        mUsers = transactions;
        mFilteredUsers.clear();
        mFilteredUsers.addAll(mUsers);
        notifyDataSetChanged();
    }

    /**
     * UpdateList.
     */
    public void updateList(final User user, final boolean delete) {
        int indexToUpdate = -1;
        for (int i = 0; i < mFilteredUsers.size(); i++) {
            if (mFilteredUsers.get(i).id.equals(user.id)) {
                indexToUpdate = i;
                break;
            }
        }
        if (indexToUpdate >= 0) {
            if (delete) {
                mFilteredUsers.remove(indexToUpdate);
            } else {
                mFilteredUsers.set(indexToUpdate, user);
            }
        }
        Collections.sort(mFilteredUsers, new User.nameComparator());
        notifyDataSetChanged();
    }

    /**
     * UpdateList.
     */
    public void updateList(final User user) {
        updateList(user, false);
    }

    /**
     * Order list by older transactions.
     */
    public void orderByOlder() {
        Collections.sort(mFilteredUsers, new User.nameComparator());
        Collections.sort(mUsers, new User.nameComparator());
        notifyDataSetChanged();
    }

    /**
     * Order list by recenter transactions.
     */
    public void orderByNewer() {
        Collections.sort(mFilteredUsers, new User.nameComparator());
        Collections.sort(mUsers, new User.nameComparator());
        Collections.reverse(mFilteredUsers);
        Collections.reverse(mUsers);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView userRole;
        ImageView userPic;


        public ViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.user_name_tv);
            userRole = (TextView) itemView.findViewById(R.id.user_role_tv);
            userPic = (ImageView) itemView.findViewById(R.id.user_picture_riv);

        }
    }


    /**
     * Class that represents the Expense Filter.
     */
    private class UserTextFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            mFilteredUsers.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() == 0) {
                mFilteredUsers.addAll(mUsers);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final User expense : mUsers) {
                    if (expense.name.toLowerCase().contains(filterPattern.toLowerCase())) {
                        mFilteredUsers.add(expense);
                    }
                }
            }
            results.values = mFilteredUsers;
            results.count = mFilteredUsers.size();
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
