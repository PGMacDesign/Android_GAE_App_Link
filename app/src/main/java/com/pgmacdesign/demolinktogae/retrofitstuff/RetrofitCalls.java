package com.pgmacdesign.demolinktogae.retrofitstuff;

import android.content.Context;

import com.pgmacdesign.demolinktogae.misc.L;
import com.pgmacdesign.demolinktogae.misc.MyApplication;
import com.pgmacdesign.demolinktogae.pojo.Employee;
import com.pgmacdesign.demolinktogae.pojo.MasterObject;
import com.pgmacdesign.demolinktogae.pojo.User;
import com.pgmacdesign.demolinktogae.utility.MyUtilities;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.Body;

/**
 * This class will house the public static calls
 * Created by pmacdowell on 4/11/2016.
 */
public class RetrofitCalls {

    private static RetrofitService retrofitService;
    private static RetrofitClient retrofitClient;
    private static String defaultErrorString = "Oops! An error occurred";

    /**
     * This call will check for internet connectivity before moving forward.
     * This is where you would want to put a kill switch of sorts too if you were wanting to prevent
     * users from making web requests in certain situations (IE not logged in)
     * @return A boolean, false if they are not to proceed, true if they are
     */
    private static boolean initialCall(){

        /*
        First initialize the RetrofitService and RetrofitClient calls. We want these to hit
        before the internet connectivity check so they are initialized regardless
         */
        if(retrofitClient == null){
            retrofitClient = new RetrofitClient();
        }
        if(retrofitService == null){
            retrofitService = RetrofitClient.getServiceClient();
        }

        //Next get the context and use it to check for internet connectivity
        Context context = MyApplication.getAppContext();
        //If you do not have this code from a previous tutorial, I will include a link below
        boolean hasInternetConnection = MyUtilities.haveNetworkConnection(context);

        //More checks would go here, but for now, simply return off of their connection
        if(hasInternetConnection){
            return true;
        } else {
            L.toast(context, "It appears you do not have a stable internet connection");
            return false;
        }
    }

    /**
     * This class is called to sign a user in. It will send a username and password combo to the
     * server via a User object. It should be noted that there are far more concise ways to go about
     * this code, but I wanted to make various options available, so it will seem like more code
     * than is needed in the onResponse.
     * @param listener This is the RetrofitListeners object that the response will be sent back along
     * @param user This is the user object being sent to the server. It needs to include a username
     *             and a password
     */
    public static void signIn(final RetrofitListeners listener, final User user){
        if(!RetrofitCalls.initialCall()){
            return;
        }
        if(user == null || listener == null){
            return;
        }

        //Past the initial call, move on to the retrofit call
        Call<User> call = retrofitService.signin(user);
        call.enqueue(new Callback<User>() {
            /**
             * This is the onResponse. It will return data when it is not a failure call
             * @param response
             * @param retrofit
             */
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {

                //First, get the respective response parts

                //Status code response from the server. Could be something like 404, 200, 501, etc
                Integer statusCode = response.code();

                //Error body, should only be received if the server sends back an error
                ResponseBody errorBody = response.errorBody();

                //User object. Received back from the server assuming Gson parsed everything correctly
                User receivedUser = response.body();

                //Used to prevent us from writing multiple nested if statements
                boolean responseWasNotNull = false;
                //First, we want to check if the received user is null, if it is, we move on to the errorbody
                if(receivedUser != null){
                    responseWasNotNull = true;
                    //The user object is returned, pass it to the listener
                    listener.onUserLoaded(receivedUser);
                }

                //If it is false at this point, it means the receivedUser was null, in which case we
                //Need to move on to the errorbody.
                if(!responseWasNotNull){
                    if(errorBody != null){
                        responseWasNotNull = true;
                        //Error received, get the String and pass it to the listener
                        String errorString = null;
                        try{
                            //Need a try / catch here due to an unhandled IO Exception
                            errorString = errorBody.string();
                        } catch (IOException e){
                            errorString = defaultErrorString;
                        }
                        listener.onErrorListener(errorString);
                    }
                }

                //If it is false at this point, it means the errorBody was null, in which case we
                //Need to move on to the status code instead
                if(!responseWasNotNull){
                    if(statusCode != null){
                        responseWasNotNull = true;
                        listener.onServerCodeLoaded(statusCode);
                    }
                }

                //If it is false at this point, it means everything failed, we need to send back an error
                if(!responseWasNotNull){
                    listener.onErrorListener(defaultErrorString);
                }

            }
            /**
             * The Failure call is hit when something goes wrong with the call, IE Gson parsing fail
             * @param t
             */
            @Override
            public void onFailure(Throwable t) {
                //For logging, I recommend you print out the throwable to see what is happening
                t.printStackTrace();
                //We will send back the error String here
                listener.onErrorListener(defaultErrorString);
            }
        });
    }

