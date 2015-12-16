package com.pgmacdesign.demolinktogae.pojo;

import java.util.Date;

/**
 * Created by pmacdowell on 12/15/2015.
 */
public class Employee extends MasterObject{

    private String firstName;
    private String lastName;
    private Date hireDate;
    private boolean attendedHrTraining;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public boolean isAttendedHrTraining() {
        return attendedHrTraining;
    }

    public void setAttendedHrTraining(boolean attendedHrTraining) {
        this.attendedHrTraining = attendedHrTraining;
    }
}
