package com.pgmacdesign.demolinktogae.pojo;

import java.util.Date;

/**
 * Created by pmacdowell on 12/15/2015.
 */
public class Employee extends MasterObject{

    private Long id;
    private String sessionId;
    private byte[] picture;
    private String firstName;
    private String lastName;
    private Date hireDate;
    private Boolean attendedHrTraining;
    private String message;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public Boolean getAttendedHrTraining() {
        return attendedHrTraining;
    }

    public void setAttendedHrTraining(Boolean attendedHrTraining) {
        this.attendedHrTraining = attendedHrTraining;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
