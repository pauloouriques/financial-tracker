package com.toptal.app.financialtracker.rest.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.toptal.app.financialtracker.R;
import com.toptal.app.financialtracker.dialogs.ProgressDialog;
import com.toptal.app.financialtracker.entities.User;
import com.toptal.app.financialtracker.rest.ApiError;
import com.toptal.app.financialtracker.rest.HttpRetrofitClient;
import com.toptal.app.financialtracker.rest.OnTaskListener;

import retrofit2.Call;
import retrofit2.Response;

/**
 * This class represents the find user task.
 */
public class EditUserTask extends AsyncTask<User, Void, Object> {

    private Activity mActivity;
    private ProgressDialog mPDialog;
    private OnTaskListener mListener;

    /**
     * Constructor method.
     *
     * @param listener - Task listener.
     */
    public EditUserTask(Activity activity, OnTaskListener listener) {
        this.mActivity = activity;
        this.mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        mPDialog = new ProgressDialog(mActivity);
        mPDialog.show();
    }

    @Override
    protected Object doInBackground(User... params) {
        try {

            Call<User> call;
            call = new HttpRetrofitClient(mActivity).mClient.editUser(params[0]);
            Response<User> response = call.execute();

            switch (response.code()) {
                case 204:
                    return response.body();
                case 500:
                    return new ApiError(500, response.message());
                case 409:
                    return new ApiError(409, mActivity.getString(R.string.user_already_exist_text));
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
