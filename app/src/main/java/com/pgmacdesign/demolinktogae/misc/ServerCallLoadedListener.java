package com.pgmacdesign.demolinktogae.misc;

import com.pgmacdesign.demolinktogae.pojo.Employee;
import com.pgmacdesign.demolinktogae.pojo.User;

import java.util.List;

/**
 * Created by pmacdowell on 12/15/2015.
 */
public interface ServerCallLoadedListener {
    public void userFinishedLoading(User pojo);
    public void usersFinishedLoading(List<User> pojos);
    public void employeeFinishedLoading(Employee pojo);
    public void employeesFinishedLoading(List<Employee> pojos);
    public void simpleNetworkResponseInt(int response);
    public void simpleNetworkResponseString(String response);
}
