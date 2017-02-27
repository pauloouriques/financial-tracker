package com.toptal.app.financialtracker.main;

import android.app.Activity;
import android.content.DialogInterface;
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
import com.toptal.app.financialtracker.login.LoginActivity;
import com.toptal.app.financialtracker.persistence.PrefsHelper;
import com.toptal.app.financialtracker.rest.ApiError;
import com.toptal.app.financialtracker.rest.OnTaskListener;
import com.toptal.app.financialtracker.rest.tasks.DeleteUserTask;
import com.toptal.app.financialtracker.rest.tasks.EditUserTask;
import com.toptal.app.financialtracker.utils.Constants;
import com.toptal.app.financialtracker.utils.SharedMethods;


/**
 * This class represents the add expense Activity.
 */

public class EditUserActivity extends AppCompatActivity implements View.OnClickListener {

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        User loggedUser = PrefsHelper.getUser(this);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mUser = User.getFromJson(bundle.getString(Constants.USER_TO_EDIT));
        } else {
            mUser = loggedUser;
        }

        if (loggedUser != null && loggedUser.type.equals(User.TYPE_ADMIN)) {
            Spinner spinner = (Spinner) findViewById(R.id.types_spinner);
            findViewById(R.id.user_type_layout).setVisibility(View.VISIBLE);
            spinner.setSelection(mUser.getCategoryIndex(this));
        }

        if (loggedUser.type.equals(User.TYPE_ADMIN) || loggedUser.type.equals(User.TYPE_MANAGER)) {
            if (!mUser.id.equals(loggedUser.id)) {
                findViewById(R.id.old_password_edit_text).setVisibility(View.GONE);
            }
        }

        TextView name = (TextView) findViewById(R.id.name_edit_text);
        name.setText(mUser.name);
        TextView email = (TextView) findViewById(R.id.email_edit_text);
        email.setText(mUser.email);


        findViewById(R.id.edit_user_button).setOnClickListener(this);
        findViewById(R.id.remove_user_button).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_user_button:
                addUser();
                break;
            case R.id.remove_user_button:
                SharedMethods.showDialog(
                        EditUserActivity.this,
                        getString(R.string.delete_user_text),
                        getString(R.string.delete_user_dialog_text),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                deleteUser();
                                dialog.cancel();
                            }
                        });
                break;
        }
    }



    private void deleteUser() {
        new DeleteUserTask(this, new OnTaskListener() {
            @Override
            public void onSuccess(AsyncTask task, Object result) {
                Intent returnIntent = new Intent();
                User deletedUser = (User) result;
                returnIntent.putExtra("result", deletedUser.toJsonString());
                returnIntent.putExtra("deleted", true);
                setResult(Activity.RESULT_OK, returnIntent);

                finish();
            }

            @Override
            public void onFailure(AsyncTask task, Object error) {

            }
        }).execute(mUser);
    }

    private void addUser() {
        TextView name = (TextView) findViewById(R.id.name_edit_text);
        TextView email = (TextView) findViewById(R.id.email_edit_text);
        TextView password = (TextView) findViewById(R.id.password_edit_text);
        TextView oldPassword = (TextView) findViewById(R.id.old_password_edit_text);
        TextView passwordConfirmation = (TextView) findViewById(R.id.password_confirmation_edit_text);

        if (name.getText().toString().equals("")) {
            Toast.makeText(this, getString(R.string.no_name_text), Toast.LENGTH_LONG).show();
        } else if (email.getText().toString().equals("")) {
            Toast.makeText(this, getString(R.string.no_email_text), Toast.LENGTH_LONG).show();
        } else if (!SharedMethods.isValidEmail(email.getText().toString())) {
            Toast.makeText(this, getString(R.string.invalid_email_text), Toast.LENGTH_LONG).show();
        } else if (!oldPassword.getText().toString().equals("")) {
            if(!oldPassword.getText().toString().equals(PrefsHelper.getUser(EditUserActivity.this).password)) {
                Toast.makeText(this, getString(R.string.old_password_error_text), Toast.LENGTH_LONG).show();
            } else if (password.getText().toString().equals("")) {
                Toast.makeText(this, getString(R.string.no_password_text), Toast.LENGTH_LONG).show();
            } else if (!password.getText().toString().equals(passwordConfirmation.getText().toString())) {
                Toast.makeText(this, getString(R.string.no_password_match_text), Toast.LENGTH_LONG).show();
            }
        } else if (oldPassword.getVisibility() == View.GONE && !password.getText().toString().equals("") && !password.getText().toString().equals(passwordConfirmation.getText().toString())) {
            Toast.makeText(this, getString(R.string.no_password_match_text), Toast.LENGTH_LONG).show();
        } else {
            mUser.name = name.getText().toString();
            mUser.email = email.getText().toString();
            if (!password.getText().toString().equals("")) {
                mUser.password = password.getText().toString();
            }

            User loggedUser = PrefsHelper.getUser(this);
            if (loggedUser != null && loggedUser.type.equals(User.TYPE_ADMIN)) {
                Spinner spinner = (Spinner) findViewById(R.id.types_spinner);
                mUser.type = spinner.getSelectedItem().toString().toUpperCase();
            } else {
                mUser.type = User.TYPE_USER;
            }

            new EditUserTask(this, new OnTaskListener() {
                @Override
                public void onSuccess(AsyncTask task, Object result) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", ((User) result).toJsonString());
                    returnIntent.putExtra("deleted", false);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }

                @Override
                public void onFailure(AsyncTask task, Object error) {
                    Toast.makeText(EditUserActivity.this, ((ApiError) error).getMessage(), Toast.LENGTH_LONG).show();
                }
            }).execute(mUser);
        }
    }
}
