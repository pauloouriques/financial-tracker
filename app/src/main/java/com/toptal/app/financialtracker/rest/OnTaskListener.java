package com.toptal.app.financialtracker.rest;

import android.os.AsyncTask;

/**
 * This class handles all task events.
 */
public interface OnTaskListener {

    /**
     * Called when PaymentDetailsActivity task was executed with success.
     * @param task - The task.
     * @param result - Task result.
     */
    void onSuccess(AsyncTask task, Object result);

    /**
     * Called when PaymentDetailsActivity task failed during its execution.
     * @param task - The task.
     * @param error - Task error.
     */
    void onFailure(AsyncTask task, Object error);

}
