package com.atosoft.EmployeeApp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Joe on 9/27/13.
 */
public class Department implements Parcelable{
    private  int id;
    private String code;
    private String description;

    public Department ( int id , String code, String description ) {
        this.id = id;
        this.code = code;
        this.description = description;
    }

    public static final Parcelable.Creator<Department> CREATOR = new Parcelable.Creator<Department>() {
        public Department createFromParcel(Parcel in) {
            return new Department(in);
        }

        public Department[] newArray(int size) {
            return new Department[size];
        }
    };

    public Department(Parcel in) {
        this.id = in.readInt();
        this.code = in.readString();
        this.description = in.readString();
    }

    public int getId() {
        return this.id;
    }

    public String getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        return this.description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.code);
        dest.writeString(this.description);
    }
}
