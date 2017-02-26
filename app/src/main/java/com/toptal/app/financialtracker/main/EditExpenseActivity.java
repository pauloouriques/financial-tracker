package com.toptal.app.financialtracker.main;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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
import com.toptal.app.financialtracker.rest.tasks.DeleteExpenseTask;
import com.toptal.app.financialtracker.rest.tasks.EditExpenseTask;
import com.toptal.app.financialtracker.utils.Constants;
import com.toptal.app.financialtracker.utils.SharedMethods;
import com.toptal.app.financialtracker.watchers.NumberTextWatcher;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * This class represents the add expense Activity.
 */

public class EditExpenseActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Calendar mCalendar;
    private Expense mExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        Spinner spinner = (Spinner) findViewById(R.id.categories_spinner);
        spinner.setOnItemSelectedListener(this);

        EditText valueEditText = (EditText) findViewById(R.id.expense_value_text_view);
        valueEditText.addTextChangedListener(new NumberTextWatcher(valueEditText));

        mCalendar = new GregorianCalendar();

        findViewById(R.id.add_expense_button).setOnClickListener(this);
        findViewById(R.id.expense_date_button).setOnClickListener(this);
        findViewById(R.id.remove_expense_button).setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mExpense = Expense.getFromJson(bundle.getString(Constants.EXPENSE_TO_EDIT));
            try {
                mCalendar.setTime(Constants.DATE_FORMAT_FROM_SERVER.parse(mExpense.date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            fillExpenseFields();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_expense_button:
                editExpense();
                break;
            case R.id.expense_date_button:
                showDatePicker();
                break;
            case R.id.remove_expense_button:
                SharedMethods.showDialog(
                        this,
                        getString(R.string.delete_expense_dialog_title),
                        getString(R.string.delete_expense_dialog_text),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.cancel();
                                deleteExpense();
                            }
                        });
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
     * Fill the expense info.
     */
    private void fillExpenseFields() {
        ((TextView) findViewById(R.id.expense_date)).setText(
                Constants.DATE_FORMAT.format(mCalendar.getTime()));
        ((TextView) findViewById(R.id.expense_time)).setText(
                Constants.TIME_FORMAT.format(mCalendar.getTime()));

        EditText valueEditText = (EditText) findViewById(R.id.expense_value_text_view);
        valueEditText.setText(SharedMethods.formatMonetaryValue(mExpense.amount));

        Spinner spinner = (Spinner) findViewById(R.id.categories_spinner);
        spinner.setSelection(mExpense.getCategoryIndex(this));

        TextView description = (TextView) findViewById(R.id.expense_description_edit_text);
        description.setText(mExpense.description);

        TextView comment = (TextView) findViewById(R.id.expense_comment_edit_text);
        comment.setText(mExpense.comment);
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

    private void deleteExpense() {
        new DeleteExpenseTask(this, new OnTaskListener() {
            @Override
            public void onSuccess(AsyncTask task, Object result) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", ((Expense) result).toJsonString());
                returnIntent.putExtra("deleted", true);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }

            @Override
            public void onFailure(AsyncTask task, Object error) {

            }
        }).execute(mExpense);
    }

    private void editExpense() {
        TextView description = (TextView) findViewById(R.id.expense_description_edit_text);
        TextView amount = (TextView) findViewById(R.id.expense_value_text_view);
        TextView comment = (TextView) findViewById(R.id.expense_comment_edit_text);
        Spinner spinner = (Spinner) findViewById(R.id.categories_spinner);

        if (description.getText().toString().equals("")) {
            Toast.makeText(this, getString(R.string.no_description_text), Toast.LENGTH_LONG).show();
        } else {
            mExpense.description = description.getText().toString();
            mExpense.comment = comment.getText().toString();
            mExpense.amount = Double.parseDouble(amount.getText().toString());
            mExpense.date = Constants.DATE_FORMAT_FROM_SERVER.format(mCalendar.getTime());
            mExpense.category = spinner.getSelectedItem().toString().toUpperCase();

            new EditExpenseTask(this, new OnTaskListener() {
                @Override
                public void onSuccess(AsyncTask task, Object result) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", ((Expense) result).toJsonString());
                    returnIntent.putExtra("deleted", false);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }

                @Override
                public void onFailure(AsyncTask task, Object error) {

                }
            }).execute(mExpense);
        }
    }

}
