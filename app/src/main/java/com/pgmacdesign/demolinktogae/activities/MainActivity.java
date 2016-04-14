package com.pgmacdesign.demolinktogae.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.pgmacdesign.demolinktogae.R;
import com.pgmacdesign.demolinktogae.adapters.RecyclerViewAdapterCustom;
import com.pgmacdesign.demolinktogae.misc.CustomClickInterface;
import com.pgmacdesign.demolinktogae.misc.L;
import com.pgmacdesign.demolinktogae.misc.ServerCallLoadedListener;
import com.pgmacdesign.demolinktogae.misc.SharedPrefs;
import com.pgmacdesign.demolinktogae.pojo.Employee;
import com.pgmacdesign.demolinktogae.pojo.MasterObject;
import com.pgmacdesign.demolinktogae.pojo.User;
import com.pgmacdesign.demolinktogae.retrofitstuff.RetrofitCalls;
import com.pgmacdesign.demolinktogae.retrofitstuff.RetrofitClient;
import com.pgmacdesign.demolinktogae.retrofitstuff.RetrofitListeners;
import com.pgmacdesign.demolinktogae.utility.MyUtilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pmacdowell on 12/18/2015.
 */
public class MainActivity extends AppCompatActivity implements ServerCallLoadedListener, CustomClickInterface, RetrofitListeners {

    //UI Components
    private RecyclerView main_activity_recyclerview;
    private TextView main_activity_top_tv;
    private RecyclerViewAdapterCustom employeesAdapter;
    private String sessionId;

    //Misc
    private static final String getEmployeesURL = "https://com-pgmacdesign-androidtest2.appspot.com/_ah/api/testendpoint/v1/getEmployees";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        initialize();
        setupUI();
        getPassedId();
        ///sendNetworkRequest();
        //TEST
        testRetrofit();
    }

    private void testRetrofit(){
        User user = new User();
        user.setSessionId(sessionId);
        RetrofitCalls.getAllEmployees(this, user);
    }

    /**
     * Initialize variables and the like
     */
    private void initialize(){
        //UI
        main_activity_recyclerview = (RecyclerView) this.findViewById(R.id.main_activity_recyclerview);
        main_activity_top_tv = (TextView) this.findViewById(R.id.main_activity_top_tv);

        //Recyclerview and adapter
        employeesAdapter = new RecyclerViewAdapterCustom(this, this, MyUtilities.pojoObjects.EMPLOYEE);
        main_activity_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        main_activity_recyclerview.setAdapter(employeesAdapter);
    }

    /**
     * Setup the UI Components
     */
    private void setupUI(){
        //Empty for now
    }

    /**
     * Gets the passed Session ID via the shared prefs
     */
    private void getPassedId(){
        sessionId = SharedPrefs.getString("sessionId", null);
    }

    /**
     * Sends a network request out
     */
    private void sendNetworkRequest(){
        //Remember, we need an object to send in order to retrieve the sessionId
        if(sessionId != null) {
            User user = new User();
            L.m("Session id = " + sessionId);
            user.setSessionId(sessionId);
            MyUtilities.SendNetworkRequest sendNetworkRequest = new MyUtilities.SendNetworkRequest(
                    this, MyUtilities.pojoObjects.EMPLOYEE_LIST, getEmployeesURL, user);
            Void[] param = null;
            sendNetworkRequest.execute(param);
        } else {
            //Issue with sessionId Being passed
        }
    }

    /////////These below are all for the listener///////////////////////////////////////////////////
    @Override
    public void userFinishedLoading(User pojo) {
        //
    }

    @Override
    public void usersFinishedLoading(List<User> pojos) {
        //
    }

    @Override
    public void employeeFinishedLoading(Employee pojo) {
        //
    }

    @Override
    public void employeesFinishedLoading(List<Employee> pojos) {
        if(pojos.size() > 0){
            employeesAdapter.setData(pojos);
        } else {
            // TODO: 12/18/2015 Do something here to indicate none loaded
        }
    }

    @Override
    public void simpleNetworkResponseInt(int response) {
        //
    }

    @Override
    public void simpleNetworkResponseString(String response) {
        //
    }


    ////////These are the custom click methods//////////////////////////////////////////////////////

    @Override
    public void employeeClicked(Employee pojo) {

    }

    @Override
    public void userClicked(User user) {

    }

    @Override
    public void onServerCodeLoaded(int serverCode) {

    }

    @Override
    public void onUserLoaded(User user) {

    }

    @Override
    public void onEmployeeLoaded(Employee employee) {

    }

    @Override
    public void onMasterObjectLoaded(MasterObject masterObject) {

        List<Employee> pojos = new ArrayList<>();
        if(masterObject != null){
            pojos = masterObject.getEmployees();
        }
        if(pojos.size() > 0){
            employeesAdapter.setData(pojos);
        } else {
            // TODO: 12/18/2015 Do something here to indicate none loaded
        }
    }

    @Override
    public void onErrorListener(String errorResponse) {

    }
}
