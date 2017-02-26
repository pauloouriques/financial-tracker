package com.toptal.app.financialtracker.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.toptal.app.financialtracker.R;
import com.toptal.app.financialtracker.main.AddUserActivity;
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
public class LoginActivity extends Activity implements OnClickListener{

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        PrefsHelper.clearCookie(this);
        PrefsHelper.clearPrefs(this);

        if (!PrefsHelper.getCookie(LoginActivity.this).equals("")) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.create_account_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.email_sign_in_button:
                final TextView username = (TextView) findViewById(R.id.email);
                final TextView password = (TextView) findViewById(R.id.password);

                username.setText("a@a.com");
                password.setText("aaa");

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
                    }

                    @Override
                    public void onFailure(AsyncTask task, Object error) {
                        Toast.makeText(LoginActivity.this, R.string.error_invalid_email_or_password,
                                Toast.LENGTH_SHORT).show();
                    }
                }).execute( username.getText().toString(),
                        password.getText().toString());
                break;
            case R.id.create_account_button:
                startActivity(new Intent(LoginActivity.this, AddUserActivity.class));
                break;
        }
    }
}

