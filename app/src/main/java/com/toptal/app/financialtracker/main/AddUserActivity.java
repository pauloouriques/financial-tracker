package com.toptal.app.financialtracker.main;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.toptal.app.financialtracker.R;
import com.toptal.app.financialtracker.entities.Expense;
import com.toptal.app.financialtracker.entities.User;
import com.toptal.app.financialtracker.persistence.PrefsHelper;
import com.toptal.app.financialtracker.rest.ApiError;
import com.toptal.app.financialtracker.rest.OnTaskListener;
import com.toptal.app.financialtracker.rest.tasks.AddExpenseTask;
import com.toptal.app.financialtracker.rest.tasks.AddUserTask;
import com.toptal.app.financialtracker.utils.Constants;
import com.toptal.app.financialtracker.utils.SharedMethods;


/**
 * This class represents the add expense Activity.
 */

public class AddUserActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        User user = PrefsHelper.getUser(this);
        if (user != null && user.type == User.TYPE_ADMIN) {
            Spinner spinner = (Spinner) findViewById(R.id.types_spinner);
            spinner.setVisibility(View.VISIBLE);
            spinner.setOnItemSelectedListener(this);
        }

        findViewById(R.id.add_user_button).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_user_button:
                addUser();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }



    private void addUser() {
        TextView name = (TextView) findViewById(R.id.name_edit_text);
        TextView email = (TextView) findViewById(R.id.email_edit_text);
        TextView password = (TextView) findViewById(R.id.password_edit_text);
        TextView passwordConfirmation = (TextView) findViewById(R.id.password_confirmation_edit_text);

        if (name.getText().toString().equals("")) {
            Toast.makeText(this, getString(R.string.no_name_text), Toast.LENGTH_LONG).show();
        } else if (email.getText().toString().equals("")) {
            Toast.makeText(this, getString(R.string.no_email_text), Toast.LENGTH_LONG).show();
        } else if (!SharedMethods.isValidEmail(email.getText().toString())) {
            Toast.makeText(this, getString(R.string.invalid_email_text), Toast.LENGTH_LONG).show();
        } else if (password.getText().toString().equals("")) {
            Toast.makeText(this, getString(R.string.no_password_text), Toast.LENGTH_LONG).show();
        } else if (!password.getText().toString().equals(passwordConfirmation.getText().toString())) {
            Toast.makeText(this, getString(R.string.no_password_match_text), Toast.LENGTH_LONG).show();
        } else {
            User user = new User();
            user.name = name.getText().toString();
            user.email = email.getText().toString();
            user.password = password.getText().toString();

            User loggedUser = PrefsHelper.getUser(this);
            if (loggedUser != null && loggedUser.type == User.TYPE_ADMIN) {
                Spinner spinner = (Spinner) findViewById(R.id.types_spinner);
                user.type = spinner.getSelectedItem().toString().toUpperCase();
            } else {
                user.type = User.TYPE_USER;
            }

            new AddUserTask(this, new OnTaskListener() {
                @Override
                public void onSuccess(AsyncTask task, Object result) {
                    Toast.makeText(AddUserActivity.this, getString(R.string.user_added_text), Toast.LENGTH_LONG).show();
                    finish();
                }

                @Override
                public void onFailure(AsyncTask task, Object error) {
                    Toast.makeText(AddUserActivity.this, ((ApiError) error).getMessage(), Toast.LENGTH_LONG).show();
                }
            }).execute(user);
        }


    }

}
