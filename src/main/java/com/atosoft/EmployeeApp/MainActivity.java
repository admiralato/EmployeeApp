package com.atosoft.EmployeeApp;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity implements SearchView.OnQueryTextListener {
    private EmployeeDataSource datasource;
    public ListView listView;
    public static LazyAdapter adapter;
    public Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datasource = new EmployeeDataSource(this);
        datasource.open();

        listView = (ListView) findViewById(R.id.employeeListView);
        mHandler = new Handler();

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                adapter = new LazyAdapter(MainActivity.this, datasource.getAllEmployees());
                listView.setAdapter(adapter);
            }
        });

        // Get the intent, verify the action and get the query
        handleIntent(getIntent());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("E-201", "clicked on item: " + position);
                // TODO Eliminate too much querying, get values from adapter

                //Employee employee = datasource.getEmployee();
                Employee employee = (Employee) adapter.getItem(position);
                employee.setLatestWageList(datasource.getLatestWageList(employee.getId()));
                employee.setEmergencyContactList(datasource.getEmergencyContactsList(employee.getId()));

                employee.setGenderString(datasource.getObjGender(employee.getGender()).toString());
                employee.setCivilStatusString(datasource.getObjCivilStatus(employee.getCivilStatusCode()).toString());
                employee.setPositionString(datasource.getObjPosition(employee.getPositionId()).toString());
                employee.setDepartmentString(datasource.getObjDepartment(employee.getDepartmentId()).toString());

                ArrayList<Employee> employeeList = new ArrayList<Employee>();
                employeeList.add(employee);

                // Pass the employee object to the EmployeeProfileActivity
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("profile", employeeList);

                intent.putExtras(bundle);
                intent.setClass(getApplicationContext(), EmployeeProfileActivity.class);
                startActivity(intent);

            }
        });
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    private void doMySearch(String queryString) {

    }

    @Override
    public void onResume(){
        super.onResume();
        adapter = new LazyAdapter(MainActivity.this, datasource.getAllEmployees());
        listView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        final Menu mainMenu = menu;

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem menuItem = (MenuItem) menu.findItem(R.id.action_search);
        // Assumes current activity is the searchable activity
        if(searchView != null)
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hide action item
                if (mainMenu != null) {
                    mainMenu.findItem(R.id.action_create).setVisible(false);
                }

            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                // re-show the action button
                if (mainMenu != null) {
                    mainMenu.findItem(R.id.action_create).setVisible(true);
                }
                return false;

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    adapter.getFilter().filter("");
                    Log.i("E-201", "onQueryTextChange Empty String");
                    listView.clearTextFilter();
                } else {
                    Log.i("E-201", "onQueryTextChange " + newText.toString());
                    adapter.getFilter().filter(newText.toString());
                }
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Toast.makeText(this, "Menu Item 1 selected", Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.action_create:
                Intent i = new Intent(getBaseContext(), EmployeeDetail.class);
                startActivity(i);
                break;

            default:
                break;
        }

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            adapter.getFilter().filter("");
            Log.i("E-201", "onQueryTextChange Empty String");
            listView.clearTextFilter();
        } else {
            Log.i("E-201", "onQueryTextChange " + newText.toString());
            adapter.getFilter().filter(newText.toString());
        }
        return true;
    }
}
