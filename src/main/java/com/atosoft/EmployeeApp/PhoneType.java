package com.atosoft.EmployeeApp;

/**
 * Created by Joe on 10/3/13.
 */
public class PhoneType {
    private  int id;
    private String code;
    private String description;

    public PhoneType ( int id , String code, String description ) {
        this.id = id;
        this.code = code;
        this.description = description;
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
        return this.code;
    }
}
