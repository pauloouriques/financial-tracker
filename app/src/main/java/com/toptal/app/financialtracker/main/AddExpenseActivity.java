package com.toptal.app.financialtracker.main;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.toptal.app.financialtracker.R;
import com.toptal.app.financialtracker.entities.Expense;
import com.toptal.app.financialtracker.rest.OnTaskListener;
import com.toptal.app.financialtracker.rest.tasks.AddExpenseTask;
import com.toptal.app.financialtracker.utils.Constants;
import com.toptal.app.financialtracker.watchers.NumberTextWatcher;

import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * This class represents the add expense Activity.
 */

public class AddExpenseActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        Spinner spinner = (Spinner) findViewById(R.id.categories_spinner);
        spinner.setOnItemSelectedListener(this);

        EditText valueEditText = (EditText) findViewById(R.id.expense_value_text_view);
        valueEditText.addTextChangedListener(new NumberTextWatcher(valueEditText));

        mCalendar = new GregorianCalendar();

        ((TextView) findViewById(R.id.expense_date)).setText(
                Constants.DATE_FORMAT.format(mCalendar.getTime()));
        ((TextView) findViewById(R.id.expense_time)).setText(
                Constants.TIME_FORMAT.format(mCalendar.getTime()));

        findViewById(R.id.add_expense_button).setOnClickListener(this);
        findViewById(R.id.expense_date_button).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_expense_button:
                addExpense();
                break;
            case R.id.expense_date_button:
                showDatePicker();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /**
     * Show date picker.
     */
    private void showDatePicker() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        mCalendar = new GregorianCalendar(
                                year,
                                monthOfYear,
                                dayOfMonth);

                        ((TextView) findViewById(R.id.expense_date)).setText(
                                Constants.DATE_FORMAT.format(mCalendar.getTime()));

                        showTimePicker();
                    }
                },
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    /**
     * Show time picker
     */
    private void showTimePicker() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        mCalendar.set(Calendar.MINUTE, minute);

                        ((TextView) findViewById(R.id.expense_time)).setText(
                                Constants.TIME_FORMAT.format(mCalendar.getTime()));
                    }
                },
                mCalendar.get(Calendar.HOUR_OF_DAY),
                mCalendar.get(Calendar.MINUTE),
                false);
        timePickerDialog.show();
    }

    private void addExpense() {
        TextView description = (TextView) findViewById(R.id.expense_description_edit_text);
        TextView amount = (TextView) findViewById(R.id.expense_value_text_view);
        TextView comment = (TextView) findViewById(R.id.expense_comment_edit_text);
        Spinner spinner = (Spinner) findViewById(R.id.categories_spinner);

        if (description.getText().toString().equals("")) {
            Toast.makeText(this, getString(R.string.no_description_text), Toast.LENGTH_LONG).show();
        } else {
            Expense expense = new Expense();
            expense.description = description.getText().toString();
            expense.comment = comment.getText().toString();
            expense.amount = Double.parseDouble(amount.getText().toString());
            expense.date = Constants.DATE_FORMAT_FROM_SERVER.format(mCalendar.getTime());
            expense.category = spinner.getSelectedItem().toString().toUpperCase();

            new AddExpenseTask(this, new OnTaskListener() {
                @Override
                public void onSuccess(AsyncTask task, Object result) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", ((Expense) result).toJsonString());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }

                @Override
                public void onFailure(AsyncTask task, Object error) {

                }
            }).execute(expense);
        }


    }

}
