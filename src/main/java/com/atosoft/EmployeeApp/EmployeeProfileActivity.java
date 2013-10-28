package com.atosoft.EmployeeApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class EmployeeProfileActivity extends Activity {
    private Employee employee;
    private ArrayList<Employee> employeeList;

    EmployeeDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        employee = new Employee();
        dataSource = new EmployeeDataSource(this);

        // Get Employee object from MainActivity
        ArrayList<Employee> list = getIntent().getParcelableArrayListExtra("profile");
        employeeList = list;
        employee = list.get(0);

        if(employee != null) {
            // TODO Returned data is messy
            TextView txtProfileName = (TextView) findViewById(R.id.profile_name);
            txtProfileName.setText(employee.getName());

            TextView txtProfileAddress = (TextView) findViewById(R.id.profile_address);
            txtProfileAddress.setText(employee.getAddress());

            TextView txtProfileBirthDate = (TextView) findViewById(R.id.profile_birthdate);
            txtProfileBirthDate.setText(employee.getBirthdate());

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    ImageView profilePic = (ImageView) findViewById(R.id.profile_photo);
                    ImageLoader imageLoader = new ImageLoader(getApplicationContext());
                    imageLoader.DisplayImage(employee.getPhotoPath(), profilePic);
                }
            });

            TextView txtProfileMobile = (TextView) findViewById(R.id.profile_mobile);
            txtProfileMobile.setText(employee.getMobileno());
            txtProfileMobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView txt = (TextView) v.findViewById(R.id.profile_mobile);
                    if(txt.getText().toString() != null) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + txt.getText().toString()));
                        startActivity(intent);
                    }
                }
            });

            TextView txtProfileWork = (TextView) findViewById(R.id.profile_work);
            txtProfileWork.setText(employee.getWorkphone());
            txtProfileWork.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView txt = (TextView) v.findViewById(R.id.profile_work);
                    if(txt.getText().toString() != null) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + txt.getText().toString()));
                        startActivity(intent);
                    }
                }
            });

            TextView txtProfileEmail = (TextView) findViewById(R.id.profile_email);
            txtProfileEmail.setText(employee.getEmail());

            TextView txtProfileGender = (TextView) findViewById(R.id.profile_gender);
            txtProfileGender.setText(employee.getGenderString());

            TextView txtProfileCivilStatus = (TextView) findViewById(R.id.profile_civilStatus);
            txtProfileCivilStatus.setText(employee.getCivilStatusString());

            TextView txtProfileDateHired = (TextView) findViewById(R.id.profile_hired_date);
            txtProfileDateHired.setText(employee.getHiredDate());

            TextView txtProfileLocation = (TextView) findViewById(R.id.profile_location);
            txtProfileLocation.setText(employee.getLocation());

            TextView txtProfilePosition = (TextView) findViewById(R.id.profile_position);
            txtProfilePosition.setText(employee.getPositionString());

            TextView txtProfileDepartment = (TextView) findViewById(R.id.profile_department);
            txtProfileDepartment.setText(employee.getDepartmentString());

            TableLayout tableLayoutContacts = (TableLayout) findViewById(R.id.profile_emergency_contacts_table);
            TableLayout tableLayoutWages = (TableLayout) findViewById(R.id.profile_wages_history_table);

            List<EmployeeEmergencyContact> contacts = employee.getEmergencyContactList();
            if(contacts != null) {
                for(int x = 0; x < contacts.size(); x++) {
                    TableRow rowContacts = new TableRow(this);
                    rowContacts.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.FILL_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));

                    rowContacts.setId(x);
                    TextView tvContactName = new TextView(this);
                    tvContactName.setText(contacts.get(x).getContactName());
                    tvContactName.setTextColor(Color.WHITE);
                    tvContactName.setBackgroundColor(Color.BLACK);
                    tvContactName.setPadding(0, 2, 10, 2);
                    rowContacts.addView(tvContactName);

                    TextView tvRelationship = new TextView(this);
                    tvRelationship.setText(contacts.get(x).getRelationship());
                    tvRelationship.setTextColor(Color.WHITE);
                    tvRelationship.setBackgroundColor(Color.BLACK);
                    tvRelationship.setPadding(0, 2, 10, 2);
                    rowContacts.addView(tvRelationship);

                    TextView tvContactNo = new TextView(this);
                    tvContactNo.setText(contacts.get(x).getContact_no());
                    tvContactNo.setTextColor(Color.WHITE);
                    tvContactNo.setBackgroundColor(Color.BLACK);
                    tvContactNo.setPadding(0, 2, 10, 2);
                    rowContacts.addView(tvContactNo);

                    tableLayoutContacts.addView(rowContacts, new TableLayout.LayoutParams(
                            ViewGroup.LayoutParams.FILL_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                }
            }

            List<EmployeeLatestWages> latestWageList = employee.getLatestWageList();
            if(latestWageList != null) {
                for(int x = 0; x < latestWageList.size(); x++) {
                    TableRow rowWages = new TableRow(this);
                    rowWages.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.FILL_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));

                    rowWages.setId(x);
                    TextView tvNote = new TextView(this);
                    tvNote.setText(latestWageList.get(x).getNote());
                    tvNote.setTextColor(Color.WHITE);
                    tvNote.setBackgroundColor(Color.BLACK);
                    tvNote.setPadding(0, 2, 10, 2);
                    rowWages.addView(tvNote);

                    TextView tvLatestWageDate = new TextView(this);
                    tvLatestWageDate.setText(latestWageList.get(x).getDate().toString());
                    tvLatestWageDate.setTextColor(Color.WHITE);
                    tvLatestWageDate.setBackgroundColor(Color.BLACK);
                    tvLatestWageDate.setPadding(0, 2, 10, 2);
                    rowWages.addView(tvLatestWageDate);

                    TextView tvRate = new TextView(this);
                    tvRate.setText(Float.toString(latestWageList.get(x).getRate()));
                    tvRate.setTextColor(Color.WHITE);
                    tvRate.setBackgroundColor(Color.BLACK);
                    tvRate.setPadding(0, 2, 10, 2);
                    rowWages.addView(tvRate);

                    tableLayoutWages.addView(rowWages, new TableLayout.LayoutParams(
                            ViewGroup.LayoutParams.FILL_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.employee_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_update:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("updateEmployee", employeeList);

                intent.putExtras(bundle);
                intent.setClass(getApplicationContext(), EmployeeUpdateView.class);
                startActivity(intent);

                this.finish();
                return true;
            case R.id.action_delete:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Delete");
                alertDialogBuilder
                        .setMessage("Delete employee?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity
                                if (dataSource.deleteEmployee(employee.getId())) {
                                    Toast.makeText(EmployeeProfileActivity.this.getBaseContext(),
                                            "Employee deleted", Toast.LENGTH_LONG).show();
                                    EmployeeProfileActivity.this.finish();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
}
