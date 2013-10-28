package com.atosoft.EmployeeApp;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Joe on 9/20/13.
 */
public class DatabaseSQLiteHelper extends SQLiteOpenHelper {

    public static String DB_PATH = "/data/data/com.atosoft.EmployeeApp/databases/";
    private static String DB_NAME = "sis.db";

    private SQLiteDatabase myDatabase;

    private final Context myContext;

    public DatabaseSQLiteHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    public void createDatabase() throws IOException {

        boolean dbExist = checkDatabase();

        if (dbExist) {
//            this.openDatabase();
            // do nothing - database already exist
        } else {
            // By calling this method an empty database will be created into
            // the default system path of your application
            // so we are gonna be able to overwrite that
            // database with our database
            //this.getReadableDatabase();
            try {
                copyDatabase();
                //this.getReadableDatabase();
            } catch (IOException e) {
                throw new Error("Error copying database." + e.getMessage());
            }
        }
    }

    private boolean checkDatabase() {
        //SQLiteDatabase checkDB = null;
        boolean fileExists = false;
        //try {
            String myPath = DB_PATH + DB_NAME;
            File file = new File(myPath);
            fileExists = file.exists();

//            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        //} catch (SQLiteException e) {
            //database doesn't exist yet
        //}

//        if(checkDB != null) {
//            checkDB.close();
//        }

//        return checkDB != null ? true : false;
        return fileExists;
    }

    private void copyDatabase() throws IOException {
        openDatabase(); //Added 09.30.2013
        // Open local db as the input stream
        InputStream inputStream = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;
        final File dir = new File(DB_PATH);
        if(!dir.exists()) {
            dir.mkdirs();
        }

        // Open the empty DB as the output stream
        OutputStream outputStream = new FileOutputStream(outFileName);

        //transfer bytes from the input file to the output file
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        // close the streams
        if(myDatabase.isOpen())
            myDatabase.close();
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    public void openDatabase() throws SQLException {
//        if(checkDatabase()) {
            // Open the database
            //String myPath = DB_PATH + DB_NAME;
            //myDatabase = SQLiteDatabase.openOrCreateDatabase(myPath, null);
//        }
        myDatabase = this.getWritableDatabase();
    }

    @Override
    public synchronized void close() {
        if(myDatabase != null)
            myDatabase.close();
        super.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            this.createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
