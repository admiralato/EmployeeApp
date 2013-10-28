package com.atosoft.EmployeeApp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Joe on 10/4/13.
 */
public class EmployeeLatestWages implements Parcelable {
    private long id;
    private long genInfoId;
    private String note;
    private String date;
    private float rate;

    public EmployeeLatestWages ( long id, long genInfoId,
                                      String note,
                                      String date,
                                      float rate) {
        this.id = id;
        this.setGenInfoId(genInfoId);
        this.setNote(note);
        this.setDate(date);
        this.setRate(rate);
    }

    public static final Parcelable.Creator<EmployeeLatestWages> CREATOR = new Parcelable.Creator<EmployeeLatestWages>() {
        public EmployeeLatestWages createFromParcel(Parcel in) {
            return new EmployeeLatestWages(in);
        }

        public EmployeeLatestWages[] newArray(int size) {
            return new EmployeeLatestWages[size];
        }
    };

    public EmployeeLatestWages(Parcel in) {
        this.id = in.readLong();
        this.genInfoId = in.readLong();
        this.note = in.readString();
        this.date = in.readString();
        this.rate= in.readFloat();
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }


    public long getGenInfoId() {
        return genInfoId;
    }

    public void setGenInfoId(long genInfoId) {
        this.genInfoId = genInfoId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return Float.toString(this.rate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.genInfoId);
        dest.writeString(this.note);
        dest.writeString(this.date);
        dest.writeFloat(this.rate);
    }
}
