package com.toptal.app.financialtracker.rest.tasks;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.toptal.app.financialtracker.entities.Expense;
import com.toptal.app.financialtracker.rest.ApiError;
import com.toptal.app.financialtracker.rest.HttpRetrofitClient;
import com.toptal.app.financialtracker.rest.OnTaskListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Class that represents the task to get insights ads.
 */
public class GetExpensesTask extends AsyncTask<String, Void, Object> {

    /**
     * StatusCallback to use with onTaskFailed or onTaskCompleted.
     */
    private OnTaskListener mListener;

    /**
     * Application context.
     */
    private Context mContext;


    /**
     * Constructor method.
     *
     * @param listener - The listener.
     */
    public GetExpensesTask(final Context context, final OnTaskListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    @Override
    protected final Object doInBackground(final String... params) {
        try {
            Call<ArrayList<Expense>> call = new HttpRetrofitClient(mContext).mClient.getExpenses();
            Response<ArrayList<Expense>> response = call.execute();

            if(response.isSuccessful()){
                return response.body();
            }
            return new ApiError(0, "");
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }

    }


    @Override
    protected void onPostExecute(Object result) {
        if (result instanceof ArrayList) {
            Log.w(getClass().getSimpleName(), "onSuccess()");
            if (mListener != null)
                mListener.onSuccess(this, result);
        } else {
            Log.w(getClass().getSimpleName(), "onFailure()");
            if (mListener != null)
                mListener.onFailure(this, result);
        }
    }
}
