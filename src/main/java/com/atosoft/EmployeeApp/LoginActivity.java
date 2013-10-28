package com.atosoft.EmployeeApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

/**
 * Created by Joe on 9/20/13.
 */
public class LoginActivity extends Activity {

    private String SHAHash;
    public static int NO_OPTIONS = 0;

    private UsersDataSource usersDataSource;
    private Utilities utilities;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setting default screen to login.xml
        setContentView(R.layout.activity_login);

        usersDataSource = new UsersDataSource(this);
//        usersDataSource.open();

//        txtHash = (TextView) findViewById(R.id.textView2);

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = (EditText) findViewById(R.id.txtUsername);
                EditText password = (EditText) findViewById(R.id.txtPassword);

                // get username and password
                String strUsername = username.getText().toString();
                String strPassword = password.getText().toString();
                // Validate login
                // If valid
                if(!strUsername.isEmpty() && !strPassword.isEmpty()) {
//                    strPassword = utilities.computeSHAHash(strPassword);
                    Users user = new Users();
                    user = usersDataSource.getUserCredentials(strUsername, strPassword);

//                    if(!user.equals(null)) {
                    if(user != null) {
                        // TODO
                        // Update User.last_login
                        // Ex.
                        //      Time now - new Time();
                        //      user.last_login = now.setToNow() <---might need to be casted to a Date format
                        finish();
                        Intent i = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(getBaseContext(),
                                "Invalid username/password.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(),
                            "Enter your credentials.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
