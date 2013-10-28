package com.atosoft.EmployeeApp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Joe on 9/20/13.
 */
public class UsersDataSource {

    // Database fields
    private SQLiteDatabase database;
    private DatabaseSQLiteHelper dbHelper;
    private String[] allColumns = { "_id", "username", "password", "last_login" };

    private Utilities mUtilities;

    public UsersDataSource(Context context) {
        dbHelper = new DatabaseSQLiteHelper(context);
        try {
            dbHelper.createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        dbHelper.openDatabase();
        mUtilities = new Utilities();
    }

    public void open() throws SQLException {
//        if(!database.isOpen())
          database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Cursor getAllUsers() {
        return database.query("users", allColumns, null, null, null, null, null);
    }

    public List<Users> getUsersList() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
        Date convertedDate = new Date();

        List<Users> usersList = new ArrayList<Users>();

        Cursor cursor = database.query("users", allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Users user = new Users();
            user.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex("_id"))));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            String last_login = cursor.getString(cursor.getColumnIndex("last_login"));
            if(!last_login.isEmpty()) {
                try {
                    convertedDate =
                            dateFormat.parse(cursor.getString(cursor.getColumnIndex("last_login")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                user.setLast_login(convertedDate);
            }

            usersList.add(user);
            cursor.moveToNext();
        }

        cursor.close();
        database.close();
        return usersList;
    }

    public Cursor getUser(long rowId) throws SQLException {
        this.open();
        Cursor mCursor =
                database.query(true, "users", allColumns, "_id = " + rowId,
                        null, null, null, null, null);

        if(mCursor != null) {
            mCursor.moveToFirst();
        }
        database.close();
        return mCursor;
    }

    /*  Returns a User object
        params:
            username - User username
            password - User password
    * */
     public Users getUserCredentials(String username, String password) {
        password = mUtilities.computeSHAHash(password);
        password = password.replaceAll("(\\r|\\n)", "");

//        String whereCols = "username = ? AND password = ?";
        String sql = "SELECT _id, username, password, last_login FROM users WHERE username = '" +
                username + "' AND password = '" + password + "'";
//        String[] whereArgs = new String[] { username, password };

        this.open();
        Cursor cursor =
                database.rawQuery(sql, null);

        Users user = new Users();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
        Date convertedDate = new Date();

        if(cursor != null && cursor.getCount() > 0) {
            try {
                cursor.moveToFirst();
                user.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex("_id"))));
                user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                String last_login = cursor.getString(cursor.getColumnIndex("last_login"));
                if(!last_login.isEmpty()) {
                    convertedDate =
                            dateFormat.parse(cursor.getString(cursor.getColumnIndex("last_login")));
                    user.setLast_login(convertedDate);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            return null;
        }
        database.close();

        return user;
    }

    public long insertUser(String username, String password, Date last_login) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", mUtilities.computeSHAHash(password));
        values.put("last_login", last_login.toString());
        return database.insert("users", null, values);
    }

    public boolean deleteUser(long rowId) {
        return database.delete("users", "_id = " + rowId, null) > 0;
    }

    public boolean updateUser(long rowId, String username, String password, Date last_login) {
        ContentValues args = new ContentValues();
        args.put("username", username);
        args.put("password", mUtilities.computeSHAHash(password));
        args.put("last_login", last_login.toString());
        return database.update("users", args, "_id = " + rowId, null) > 0;
    }
}
