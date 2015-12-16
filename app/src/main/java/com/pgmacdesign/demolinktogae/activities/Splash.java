package com.pgmacdesign.demolinktogae.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pgmacdesign.demolinktogae.R;
import com.pgmacdesign.demolinktogae.misc.L;
import com.pgmacdesign.demolinktogae.misc.ServerCallLoadedListener;
import com.pgmacdesign.demolinktogae.pojo.Employee;
import com.pgmacdesign.demolinktogae.pojo.User;
import com.pgmacdesign.demolinktogae.utility.MyUtilities;

import java.util.List;

/**
 * This is the splash screen, it is the first thing loaded upon the app opening
 * Created by pmacdowell on 12/8/2015.
 */
public class Splash extends AppCompatActivity implements View.OnClickListener, TextWatcher, ServerCallLoadedListener {

    //UI
    private RelativeLayout splash_progress_view;
    private ProgressBar splash_loading_bar;
    private EditText splash_username, splash_pw;
    private Button splash_button;
    private TextView splash_halp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initialize();
        setDefaults();
    }

    /**
     * Initializes variables and sets Listeners
     */
    private void initialize(){

        //UI
        splash_progress_view = (RelativeLayout) this.findViewById(R.id.splash_progress_view);
        splash_loading_bar = (ProgressBar) this.findViewById(R.id.splash_loading_bar);
        splash_username = (EditText) this.findViewById(R.id.splash_username);
        splash_pw = (EditText) this.findViewById(R.id.splash_pw);
        splash_button = (Button) this.findViewById(R.id.splash_button);
        splash_halp = (TextView) this.findViewById(R.id.splash_halp);

        //Click Listeners
        splash_button.setOnClickListener(this);
        splash_halp.setOnClickListener(this);

        //Text listeners
        splash_username.addTextChangedListener(this);
        splash_pw.addTextChangedListener(this);

        //TESTING
        User user = new User();
        user.setFirstName("patrick");
        user.setLastName("MacDowell");
        user.setPassword("password123");
        String url = "https://com-pgmacdesign-androidtest2.appspot.com/_ah/api/testendpoint/v1/checkUserData";
        MyUtilities.SendNetworkRequest sendNetworkRequest = new MyUtilities.SendNetworkRequest(
                this, MyUtilities.pojoObjects.USER, url, user);
        Void[] param = null;
        sendNetworkRequest.execute(param);
        //END TESTING
    }

    /**
     * Sets Defaults, upon loading
     */
    private void setDefaults(){
        //Disable the button by default
        splash_button.setEnabled(false);

        //This looks a little more professional if you add the bolding (Use the fromHTML to set)
        String halp = "Not sure what's going on? This app is paired to a tutorial series, " +
                "<b>Click Here</b> to check it out";
        splash_halp.setText(Html.fromHtml(halp));

        //Hide this overlay view for now, used for a 'loading' view.
        splash_progress_view.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.splash_button:
                //Set the progress view visibility and to start spinning
                splash_progress_view.setVisibility(View.VISIBLE);
                splash_progress_view.bringToFront();
                splash_loading_bar.setVisibility(View.VISIBLE);
                splash_loading_bar.setIndeterminate(true);
                splash_loading_bar.setBackgroundColor(this.getResources().getColor(R.color.Semi_Transparent2));
                splash_loading_bar.bringToFront();

                //Make sure other buttons cannot be pressed during loading
                splash_halp.setEnabled(false);
                splash_button.setEnabled(false);

                //Hide the keyboard


                //Make our network call to Server
                // TODO: 12/8/2015 Does not exist yet, but will go right here
                // TODO: 12/8/2015 Handle response

                break;
            case R.id.splash_halp:
                //Nothing for now

                // TODO: 12/8/2015 Make an intent to open browser to page
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    /**
     * Handles text being changed in the Edit Texts
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(s.length() > 0) { //Check to make sure text is actually being entered
            if (validateEditTexts()) { //Make sure the edit text fields are valid before continuing
                splash_button.setEnabled(true);
            } else {
                splash_button.setEnabled(false);
            }
        } else {
            splash_button.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {}

    //NOTE! These will be refactored into utility methods, but for now they are here

    /**
     * Validates the edit texts to make sure there is text in each of them.
     * @return
     */
    private boolean validateEditTexts(){
        String usernameEditText = null;
        String usernamePassword = null;

        try {
            //Get the String from the Edit Text Fields
            usernameEditText = splash_username.getText().toString();
            usernamePassword = splash_pw.getText().toString();
        } catch (NullPointerException e){
            //This will only be caught in the event that the ETs are null, in which case, false
            return false;
        } catch (Exception e){} //Insurance policy

        //Check for null first
        if(usernameEditText == null || usernamePassword == null){
            return false;
        }

        //If they are both of length > 0, good to go
        if(usernameEditText.length() > 0 && usernamePassword.length() > 0){
            return true;
        }

        //Return false by default in case everything else makes it past (IE 1 is length > 0 but not the other
        return false;
    }

    @Override
    public void userFinishedLoading(User pojo) {
        String sessionID = pojo.getSessionId();
        String message = pojo.getMessage();
        L.m("Session ID = " + sessionID);
        L.m("Message = " + message);
    }

    @Override
    public void usersFinishedLoading(List<User> pojos) {

    }

    @Override
    public void employeeFinishedLoading(Employee pojo) {

    }

    @Override
    public void employeesFinishedLoading(List<Employee> pojos) {

    }

    @Override
    public void simpleNetworkResponseInt(int response) {

    }

    @Override
    public void simpleNetworkResponseString(String response) {
        L.m("Error yo!");
    }
}