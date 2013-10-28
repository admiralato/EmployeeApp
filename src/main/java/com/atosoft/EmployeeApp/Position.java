package com.atosoft.EmployeeApp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Joe on 9/30/13.
 */
public class Position implements Parcelable {

    private  int id;
    private String code;
    private String description;

    public Position ( int id , String code, String description ) {
        this.id = id;
        this.code = code;
        this.description = description;
    }

    public static final Parcelable.Creator<Position> CREATOR = new Parcelable.Creator<Position>() {
        public Position createFromParcel(Parcel in) {
            return new Position(in);
        }

        public Position[] newArray(int size) {
            return new Position[size];
        }
    };

    public Position(Parcel in) {
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
