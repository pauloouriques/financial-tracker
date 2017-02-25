package com.toptal.app.financialtracker.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.toptal.app.financialtracker.R;
import com.toptal.app.financialtracker.adapters.ExpensesAdapter;
import com.toptal.app.financialtracker.entities.Expense;
import com.toptal.app.financialtracker.login.LoginActivity;
import com.toptal.app.financialtracker.persistence.PrefsHelper;
import com.toptal.app.financialtracker.rest.OnTaskListener;
import com.toptal.app.financialtracker.rest.tasks.GetExpensesTask;

import java.util.ArrayList;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final int ADD_EXPENSE_REQUEST_CODE = 1201;
    private RecyclerView mExpensesRecyclerView;
    private ArrayList<Expense> mExpenses;
    private ExpensesAdapter mExpensesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(
                        new Intent(MainActivity.this, AddExpenseActivity.class),
                        ADD_EXPENSE_REQUEST_CODE);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        TextView name = (TextView) header.findViewById(R.id.user_name_text_view);
        name.setText(PrefsHelper.getUser(this).getName());

        setupExpensesList();
        getExpenses();
        setupFilterField();

        findViewById(R.id.clear_search_field).setOnClickListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            View view = findViewById(R.id.main_search_layout);
            if (view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.GONE);
                ((EditText) findViewById(R.id.main_search_edit_text)).setText("");
                mExpensesAdapter.clearFilter();
            } else {
                view.setVisibility(View.VISIBLE);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_add_expense:
                startActivityForResult(
                        new Intent(MainActivity.this, AddExpenseActivity.class),
                        ADD_EXPENSE_REQUEST_CODE);
                break;
            case R.id.nav_exit:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                PrefsHelper.clearPrefs(this);
                finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear_search_field:
                ((EditText) findViewById(R.id.main_search_edit_text)).setText("");
                mExpensesAdapter.clearFilter();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_EXPENSE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                mExpenses.add(Expense.getFromJson(result));
                mExpensesAdapter.updateList(mExpenses);
            }
        }
    }

    private void setupExpensesList() {
        mExpensesRecyclerView = (RecyclerView) findViewById(R.id.expenses_recycler_view);
        mExpensesRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mExpensesRecyclerView.setNestedScrollingEnabled(false);
        mExpenses = new ArrayList<>();
        mExpensesAdapter = new ExpensesAdapter(this, mExpenses);
        mExpensesRecyclerView.setAdapter(mExpensesAdapter);
    }


    private void getExpenses() {

        new GetExpensesTask(
                this,
                (ProgressBar) findViewById(R.id.main_progress_bar),
                new OnTaskListener() {
            @Override
            public void onSuccess(AsyncTask task, Object result) {
                mExpenses = (ArrayList<Expense>) result;
                mExpensesAdapter.updateList(mExpenses);
            }

            @Override
            public void onFailure(AsyncTask task, Object error) {

            }
        }).execute();
    }

    private void setupFilterField() {
        EditText searchField = (EditText) findViewById(R.id.main_search_edit_text);
        searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mExpensesAdapter.getTextFilter().filter(v.getText().toString());
                    InputMethodManager imm = (InputMethodManager) getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });


        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mExpensesAdapter.getTextFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                final ImageView clear = (ImageView) findViewById(R.id.clear_search_field);
                if (editable.length() > 0) {
                    clear.setVisibility(View.VISIBLE);
                } else {
                    clear.setVisibility(View.GONE);
                }
            }
        });
    }
}
