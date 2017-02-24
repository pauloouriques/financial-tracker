package com.toptal.app.financialtracker.login;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.toptal.app.financialtracker.R;
import com.toptal.app.financialtracker.entities.User;
import com.toptal.app.financialtracker.main.MainActivity;
import com.toptal.app.financialtracker.persistence.PrefsHelper;
import com.toptal.app.financialtracker.rest.OnTaskListener;
import com.toptal.app.financialtracker.rest.tasks.LoginTask;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * Class that represents the Login activity.
 */
public class LoginActivity extends Activity {

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        PrefsHelper.clearCookie(this);
        PrefsHelper.clearPrefs(this);

        final TextView username = (TextView) findViewById(R.id.email);
        final TextView password = (TextView) findViewById(R.id.password);

        username.setText("aaa@aaa.com");
        password.setText("aaa");

        if (!PrefsHelper.getCookie(LoginActivity.this).equals("")) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        findViewById(R.id.email_sign_in_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                final MaterialDialog progress = new MaterialDialog.Builder(LoginActivity.this)
                        .title(getString(R.string.ld_dialog_loading))
                        .content(getString(R.string.ld_dialog_please_wait))
                        .progress(true, 0)
                        .show();

                View view = getCurrentFocus();
                if (view != null) {
                    final InputMethodManager imm = (InputMethodManager) getSystemService(
                            INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                new LoginTask(LoginActivity.this, new OnTaskListener() {
                    @Override
                    public void onSuccess(AsyncTask task, Object result) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                        progress.dismiss();
                    }

                    @Override
                    public void onFailure(AsyncTask task, Object error) {
                        Toast.makeText(LoginActivity.this, R.string.error_invalid_email_or_password,
                                Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                }).execute( username.getText().toString(),
                        password.getText().toString());




            }
        });
    }

}

