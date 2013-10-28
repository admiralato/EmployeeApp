package com.atosoft.EmployeeApp;

/**
 * Created by Joe on 10/5/13.
 */

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LazyAdapter extends BaseAdapter implements Filterable {

    private Activity activity;
    private String[] data;
    private Cursor mCursor;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;
    List<Employee> employees, orig, itemDetailsrrayList;;
    private PlaceFilter filter;

    public LazyAdapter(Activity a, String[] d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getApplicationContext());
    }

    public LazyAdapter(Activity a, Cursor cursor) {
        activity = a;
        mCursor = cursor;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getApplicationContext());

        employees = new ArrayList<Employee>();
        orig = employees;
        itemDetailsrrayList = orig;

        filter = new PlaceFilter();

        if(cursor.moveToFirst()) {
            do{
                employees.add(new Employee(
                        Long.parseLong(cursor.getString(cursor.getColumnIndex("_id"))),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("emp_no")),
                        cursor.getString(cursor.getColumnIndex("address")),
                        cursor.getString(cursor.getColumnIndex("birth_date")),
                        cursor.getString(cursor.getColumnIndex("mobile_no")),
                        cursor.getString(cursor.getColumnIndex("work_phone")),
                        cursor.getString(cursor.getColumnIndex("email")),
                        Long.parseLong(cursor.getString(cursor.getColumnIndex("gender_code"))),
                        Long.parseLong(cursor.getString(cursor.getColumnIndex("civil_status_code"))),
                        cursor.getString(cursor.getColumnIndex("hire_date")),
                        cursor.getString(cursor.getColumnIndex("location")),
                        Long.parseLong(cursor.getString(cursor.getColumnIndex("position_id"))),
                        Long.parseLong(cursor.getString(cursor.getColumnIndex("department_id"))),
                        Boolean.getBoolean(cursor.getString(cursor.getColumnIndex("is_admin"))),
                        cursor.getString(cursor.getColumnIndex("photo_path")),
                        Boolean.getBoolean(cursor.getString(cursor.getColumnIndex("is_deleted")))));
            } while (cursor.moveToNext());
        }
    }

    public int getCount() {
        return this.itemDetailsrrayList.size();
    }

    public Object getItem(int position) {
        return itemDetailsrrayList.get(position);
    }

    public long getItemId(int position) {
        return itemDetailsrrayList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(convertView == null)
            vi = inflater.inflate(R.layout.activity_employee_listview, null);

        TextView name = (TextView)vi.findViewById(R.id.name);
        TextView code = (TextView) vi.findViewById(R.id.emp_no);
        ImageView image=(ImageView)vi.findViewById(R.id.photo);

        name.setText(itemDetailsrrayList.get(position).getName());
        code.setText(itemDetailsrrayList.get(position).getCode());

        String avatar = itemDetailsrrayList.get(position).getPhotoPath();
        imageLoader.DisplayImage(avatar, image);
        return vi;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private class PlaceFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults oReturn = new FilterResults();
            ArrayList<Employee> results = new ArrayList<Employee>();
            if (orig == null)
                orig = itemDetailsrrayList;
            if (constraint != null) {
                if (orig != null && orig.size() > 0) {
                    for (Employee g : orig) {
                        if (g.getName().toLowerCase()
                                .contains(constraint.toString().toLowerCase()))
                            results.add(g);
                    }
                }
                oReturn.values = results;
            }
            return oReturn;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            itemDetailsrrayList = (ArrayList<Employee>) results.values;
            notifyDataSetChanged();
        }

    }
}
