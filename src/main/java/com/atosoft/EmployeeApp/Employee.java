package com.atosoft.EmployeeApp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joe on 9/20/13.
 */
public class Employee implements Parcelable{
    private long id;
    private String photoPath;
    private String name;
    private String code;
    private String address;
    private String birthdate;
    private String mobileno;
    private String workphone;
    private String email;
    private long genderStatusCode;
    private long civilStatusCode;
    private String hireddate;
    private String location;
    private long positionId;
    private long departmentId;
    private boolean isAdmin;
    private boolean isDeleted;

    private Gender objGender;
    private CivilStatus objCivilStatus;
    private Position objPosition;
    private Department objDepartment;

    private String genderString;
    private String civilStatusString;
    private String positionString;
    private String departmentString;

    private List<EmployeeLatestWages> latestWageList = new ArrayList<EmployeeLatestWages>();
    private List<EmployeeEmergencyContact> emergencyContactList = new ArrayList<EmployeeEmergencyContact>();


    public static final Parcelable.Creator<Employee> CREATOR = new Parcelable.Creator<Employee>() {
        public Employee createFromParcel(Parcel in) {
            return new Employee(in);
        }

        public Employee[] newArray(int size) {
            return new Employee[size];
        }
    };

    public Employee() {
        latestWageList = new ArrayList<EmployeeLatestWages>();
        emergencyContactList = new ArrayList<EmployeeEmergencyContact>();
    }

    public Employee(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.code = in.readString();
        this.address = in.readString();
        this.birthdate = in.readString();
        this.mobileno = in.readString();
        this.workphone = in.readString();
        this.email = in.readString();
        this.genderStatusCode = in.readLong();
        this.civilStatusCode = in.readLong();
        this.hireddate = in.readString();
        this.location = in.readString();
        this.positionId = in.readLong();
        this.departmentId = in.readLong();
        this.isAdmin = Boolean.parseBoolean(in.readString());
        this.photoPath = in.readString();
        this.isDeleted = Boolean.parseBoolean(in.readString());

        this.setGenderString(in.readString());
        this.setCivilStatusString(in.readString());
        this.setPositionString(in.readString());
        this.setDepartmentString(in.readString());

        in.readTypedList(this.latestWageList, EmployeeLatestWages.CREATOR);
        in.readTypedList(this.emergencyContactList, EmployeeEmergencyContact.CREATOR);
    }

    public Employee(long id, String name, String code, String address,
                    String birthdate, String mobileno, String workphone,
                    String email, long genderStatusCode, long civilStatusCode,
                    String hireddate, String location, long positionId,
                    long departmentId, boolean isAdmin, String photoPath, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.address = address;
        this.birthdate = birthdate;
        this.mobileno = mobileno;
        this.workphone = workphone;
        this.email = email;
        this.genderStatusCode = genderStatusCode;
        this.civilStatusCode = civilStatusCode;
        this.hireddate = hireddate;
        this.location = location;
        this.positionId = positionId;
        this.departmentId = departmentId;
        this.isAdmin = isAdmin;
        this.photoPath = photoPath;
        this.isDeleted = isDeleted;

        latestWageList = new ArrayList<EmployeeLatestWages>();
        emergencyContactList = new ArrayList<EmployeeEmergencyContact>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getCivilStatusCode() {
        return civilStatusCode;
    }

    public void setCivilStatusCode(long civilStatusCode) {
        this.civilStatusCode = civilStatusCode;
    }

    public void setBirthDate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setWorkPhone(String workphone) {
        this.workphone = workphone;
    }

    public String getWorkphone() {
        return workphone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setGender(long genderCode) {
        this.genderStatusCode = genderCode;
    }

    public long getGender() {
        return this.genderStatusCode;
    }

    public void setHiredDate(String hireddate) {
        this.hireddate = hireddate;
    }

    public String getHiredDate() {
        return hireddate;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setPositionId(long positionId) {
        this.positionId = positionId;
    }

    public long getPositionId() {
        return positionId;
    }

    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }

    public long getDepartmentId() {
        return departmentId;
    }

    public void setPhotoPath(String path) {
        this.photoPath = path;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    @Override
    public String toString() {
        return name + "\n" + code;
    }

    public List<EmployeeLatestWages> getLatestWageList() {
        return latestWageList;
    }

    public void setLatestWageList(List<EmployeeLatestWages> latestWageList) {
        this.latestWageList = latestWageList;
    }

    public List<EmployeeEmergencyContact> getEmergencyContactList() {
        return emergencyContactList;
    }

    public void setEmergencyContactList(List<EmployeeEmergencyContact> emergencyContactList) {
        this.emergencyContactList = emergencyContactList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.code);
        dest.writeString(this.address);
        dest.writeString(this.birthdate);
        dest.writeString(this.mobileno);
        dest.writeString(this.workphone);
        dest.writeString(this.email);
        dest.writeLong(this.genderStatusCode);
        dest.writeLong(this.civilStatusCode);
        dest.writeString(this.hireddate);
        dest.writeString(this.location);
        dest.writeLong(this.positionId);
        dest.writeLong(this.departmentId);
        dest.writeString(Boolean.toString(this.isAdmin));
        dest.writeString(this.photoPath);
        dest.writeString(Boolean.toString(this.isDeleted));

        dest.writeString(this.genderString);
        dest.writeString(this.civilStatusString);
        dest.writeString(this.positionString);
        dest.writeString(this.departmentString);

        dest.writeTypedList(this.latestWageList);
        dest.writeTypedList(this.emergencyContactList);
    }

    public Gender getObjGender() {
        return objGender;
    }

    public void setObjGender(Gender objGender) {
        this.objGender = objGender;
    }

    public CivilStatus getObjCivilStatus() {
        return objCivilStatus;
    }

    public void setObjCivilStatus(CivilStatus objCivilStatus) {
        this.objCivilStatus = objCivilStatus;
    }

    public Position getObjPosition() {
        return objPosition;
    }

    public void setObjPosition(Position objPosition) {
        this.objPosition = objPosition;
    }

    public Department getObjDepartment() {
        return (Department) objDepartment;
    }

    public void setObjDepartment(Department objDepartment) {
        this.objDepartment = objDepartment;
    }

    public String getGenderString() {
        return genderString;
    }

    public void setGenderString(String genderString) {
        this.genderString = genderString;
    }

    public String getCivilStatusString() {
        return civilStatusString;
    }

    public void setCivilStatusString(String civilStatusString) {
        this.civilStatusString = civilStatusString;
    }

    public String getPositionString() {
        return positionString;
    }

    public void setPositionString(String positionString) {
        this.positionString = positionString;
    }

    public String getDepartmentString() {
        return departmentString;
    }

    public void setDepartmentString(String departmentString) {
        this.departmentString = departmentString;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
