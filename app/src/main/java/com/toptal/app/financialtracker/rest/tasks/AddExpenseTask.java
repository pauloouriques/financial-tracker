package com.toptal.app.financialtracker.rest.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.toptal.app.financialtracker.entities.Expense;
import com.toptal.app.financialtracker.rest.ApiError;
import com.toptal.app.financialtracker.rest.HttpRetrofitClient;
import com.toptal.app.financialtracker.rest.OnTaskListener;

import java.io.File;
import java.security.acl.Group;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * This class represents the find user task.
 */
public class AddExpenseTask extends AsyncTask<Expense, Void, Object> {

    private Activity mActivity;
    private ProgressDialog mPDialog;
    private OnTaskListener mListener;

    /**
     * Constructor method.
     *
     * @param listener - Task listener.
     */
    public AddExpenseTask(Activity activity, OnTaskListener listener) {
        this.mActivity = activity;
        this.mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        mPDialog = new ProgressDialog(mActivity);
        mPDialog.show();
    }

    @Override
    protected Object doInBackground(Expense... params) {
        try {

            Call<Expense> call;
            call = new HttpRetrofitClient(mActivity).mClient.addExpense(params[0]);
            Response<Expense> response = call.execute();

            switch (response.code()) {
                case 204:
                    return response.body();
                case 500:
                    return new ApiError(500, response.message());
                default:
                    return response.body();
            }


        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        mPDialog.dismiss();

        if (result instanceof ApiError || result instanceof Exception) {
            Log.w(getClass().getSimpleName(), "(onFailure)");
            if (mListener != null) {
                mListener.onFailure(this, result);
            }
        } else {
            Log.w(getClass().getSimpleName(), "onSuccess()");
            if (mListener != null) {
                mListener.onSuccess(this, result);
            }
        }
    }


}
