package com.pgmacdesign.demolinktogae.utility;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.pgmacdesign.demolinktogae.misc.L;
import com.pgmacdesign.demolinktogae.misc.ServerCallLoadedListener;
import com.pgmacdesign.demolinktogae.pojo.Employee;
import com.pgmacdesign.demolinktogae.pojo.MasterObject;
import com.pgmacdesign.demolinktogae.pojo.User;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pmacdowell on 12/15/2015.
 */
public class MyUtilities {

    // TODO: 12/17/2015 TO DO! Need to change up the input method here,  fix comp errors and handle diff
    public enum pojoObjects{
        EMPLOYEE, USER, EMPLOYEE_LIST, USER_LIST
    }
    /**
     * Checks for network connectivity either via wifi or cellular.
     * I am aware that getAllNetworkInfo is deprecated, leaving it in for now
     * @param context The context of the activity doing the checking
     * @return A Boolean. True if they have connection, false if they do not
     */
    public static boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    /**
     * This class sends a network request (POST) on a background thread
     * @param <T> T Extends MasterObject
     */
    public static class SendNetworkRequest <T extends MasterObject> extends AsyncTask<Void, Void, String> {
        private ServerCallLoadedListener listener;
        private VolleySingleton volleySingleton;
        private RequestQueue requestQueue;
        private pojoObjects type;
        private T passedObject;
        private String url;


        public SendNetworkRequest(ServerCallLoadedListener listener1, pojoObjects type1, String url1, T passedObject1) {
            this.listener = listener1;
            this.volleySingleton = VolleySingleton.getInstance();
            this.requestQueue = volleySingleton.getRequestQueue();
            this.passedObject = passedObject1;
            this.type = type1;
            this.url = url1;
        }

        @Override
        protected String doInBackground(Void... params) {
            if(passedObject != null) {
                //First we will make a Response listener for the Volley:
                Response.Listener<JSONObject> volleyListener = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Here we parse the data and send it back via the other listener
                        String jsonResponse = response.toString();
                        L.m("Response String = " + jsonResponse);

                        switch (type) {
                            case EMPLOYEE:
                                T parsedObject = MyUtilities.parseNetworkResponse(jsonResponse, type);
                                Employee employee = (Employee) parsedObject;
                                listener.employeeFinishedLoading(employee);
                                break;

                            case EMPLOYEE_LIST:
                                MasterObject masterObject = MyUtilities.parseNetworkResponseMaster(jsonResponse);
                                List<Employee> employeeList = masterObject.getEmployees();
                                List<Employee> listToReturn = new ArrayList<>();
                                for(Employee emp : employeeList){
                                    listToReturn.add(emp);
                                }
                                listener.employeesFinishedLoading(listToReturn);
                                break;

                            case USER:
                                T parsedObject2 = MyUtilities.parseNetworkResponse(jsonResponse, type);
                                User user = (User) parsedObject2;
                                listener.userFinishedLoading(user);
                                break;

                            case USER_LIST:
                                MasterObject masterObject1 = MyUtilities.parseNetworkResponseMaster(jsonResponse);
                                List<User> userList = masterObject1.getUsers();
                                List<User> listToReturn1 = new ArrayList<>();
                                for(User user1 : userList){
                                    listToReturn1.add(user1);
                                }
                                listener.usersFinishedLoading(listToReturn1);
                                break;

                            default:
                                listener.simpleNetworkResponseString("Fail");
                                break;
                        }
                    }
                };
                //Error listener
                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.simpleNetworkResponseString("Volley Error");
                        L.m("Volley Error");
                        error.printStackTrace();
                    }
                };

                try {
                    MyUtilities.sendPostRequest(requestQueue, url, volleyListener, passedObject, errorListener);
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.simpleNetworkResponseString("Error");
                }
            }
            return null;
        }
    }

    /**
     * Send a post request
     * @param requestQueue Requestqueue to use
     * @param url The URL sending to
     * @param listener The listener for the response
     * @param objectToSend The object being sent (One of our pojo objects)
     * @param errorListener The listener for any errors
     * @param <T> T extends MasterObject
     */
    public static <T extends MasterObject> void sendPostRequest(RequestQueue requestQueue, String url,
                                                                Response.Listener<JSONObject> listener,
                                                                T objectToSend,
                                                                Response.ErrorListener errorListener){
        Gson gson = new Gson();
        if(objectToSend == null){
            return;
        }
        String requestBody = gson.toJson(objectToSend);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                requestBody,
                listener,
                errorListener
        );
        //TEST
        L.m("URL = " + url);
        L.m("requestBody = " + requestBody);
        //END TEST
        requestQueue.add(jsonObjectRequest);

        return;
    }

    /*
    Sample StringRequest:
    StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            L.m("Response = " + response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonResponse = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    Gson gson = new Gson();
                    Response aResponse;
                    switch (type) {
                        case EMPLOYEE:
                            aResponse = Response.success(gson.fromJson(jsonResponse, Employee.class),
                                    HttpHeaderParser.parseCacheHeaders(response));
                            break;

                        case USER:
                            aResponse = Response.success(gson.fromJson(jsonResponse, User.class),
                                    HttpHeaderParser.parseCacheHeaders(response));
                            break;

                        default:
                            aResponse = null;
                    }
                    return aResponse;
                } catch (Exception e){
                    e.printStackTrace();
                }
                return null;

            }

            //We don't need to add this, but, for future reference, this is how you would make a
            //POST request if we were not using objects / GSON to do so
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                // the POST parameters:
                params.put("site", "code");
                params.put("network", "tutsplus");
                return params;
            }

            };

     */
    /**
     * Parses the network response using GSON.
     * @param jsonResponse The string response from the network
     * @param type pojoObject Enum type. This is used for determining which parsing method to use
     * @param <T> T extends MasterObject (IE User or Employee)
     * @return a T object, which can be case back after being returned
     */
    public static <T extends MasterObject> T parseNetworkResponse(String jsonResponse, pojoObjects type) {
        //Declare Variables
        Gson gson = new Gson();
        T returnObject;

        //Check if the data is null or empty
        if(jsonResponse == null){
            return null;
        }
        if(jsonResponse.isEmpty()){
            return null;
        }

        //Check the type passed and convert it. If valid, return it
        try {
            switch (type) {
                case EMPLOYEE:
                    Employee employee = gson.fromJson(jsonResponse, Employee.class);
                    if(employee != null){
                        returnObject = (T) employee;
                        return returnObject;
                    }
                    break;

                case USER:
                    User user = gson.fromJson(jsonResponse, User.class);
                    if(user != null){
                        returnObject = (T) user;
                        return returnObject;
                    }
                    break;

                default:
                    //Empty for now
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static <T extends MasterObject> MasterObject parseNetworkResponseMaster(String jsonResponse) {
        //Declare Variables
        Gson gson = new Gson();
        List<T> returnObjects = new ArrayList<>();

        //Check if the data is null or empty
        if(jsonResponse == null){
            return null;
        }
        if(jsonResponse.isEmpty()){
            return null;
        }

        //Check the type passed and convert it. If valid, return it
        try {
            MasterObject myMasterObject = gson.fromJson(jsonResponse, MasterObject.class);
            return myMasterObject;

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * This will hide the keyboard
     * @param activeActivity The current activity
     */
    public static void hideTheKeyboard(Activity activeActivity) {
        try {
            //IM Object used for acting upon the digital keyboard
            InputMethodManager inputMethodManager = (InputMethodManager) activeActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            View view = activeActivity.getCurrentFocus();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = new View(activeActivity);
            }
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
