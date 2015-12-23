package com.pgmacdesign.demolinktogae.pojo;

import java.util.List;

/**
 * Created by pmacdowell on 12/16/2015.
 */
public class MasterObject {
    private List<Employee> employees;
    private List<User> users;

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> items) {
        this.employees = items;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
