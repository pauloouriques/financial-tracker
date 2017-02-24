package com.toptal.app.financialtracker.rest.tasks;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.toptal.app.financialtracker.entities.Expense;
import com.toptal.app.financialtracker.entities.LoginResponse;
import com.toptal.app.financialtracker.entities.User;
import com.toptal.app.financialtracker.login.LoginActivity;
import com.toptal.app.financialtracker.main.MainActivity;
import com.toptal.app.financialtracker.persistence.PrefsHelper;
import com.toptal.app.financialtracker.rest.ApiError;
import com.toptal.app.financialtracker.rest.HttpRetrofitClient;
import com.toptal.app.financialtracker.rest.OnTaskListener;

import java.util.ArrayList;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Class that represents the task to get groups.
 */
public class LoginTask extends AsyncTask<String, Void, Object> {
    /**
     * The context.
     */
    private Context mContext;

    /**
     * StatusCallback to use with onTaskFailed or onTaskCompleted.
     */
    private OnTaskListener mListener;

    /**
     * Constructor method.
     *
     * @param context - The context.
     */
    public LoginTask(final Context context, final OnTaskListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    @Override
    protected final Object doInBackground(final String... params) {
        try {
            HttpRetrofitClient httpRetrofitClient = new HttpRetrofitClient(mContext);
            httpRetrofitClient.mClient.login(params[0], params[1]);

            Call<User> call = new HttpRetrofitClient(mContext).mClient.login(params[0], params[1]);
            Response<User> response = call.execute();

            PrefsHelper.putUser(mContext, response.body());
            final Headers headerList = response.headers();

            for (final String headerName : headerList.names()) {
                if (headerName.equalsIgnoreCase("set-cookie")) {
                    PrefsHelper.putCookies(mContext, headerList.get(headerName));
                    break;
                }
            }

            if(response.isSuccessful()){
                return response.body();
            }

        } catch (Exception e) {
            return new ApiError(e.hashCode(), e.getMessage());
        }
        return new ApiError(500, "Internal server error.");
    }

    @Override
    protected void onPostExecute(Object result) {
        if (result instanceof User) {
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
