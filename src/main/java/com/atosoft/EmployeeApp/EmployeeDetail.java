package com.atosoft.EmployeeApp;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joe on 9/27/13.
 */
public class EmployeeDetail extends FragmentActivity implements ActionBar.TabListener {

    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;
    EmployeeDataSource dataSource;

    static long currentEmployeeID;
    static List<EmployeeEmergencyContact> emergencyContacts;
    static List<EmployeeLatestWages> latestWagesList;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_main_employee);
        try {
            dataSource = new EmployeeDataSource(this);

            mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

            final ActionBar actionBar = getActionBar();
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

            mViewPager = (ViewPager) findViewById(R.id.pager);
            mViewPager.setAdapter(mAppSectionsPagerAdapter);
            mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    // When swiping between different app sections, select the corresponding tab.
                    // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                    // Tab.
                    actionBar.setSelectedNavigationItem(position);
                }
            });

            for (int i= 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
                actionBar.addTab(
                        actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this)
                );
            }

            currentEmployeeID = dataSource.getNextAvailableID("gen_info");
        } catch (Exception error) {
            Log.v("E-201 Error: ", error.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_save:
                LaunchpadSectionFragment genInfoFragment = new LaunchpadSectionFragment();
                List<Employee> values = genInfoFragment.getGenInfoTabValuesList();
                if(values.size() > 0) {
                    if (!dataSource.isUniqueEmployeeCode(values.get(0).getCode())){
                        Toast.makeText(this.getBaseContext(), "Employee code already exists.",
                                Toast.LENGTH_LONG).show();
                        return false;
                    }
                    try {
                        dataSource.insertEmployee(values.get(0));

                        // --- Save other tab information ---
                        for(int x = 0; x < emergencyContacts.size(); x++){
                            dataSource.saveEmergencyContacts(emergencyContacts.get(x));
                        }

                        for(int i = 0; i < latestWagesList.size(); i++) {
                            dataSource.saveLatestWages(latestWagesList.get(i));
                        }
                        // --- Other tabs saved ---

                        this.finish();
                        Toast.makeText(this.getBaseContext(),
                            "New record saved. Opening camera...", Toast.LENGTH_LONG).show();

                        // --- Launch camera ---
                        Intent i = new Intent(getBaseContext(), MakePhotoActivity.class);
                        i.putExtra("EMP_CODE", values.get(0).getCode());
                        startActivity(i);
                    } catch (Exception e) {
                        Log.v("E-201", "Error saving record. " + e.getMessage());
                        return false;
                    }
                }

                return true;
            case R.id.action_discard:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new LaunchpadSectionFragment();

                case 1:
                    return new ContactsFragment();

                case 2:
                    return new LatestWageFragment();

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String[] titles = {
                    "General Info",
                    "Contacts",
                    "Latest Wage"
            };
            return titles[position];
        }
    }

    public static class LaunchpadSectionFragment extends Fragment {
        static View fragmentView;

        public List<Employee> getGenInfoTabValuesList() {
            List<Employee> details = new ArrayList<Employee>();
            EditText code = (EditText) fragmentView.findViewById(R.id.code);
            EditText name = (EditText) fragmentView.findViewById(R.id.name);
            EditText address = (EditText) fragmentView.findViewById(R.id.address);
            EditText birthDate = (EditText) fragmentView.findViewById(R.id.birthdate);
            EditText mobile = (EditText) fragmentView.findViewById(R.id.mobile);
            EditText workPhone = (EditText) fragmentView.findViewById(R.id.workPhone);
            EditText email = (EditText) fragmentView.findViewById(R.id.email);
            Spinner gender = (Spinner) fragmentView.findViewById(R.id.gender_options);
            Spinner civilStatus = (Spinner) fragmentView.findViewById(R.id.civil_status);
            EditText hiredDate = (EditText) fragmentView.findViewById(R.id.hired_date);
            EditText location = (EditText) fragmentView.findViewById(R.id.location);
            Spinner positionId = (Spinner) fragmentView.findViewById(R.id.position);
            Spinner departmentId = (Spinner) fragmentView.findViewById(R.id.department);
            CheckBox isAdmin = (CheckBox) fragmentView.findViewById(R.id.isAdmin);

            String photo_file = "Picture_" + code.getText().toString() + ".jpg";
            details.add(new Employee(
                    currentEmployeeID,
                    name.getText().toString(),
                    code.getText().toString(),
                    address.getText().toString(),
                    birthDate.getText().toString(),
                    mobile.getText().toString(),
                    workPhone.getText().toString(),
                    email.getText().toString(),
                    gender.getSelectedItemId(),
                    civilStatus.getSelectedItemId(),
                    hiredDate.getText().toString(),
                    location.getText().toString(),
                    positionId.getSelectedItemId(),
                    departmentId.getSelectedItemId(),
                    isAdmin.isChecked(),
                    photo_file,
                    false
            ));

            return details;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_launchpad, container, false);
            this.fragmentView = rootView;

            Spinner spinner = (Spinner) rootView.findViewById(R.id.gender_options);
            Spinner civilStatusSpinner = (Spinner) rootView.findViewById(R.id.civil_status);
            Spinner positionSpinner = (Spinner) rootView.findViewById(R.id.position);
            Spinner departmentSpinner = (Spinner) rootView.findViewById(R.id.department);

            EmployeeDataSource datasource = new EmployeeDataSource(getActivity());
            datasource.open();

            //--- Gender Spinner ---
            List<Gender> list = datasource.getGenderLists();
            ArrayAdapter<Gender> adapter = new ArrayAdapter<Gender>(getActivity(),
                    android.R.layout.simple_spinner_item, list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            //--- Civil Status Spinner ---
            List<CivilStatus> civilStatusList = datasource.getCivilStatusList();
            ArrayAdapter<CivilStatus> civilStatusAdapter = new ArrayAdapter<CivilStatus>(getActivity(),
                    android.R.layout.simple_spinner_item, civilStatusList);
            civilStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            civilStatusSpinner.setAdapter(civilStatusAdapter);

            civilStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //--- Position ---
            List<Position> positions = datasource.getPositionList();
            ArrayAdapter<Position> positionArrayAdapter = new ArrayAdapter<Position>(getActivity(),
                    android.R.layout.simple_spinner_item, positions);
            positionArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            positionSpinner.setAdapter(positionArrayAdapter);

            positionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //--- Department Spinner ---
            List<Department> departments = datasource.getDepartmentList();
            ArrayAdapter<Department> departmentAdapter = new ArrayAdapter<Department>(getActivity(),
                    android.R.layout.simple_spinner_item, departments);
            departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            departmentSpinner.setAdapter(departmentAdapter);

            departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            datasource.close();
            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

        }
    }

    public static class LatestWageFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_latest_wage,
                    container, false);
            latestWagesList = new ArrayList<EmployeeLatestWages>();

            Button btnAddRate = (Button)rootView.findViewById(R.id.btnAddRate);
            final TableLayout tableLayout = (TableLayout) rootView.findViewById(R.id.latestWageTableLayout);
            final EditText txtLatestWageDate = (EditText) rootView.findViewById(R.id.latestWageDate);
            final EditText txtLatestWageNote = (EditText) rootView.findViewById(R.id.latestWageNote);
            final EditText txtLatestWageRate  = (EditText) rootView.findViewById(R.id.latestWageRate);

            final EmployeeDataSource datasource = new EmployeeDataSource(getActivity());
            datasource.open();

            btnAddRate.setOnClickListener(new View.OnClickListener() {
                int rowId = 0;
                long latestWageListCount;

                @Override
                public void onClick(View v) {
                    // --- Validation ---
                    if(txtLatestWageDate.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Enter date", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if(txtLatestWageNote.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Enter note", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if(txtLatestWageRate.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Enter rate", Toast.LENGTH_LONG).show();
                        return;
                    }
                    // --- End validation ---

                    TableRow tableRow = new TableRow(getActivity().getApplicationContext());
                    tableRow.setId(rowId++);
                    tableRow.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));

                    TextView tvLatestWageDate = new TextView(getActivity().getApplicationContext());
                    tvLatestWageDate.setText(txtLatestWageDate.getText().toString());
                    tvLatestWageDate.setTextColor(Color.WHITE);
                    tvLatestWageDate.setBackgroundColor(Color.BLACK);
                    tvLatestWageDate.setPadding(0, 2, 10, 2);
                    tableRow.addView(tvLatestWageDate);

                    TextView tvLatestWageRate = new TextView(getActivity().getApplicationContext());
                    tvLatestWageRate.setText(txtLatestWageRate.getText().toString());
                    tvLatestWageRate.setTextColor(Color.WHITE);
                    tvLatestWageRate.setBackgroundColor(Color.BLACK);
                    tvLatestWageRate.setPadding(0, 2, 10, 2);
                    tableRow.addView(tvLatestWageRate);

                    tableLayout.addView(tableRow, new TableLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    tableLayout.invalidate();
                    tableLayout.refreshDrawableState();

                    if(latestWagesList.size() > 0) {
                        latestWageListCount = latestWagesList.size() + 1;
                    } else {
                        latestWageListCount = datasource.getNextAvailableID("latest_wage") + 1;
                    }

                    latestWagesList.add(new EmployeeLatestWages(
                            latestWageListCount,
                            currentEmployeeID,
                            txtLatestWageNote.getText().toString(),
                            txtLatestWageDate.getText().toString(),
                            Float.parseFloat(txtLatestWageRate.getText().toString())));

                    txtLatestWageNote.setText("");
                    txtLatestWageDate.setText("");
                    txtLatestWageRate.setText("");
                }
            });

            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }
    }

    public static class ContactsFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_contacts,
                    container, false);

            emergencyContacts = new ArrayList<EmployeeEmergencyContact>();

            Spinner phoneTypeSpinner = (Spinner) rootView.findViewById(R.id.contactPhoneTypeSpinner);
            Button contactButton = (Button) rootView.findViewById(R.id.addContact);
            final TableLayout tableLayout = (TableLayout) rootView.findViewById(R.id.contactsTableLayout);
            final EditText txtContactName = (EditText) rootView.findViewById(R.id.contactName);
            final EditText txtContactRelationship = (EditText) rootView.findViewById(R.id.contactRelationship);
            final EditText txtContactAddress = (EditText) rootView.findViewById(R.id.contactAddress);
            final EditText txtContactPhoneNo = (EditText) rootView.findViewById(R.id.contactPhoneNo);
            final EditText txtContactNotes = (EditText) rootView.findViewById(R.id.contactSpecialNotes);
            final Spinner contactPhoneTypeSpinner = (Spinner) rootView.findViewById(R.id.contactPhoneTypeSpinner);

            final EmployeeDataSource datasource = new EmployeeDataSource(getActivity());
            datasource.open();

            //--- Gender Spinner ---
            List<PhoneType> list = datasource.getPhoneTypeList();
            ArrayAdapter<PhoneType> adapter = new ArrayAdapter<PhoneType>(getActivity(),
                    android.R.layout.simple_spinner_item, list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            phoneTypeSpinner.setAdapter(adapter);

            phoneTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            contactButton.setOnClickListener(new View.OnClickListener() {
                int rowId = 0;
                long contactListCount;

                @Override
                public void onClick(View v) {

                    // --- Validation ---
                    if(txtContactName.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Enter contact name", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if(txtContactAddress.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Enter contact address", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if(txtContactRelationship.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Enter relationship with contact", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if(txtContactPhoneNo.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Enter contact phone number", Toast.LENGTH_LONG).show();
                        return;
                    }
                    // --- End validation ---

                    TableRow tableRow = new TableRow(getActivity().getApplicationContext());
                    tableRow.setId(rowId++);
                    tableRow.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));

                    TextView tvContactName = new TextView(getActivity().getApplicationContext());
                    tvContactName.setText(txtContactName.getText().toString());
                    tvContactName.setTextColor(Color.WHITE);
                    tvContactName.setBackgroundColor(Color.BLACK);
                    tvContactName.setPadding(0, 2, 10, 2);
                    tableRow.addView(tvContactName);

                    TextView tvRelationship = new TextView(getActivity().getApplicationContext());
                    tvRelationship.setText(txtContactRelationship.getText().toString());
                    tvRelationship.setTextColor(Color.WHITE);
                    tvRelationship.setBackgroundColor(Color.BLACK);
                    tvRelationship.setPadding(0, 2, 10, 2);
                    tableRow.addView(tvRelationship);

                    TextView tvContactNo = new TextView(getActivity().getApplicationContext());
                    tvContactNo.setText(txtContactPhoneNo.getText().toString());
                    tvContactNo.setTextColor(Color.WHITE);
                    tvContactNo.setBackgroundColor(Color.BLACK);
                    tvContactNo.setPadding(0, 2, 10, 2);
                    tableRow.addView(tvContactNo);

                    tableLayout.addView(tableRow, new TableLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    tableLayout.invalidate();
                    tableLayout.refreshDrawableState();

                    if(emergencyContacts.size() > 0) {
                        contactListCount = emergencyContacts.size() + 1;
                    } else {
                        contactListCount = datasource.getNextAvailableID("emergency_contact") + 1;
                    }

                    emergencyContacts.add(new EmployeeEmergencyContact(
                            contactListCount,
                            currentEmployeeID,
                            txtContactName.getText().toString(),
                            txtContactRelationship.getText().toString(),
                            txtContactAddress.getText().toString(),
                            contactPhoneTypeSpinner.getSelectedItemId(),
                            txtContactNotes.getText().toString(),
                            txtContactPhoneNo.getText().toString()));

                    txtContactName.setText("");
                    txtContactAddress.setText("");
                    txtContactRelationship.setText("");
                    txtContactPhoneNo.setText("");

                }
            });
            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }
    }

}