    /**
     * This class is called to update an employee's details. It will send employee info (emp object)
     * as well as a sessionId. It should be noted that there are far more concise ways to go about
     * this code, but I wanted to make various options available, so it will seem like more code
     * than is needed in the onResponse.
     * @param listener This is the RetrofitListeners object that the response will be sent back along
     * @param employee This is the employee object being sent to the server. It needs to include the
     *                 employee's updated info as well as a sessionId.
     */
    public static void updateEmployee(final RetrofitListeners listener, final Employee employee){
        if(!RetrofitCalls.initialCall()){
            return;
        }
        if(employee == null || listener == null){
            return;
        }

        //Past the initial call, move on to the retrofit call
        Call<Employee> call = retrofitService.updateEmployee(employee);
        call.enqueue(new Callback<Employee>() {
            /**
             * This is the onResponse. It will return data when it is not a failure call
             * @param response
             * @param retrofit
             */
            @Override
            public void onResponse(Response<Employee> response, Retrofit retrofit) {

                //First, get the respective response parts

                //Status code response from the server. Could be something like 404, 200, 501, etc
                Integer statusCode = response.code();

                //Error body, should only be received if the server sends back an error
                ResponseBody errorBody = response.errorBody();

                //Employee object. Received back from the server assuming Gson parsed everything correctly
                Employee receivedEmployee = response.body();

                //Used to prevent us from writing multiple nested if statements
                boolean responseWasNotNull = false;
                //First, we want to check if the received emp is null, if it is, we move on to the errorbody
                if(receivedEmployee != null){
                    responseWasNotNull = true;
                    //The emp object is returned, pass it to the listener
                    listener.onEmployeeLoaded(receivedEmployee);
                }

                //If it is false at this point, it means the receivedEmployee was null, in which
                //case we Need to move on to the errorbody.
                if(!responseWasNotNull){
                    if(errorBody != null){
                        responseWasNotNull = true;
                        //Error received, get the String and pass it to the listener
                        String errorString = null;
                        try{
                            //Need a try / catch here due to an unhandled IO Exception
                            errorString = errorBody.string();
                        } catch (IOException e){
                            errorString = defaultErrorString;
                        }
                        listener.onErrorListener(errorString);
                    }
                }

                //If it is false at this point, it means the errorBody was null, in which case we
                //Need to move on to the status code instead
                if(!responseWasNotNull){
                    if(statusCode != null){
                        responseWasNotNull = true;
                        listener.onServerCodeLoaded(statusCode);
                    }
                }

                //If it is false at this point, it means everything failed, we need to send back an error
                if(!responseWasNotNull){
                    listener.onErrorListener(defaultErrorString);
                }

            }
            /**
             * The Failure call is hit when something goes wrong with the call, IE Gson parsing fail
             * @param t
             */
            @Override
            public void onFailure(Throwable t) {
                //For logging, I recommend you print out the throwable to see what is happening
                t.printStackTrace();
                //We will send back the error String here
                listener.onErrorListener(defaultErrorString);
            }
        });
    }

    /**
     * This class is called to get all employees in the DB. It must send a User object that contains
     * a sessionId. It should be noted that there are far more concise ways to go about
     * this code, but I wanted to make various options available, so it will seem like more code
     * than is needed in the onResponse.
     * @param listener This is the RetrofitListeners object that the response will be sent back along
     * @param user This user object only needs to contain a sessionId
     */
    public static void getAllEmployees(final RetrofitListeners listener, final User user){
        if(!RetrofitCalls.initialCall()){
            return;
        }
        if(user == null || listener == null){
            return;
        }

        //Past the initial call, move on to the retrofit call
        Call<MasterObject> call = retrofitService.getAllEmployees(user);
        call.enqueue(new Callback<MasterObject>() {
            /**
             * This is the onResponse. It will return data when it is not a failure call
             * @param response
             * @param retrofit
             */
            @Override
            public void onResponse(Response<MasterObject> response, Retrofit retrofit) {

                //First, get the respective response parts

                //Status code response from the server. Could be something like 404, 200, 501, etc
                Integer statusCode = response.code();

                //Error body, should only be received if the server sends back an error
                ResponseBody errorBody = response.errorBody();

                //MasterObject object. Received back from the server assuming Gson parsed everything correctly
                MasterObject receivedObject = response.body();

                //Used to prevent us from writing multiple nested if statements
                boolean responseWasNotNull = false;
                //First, we want to check if the received object is null, if it is, we move on to the errorbody
                if(receivedObject != null){
                    responseWasNotNull = true;
                    //The master object is returned, pass it to the listener
                    listener.onMasterObjectLoaded(receivedObject);
                }

                //If it is false at this point, it means the masterObject was null, in which
                //case we Need to move on to the errorbody.
                if(!responseWasNotNull){
                    if(errorBody != null){
                        responseWasNotNull = true;
                        //Error received, get the String and pass it to the listener
                        String errorString = null;
                        try{
                            //Need a try / catch here due to an unhandled IO Exception
                            errorString = errorBody.string();
                        } catch (IOException e){
                            errorString = defaultErrorString;
                        }
                        listener.onErrorListener(errorString);
                    }
                }

                //If it is false at this point, it means the errorBody was null, in which case we
                //Need to move on to the status code instead
                if(!responseWasNotNull){
                    if(statusCode != null){
                        responseWasNotNull = true;
                        listener.onServerCodeLoaded(statusCode);
                    }
                }

                //If it is false at this point, it means everything failed, we need to send back an error
                if(!responseWasNotNull){
                    listener.onErrorListener(defaultErrorString);
                }

            }
            /**
             * The Failure call is hit when something goes wrong with the call, IE Gson parsing fail
             * @param t
             */
            @Override
            public void onFailure(Throwable t) {
                //For logging, I recommend you print out the throwable to see what is happening
                t.printStackTrace();
                //We will send back the error String here
                listener.onErrorListener(defaultErrorString);
            }
        });
    }

}
