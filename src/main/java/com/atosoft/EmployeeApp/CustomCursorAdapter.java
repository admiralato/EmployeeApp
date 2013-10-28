package com.atosoft.EmployeeApp;

/**
 * Created by Joe on 10/5/13.
 */

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class CustomCursorAdapter extends CursorAdapter {
    ImageLoader imageLoader;

    public CustomCursorAdapter(Context context, Cursor c) {
        super(context, c);
//        imageLoader = new ImageLoader(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // when the view will be created for first time,
        // we need to tell the adapters, how each item will look
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View retView = inflater.inflate(R.layout.activity_employee_listview, parent, false);

        return retView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // here we are setting our data
        // that means, take the data from the cursor and put it in views

        ImageView profilePic = (ImageView) view.findViewById(R.id.photo);

        TextView tvName = (TextView) view.findViewById(R.id.name);
        tvName.setText(cursor.getString(cursor.getColumnIndex("name")));

        TextView tvCode = (TextView) view.findViewById(R.id.emp_no);
        tvCode.setText(cursor.getString(cursor.getColumnIndex("emp_no")));

        String avatar = cursor.getString(cursor.getColumnIndex("photo_path"));
        File file = new File(Utilities.getDir() + "/" + avatar);

        file.setReadable(true);
        Uri uri = Uri.fromFile(file);
        profilePic.setImageURI(uri);
    }
}
