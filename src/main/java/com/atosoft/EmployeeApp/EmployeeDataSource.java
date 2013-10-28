package com.atosoft.EmployeeApp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joe on 9/23/13.
 */
public class EmployeeDataSource {

    private SQLiteDatabase database;
    private DatabaseSQLiteHelper dbHelper;
    // TODO add more fields here
    private String[] allColumns = {
            "_id",
            "is_admin",
            "emp_no",
            "name",
            "address",
            "birth_date",
            "mobile_no",
            "work_phone",
            "email",
            "gender_code",
            "civil_status_code",
            "hire_date",
            "location",
            "position_id",
            "department_id",
            "photo_path",
            "is_deleted"
    };

    private Utilities mUtilities;
    private long currentID;

    public EmployeeDataSource(Context context) {
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
        if(database == null || !database.isOpen())
            database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Cursor getAllEmployees() {
        if(!database.isOpen()) this.open();
        // Retrieve all active (is_deleted != 1) employees
        return database.query("gen_info", allColumns, "is_deleted != 1", null, null, null, null);
    }

    public List<Employee> getEmployeesList() {

        List<Employee> employeeList = new ArrayList<Employee>();
        if(!database.isOpen()) this.open();

        Cursor cursor = database.query("gen_info", allColumns, "is_deleted != 1", null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Employee employee = new Employee();
            employee.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex("_id"))));
            employee.setCode(cursor.getString(cursor.getColumnIndex("emp_no")));
            employee.setName(cursor.getString(cursor.getColumnIndex("name")));
            employee.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            employee.setCivilStatusCode(Integer.parseInt(cursor
                    .getString(cursor.getColumnIndex("civil_status_code"))));
            employeeList.add(employee);
            cursor.moveToNext();
        }

        cursor.close();
        database.close();
        return employeeList;
    }

    public Employee getEmployee(long rowId) throws SQLException {

        if(!database.isOpen()) this.open();
        Cursor mCursor =
                database.query(true, "gen_info", allColumns, "_id = " + rowId,
                        null, null, null, null, null);

        if(mCursor != null) {
            mCursor.moveToFirst();
        }
        Employee employee = new Employee();
        do {
            employee.setId(Long.parseLong(mCursor.getString(mCursor.getColumnIndex("_id"))));
            employee.setIsAdmin(Integer.parseInt(
                    mCursor.getString(mCursor.getColumnIndex("is_admin"))) == 1 ? true : false);
            employee.setCode(mCursor.getString(mCursor.getColumnIndex("emp_no")));
            employee.setName(mCursor.getString(mCursor.getColumnIndex("name")));
            employee.setAddress(mCursor.getString(mCursor.getColumnIndex("address")));
            employee.setBirthDate(mCursor.getString(mCursor.getColumnIndex("birth_date")));
            employee.setMobileno(mCursor.getString(mCursor.getColumnIndex("mobile_no")));
            employee.setWorkPhone(mCursor.getString(mCursor.getColumnIndex("work_phone")));
            employee.setEmail(mCursor.getString(mCursor.getColumnIndex("email")));
            employee.setGender(mCursor.getLong(mCursor.getColumnIndex("gender_code")));
            employee.setCivilStatusCode(mCursor.getLong(mCursor.getColumnIndex("civil_status_code")));
            employee.setHiredDate(mCursor.getString(mCursor.getColumnIndex("hire_date")));
            employee.setLocation(mCursor.getString(mCursor.getColumnIndex("location")));
            employee.setPositionId(mCursor.getLong(mCursor.getColumnIndex("position_id")));
            employee.setDepartmentId(mCursor.getLong(mCursor.getColumnIndex("department_id")));
            employee.setPhotoPath(mCursor.getString(mCursor.getColumnIndex("photo_path")));
            employee.setDeleted(Integer.parseInt(
                    mCursor.getString(mCursor.getColumnIndex("is_deleted"))) == 1 ? true : false);
        } while (mCursor.moveToNext());

        mCursor.close();
        database.close();

        return employee;
    }

    public Gender getObjGender(long id) {
        if(!database.isOpen()) this.open();
        Cursor mCursor =
                database.query(true, "gender_map", new String[] { "_id", "code", "description",},
                        "_id = " + id, null, null, null, null, null);
        if(mCursor.moveToFirst()) {
            return new Gender(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex("_id"))),
                    mCursor.getString(mCursor.getColumnIndex("code")),
                    mCursor.getString(mCursor.getColumnIndex("description")));
        } else {
            return null;
        }
    }

    public List<Gender> getGenderLists() {
        List<Gender> genders = new ArrayList<Gender>();

        if(!database.isOpen()) this.open();

        Cursor cursor = database.query("gender_map", new String[] { "_id", "code", "description" },
                null, null, null, null, null);
        if ( cursor.moveToFirst () ) {
            do {
                genders.add (new Gender( Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))),
                        cursor.getString(cursor.getColumnIndex("code")),
                        cursor.getString(cursor.getColumnIndex("description"))));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        database.close();

        return genders;
    }

    public CivilStatus getObjCivilStatus(long id) {
        if(!database.isOpen()) this.open();
        Cursor mCursor =
                database.query(true, "civil_status_map", new String[] { "_id", "code", "description",},
                        "_id = " + id, null, null, null, null, null);
        if(mCursor.moveToFirst()) {
            return new CivilStatus(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex("_id"))),
                    mCursor.getString(mCursor.getColumnIndex("code")),
                    mCursor.getString(mCursor.getColumnIndex("description")));
        } else {
            return null;
        }
    }

    public List<CivilStatus> getCivilStatusList() {
        List<CivilStatus> civilStatuses = new ArrayList<CivilStatus>();

        if(!database.isOpen()) this.open();

        Cursor cursor = database.query("civil_status_map", new String[] { "_id", "code", "description" },
                null, null, null, null, null);

        if ( cursor.moveToFirst () ) {
            do {
                civilStatuses.add (new CivilStatus( Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))),
                        cursor.getString(cursor.getColumnIndex("code")),
                        cursor.getString(cursor.getColumnIndex("description"))));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        database.close();

        return civilStatuses;
    }

    public Position getObjPosition(long id) {
        if(!database.isOpen()) this.open();
        Cursor mCursor =
                database.query(true, "position", new String[] { "_id", "code", "description",},
                        "_id = " + id, null, null, null, null, null);
        if(mCursor.moveToFirst()) {
            return new Position(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex("_id"))),
                    mCursor.getString(mCursor.getColumnIndex("code")),
                    mCursor.getString(mCursor.getColumnIndex("description")));
        } else {
            return null;
        }
    }

    public List<Position> getPositionList() {
        List<Position> positions = new ArrayList<Position>();

        if(!database.isOpen()) this.open();

        Cursor cursor = database.query("position", new String[] { "_id", "code", "description" },
                null, null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                positions.add(new Position(Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))),
                        cursor.getString(cursor.getColumnIndex("code")),
                        cursor.getString(cursor.getColumnIndex("description"))));
            } while(cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return positions;
    }

    public Department getObjDepartment(long id) {
        if(!database.isOpen()) this.open();
        Cursor mCursor =
                database.query(true, "department", new String[] { "_id", "code", "description",},
                        "_id = " + id, null, null, null, null, null);
        if(mCursor.moveToFirst()) {
            return new Department(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex("_id"))),
                    mCursor.getString(mCursor.getColumnIndex("code")),
                    mCursor.getString(mCursor.getColumnIndex("description")));
        } else {
            return null;
        }
    }

    public List<Department> getDepartmentList() {
        List<Department> departments = new ArrayList<Department>();

        if(!database.isOpen()) this.open();

        Cursor cursor = database.query("department", new String[] { "_id", "code", "description" },
                null, null, null, null, null);

        if ( cursor.moveToFirst () ) {
            do {
                departments.add (new Department( Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))),
                        cursor.getString(cursor.getColumnIndex("code")),
                        cursor.getString(cursor.getColumnIndex("description"))));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        database.close();

        return departments;
    }

    public List<PhoneType> getPhoneTypeList() {
        List<PhoneType> phoneTypes = new ArrayList<PhoneType>();

        if(!database.isOpen()) this.open();

        Cursor cursor = database.query("phone_type", new String[] { "_id", "code", "description" },
                null, null, null, null, null);

        if ( cursor.moveToFirst () ) {
            do {
                phoneTypes.add (new PhoneType(Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))),
                        cursor.getString(cursor.getColumnIndex("code")),
                        cursor.getString(cursor.getColumnIndex("description"))));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        database.close();

        return phoneTypes;
    }

    public List<EmployeeLatestWages> getLatestWageList(long genInfoId) {
        List<EmployeeLatestWages> latestWages = new ArrayList<EmployeeLatestWages>();

        if(!database.isOpen()) this.open();
        Cursor cursor = database.query("latest_wage", new String[] { "_id", "gen_info_id", "note", "date", "rate" },
                "gen_info_id = " + genInfoId, null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                latestWages.add(new EmployeeLatestWages( Long.parseLong(cursor.getString(cursor.getColumnIndex("_id"))),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex("gen_info_id"))),
                        cursor.getString(cursor.getColumnIndex("note")),
                        cursor.getString(cursor.getColumnIndex("date")),
                        cursor.getFloat(cursor.getColumnIndex("rate"))));
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return latestWages;
    }

    public List<EmployeeEmergencyContact> getEmergencyContactsList(long genInfoId) {
        List<EmployeeEmergencyContact> emergencyContacts = new ArrayList<EmployeeEmergencyContact>();

        if(!database.isOpen()) this.open();
        Cursor cursor = database.query("emergency_contact", new String[] {
                "_id", "gen_info_id", "contact_name", "relationship", "address",
                "phone_type_code", "special_notes", "contact_no" },
                "gen_info_id = " + genInfoId, null, null, null, null);

        if(cursor.moveToFirst()) {
            do {
                emergencyContacts.add(new EmployeeEmergencyContact( Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex("gen_info_id"))),
                        cursor.getString(cursor.getColumnIndex("contact_name")),
                        cursor.getString(cursor.getColumnIndex("relationship")),
                        cursor.getString(cursor.getColumnIndex("address")),
                        Long.parseLong(cursor.getString(cursor.getColumnIndex("phone_type_code"))),
                        cursor.getString(cursor.getColumnIndex("special_notes")),
                        cursor.getString(cursor.getColumnIndex("contact_no"))));
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return emergencyContacts;
    }

    public long insertEmployee(Employee employee) {
        //if(!database.isOpen()) this.open();
        this.open();

        ContentValues values = new ContentValues();
        values.put("_id", employee.getId());
        values.put("emp_no", employee.getCode());
        values.put("name", employee.getName());
        values.put("address", employee.getAddress());
        values.put("birth_date", employee.getBirthdate());
        values.put("mobile_no", employee.getMobileno());
        values.put("work_phone", employee.getWorkphone());
        values.put("email", employee.getEmail());
        values.put("gender_code", employee.getGender());
        values.put("civil_status_code", employee.getCivilStatusCode());
        values.put("hire_date", employee.getHiredDate());
        values.put("location", employee.getLocation());
        values.put("position_id", employee.getPositionId());
        values.put("department_id", employee.getDepartmentId());
        values.put("is_admin", employee.getIsAdmin());
        values.put("photo_path", employee.getPhotoPath());
        values.put("is_deleted", employee.isDeleted());

        return database.insertOrThrow("gen_info", null, values);
    }

    public boolean deleteEmployee(long rowId) {
        // Do not delete the record, just tag it as DELETED
        ContentValues args = new ContentValues();
        args.put("is_deleted", true);
        this.open();
        //return database.delete("gen_info", "_id = " + rowId, null) > 0;
        return database.update("gen_info", args, "_id = " + rowId, null) > 0;
    }

    public boolean updateEmployee(Employee employee) {
        ContentValues values = new ContentValues();
        values.put("emp_no", employee.getCode());
        values.put("name", employee.getName());
        values.put("address", employee.getAddress());
        values.put("birth_date", employee.getBirthdate());
        values.put("mobile_no", employee.getMobileno());
        values.put("work_phone", employee.getWorkphone());
        values.put("email", employee.getEmail());
        values.put("gender_code", employee.getGender());
        values.put("civil_status_code", employee.getCivilStatusCode());
        values.put("hire_date", employee.getHiredDate());
        values.put("location", employee.getLocation());
        values.put("position_id", employee.getPositionId());
        values.put("department_id", employee.getDepartmentId());
        values.put("is_admin", employee.getIsAdmin());
        values.put("photo_path", employee.getPhotoPath());
        values.put("is_deleted", employee.isDeleted());
        this.open();
        return database.update("gen_info", values, "_id = " + employee.getId(), null) > 0;
    }

    public boolean isUniqueEmployeeCode(String code) {
        boolean isUnique = true;

        this.open();

        String query = "SELECT _id FROM gen_info WHERE emp_no = '" + code + "'";
        Cursor cursor = database.rawQuery(query, null);
        if(cursor != null && cursor.getCount() > 0) isUnique = false;
        cursor.close();
        database.close();

        return isUnique;
    }

    public long getNextAvailableID(String table) {
        long next_id = 0;
        String data;

        this.open();

        String query = "SELECT MAX(_id) as 'last_id' from " + table;
        Cursor cursor = database.rawQuery(query, null);
        try {
            if(cursor.moveToFirst()) {
                data = cursor.getString(0);
                if(data != null)
                    next_id = Long.parseLong(data) + 1;
                currentID = next_id;
            }
        } catch (Exception error) {
            cursor.close();
            database.close();
            currentID = next_id;
            return next_id;
        } finally {
            cursor.close();
            database.close();
            currentID = next_id;
            return next_id;
        }

    }

    public long saveEmergencyContacts(EmployeeEmergencyContact contacts) {
        this.open();

        ContentValues values = new ContentValues();
        values.put("_id", contacts.getId());
        values.put("gen_info_id", contacts.getGenInfoId());
        values.put("contact_name", contacts.getContactName());
        values.put("relationship", contacts.getRelationship());
        values.put("address", contacts.getAddress());
        values.put("phone_type_code", contacts.getPhoneTypeCode());
        values.put("special_notes", contacts.getNotes());
        values.put("contact_no", contacts.getContact_no());

        return database.insertOrThrow("emergency_contact", null, values);
    }

    public boolean deleteEmergencyContacts(long genInfoId) {
        this.open();
        return database.delete("emergency_contact","gen_info_id = " + genInfoId, null) > 0;
    }

    public long saveLatestWages(EmployeeLatestWages latestWages) {
        this.open();

        ContentValues values = new ContentValues();
        values.put("_id", latestWages.getId());
        values.put("gen_info_id", latestWages.getGenInfoId());
        values.put("note", latestWages.getNote());
        values.put("date", latestWages.getDate());
        values.put("rate", latestWages.getRate());
        return database.insertOrThrow("latest_wage", null, values);
    }

    public boolean deleteLatestWages(long genInfoId) {
        this.open();
        return database.delete("latest_wage","gen_info_id = " + genInfoId, null) > 0;
    }
}
