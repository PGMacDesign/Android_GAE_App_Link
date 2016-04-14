package com.pgmacdesign.demolinktogae.retrofitstuff;

import com.pgmacdesign.demolinktogae.pojo.Employee;
import com.pgmacdesign.demolinktogae.pojo.MasterObject;
import com.pgmacdesign.demolinktogae.pojo.User;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * This service interface is the base for all of the Retrofit calls. It houses the info for base calls
 * Created by pmacdowell on 4/11/2016.
 */
public interface RetrofitService {

    /*
    Notations here denote the type of call being sent. IE, standard @GET or @POST are what we will
    use for calls.
     */

    /**
     * This will sign a user in. It returns a User object with a sessionId included
     * @param user The user to sign in with (Include username and password)
     * @return User object with a valid sessionId if successful
     */
    @POST("testendpoint/v1/checkUserData")
    Call <User> signin(@Body User user);

    /**
     * This post request DOES require us to include both a sessionId that is included within a User
     * object. We therefore send the User as a body parameter. If this were included in the header
     * as an authorization token, we would simply use @Header instead.
     * @return A MasterObject, which includes lists of employees
     */
    @POST("testendpoint/v1/getEmployees")
    Call<MasterObject> getAllEmployees(@Body User user);

    /**
     * This will update an employee. Update the fields of the employee you want to change. You must
     * include a session Id or this will fail
     * @param employee Employee object to update. Include sessionId Here
     * @return Updated Employee object
     */
    @POST("testendpoint/v1/updateEmployee")
    Call<Employee> updateEmployee(@Body Employee employee);
}
