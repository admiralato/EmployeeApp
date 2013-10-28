package com.atosoft.EmployeeApp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Joe on 10/4/13.
 */
public class EmployeeEmergencyContact implements Parcelable {
    private long id;
    private long genInfoId;
    private String contactName;
    private String relationship;
    private String address;
    private long phoneTypeCode;
    private String notes;
    private String contact_no;

    public EmployeeEmergencyContact ( long id, long genInfoId,
                                      String contactName,
                                      String relationship,
                                      String address,
                                      long phoneTypeCode,
                                      String notes, String contact_no) {
        this.id = id;
        this.genInfoId = genInfoId;
        this.contactName = contactName;
        this.relationship = relationship;
        this.address = address;
        this.phoneTypeCode = phoneTypeCode;
        this.notes = notes;
        this.contact_no = contact_no;
    }

    public static final Parcelable.Creator<EmployeeEmergencyContact> CREATOR = new Parcelable.Creator<EmployeeEmergencyContact>() {
        public EmployeeEmergencyContact createFromParcel(Parcel in) {
            return new EmployeeEmergencyContact(in);
        }

        public EmployeeEmergencyContact[] newArray(int size) {
            return new EmployeeEmergencyContact[size];
        }
    };

    public EmployeeEmergencyContact(Parcel in) {
        this.id = in.readLong();
        this.genInfoId = in.readLong();
        this.contactName = in.readString();
        this.relationship= in.readString();
        this.address = in.readString();
        this.phoneTypeCode = in.readLong();
        this.notes = in.readString();
        this.contact_no= in.readString();
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

    public String getContactName() {
        return this.contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getRelationship() {
        return this.relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getPhoneTypeCode() {
        return phoneTypeCode;
    }

    public void setPhoneTypeCode(long phoneTypeCode) {
        this.phoneTypeCode = phoneTypeCode;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    @Override
    public String toString() {
        return this.contactName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.genInfoId);
        dest.writeString(this.contactName);
        dest.writeString(this.relationship);
        dest.writeString(this.address);
        dest.writeLong(this.phoneTypeCode);
        dest.writeString(this.notes);
        dest.writeString(this.contact_no);
    }
}
