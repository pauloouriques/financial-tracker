package com.toptal.app.financialtracker.main;

import android.app.Activity;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.toptal.app.financialtracker.R;
import com.toptal.app.financialtracker.adapters.ExpensesAdapter;
import com.toptal.app.financialtracker.entities.Expense;
import com.toptal.app.financialtracker.rest.OnTaskListener;
import com.toptal.app.financialtracker.rest.tasks.GetExpensesTask;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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

        setupExpensesList();
        getExpenses();
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
        if (id == R.id.action_settings) {


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        new GetExpensesTask(this, new OnTaskListener() {
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
}
