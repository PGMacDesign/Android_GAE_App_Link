package com.pgmacdesign.demolinktogae.activities;

import android.content.Intent;
import android.net.Uri;
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
import com.pgmacdesign.demolinktogae.misc.SharedPrefs;
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
    private EditText splash_first_name, splash_last_name, splash_pw;
    private Button splash_button;
    private TextView splash_halp;

    //https://com-pgmacdesign-androidtest2.appspot.com/_ah/api/testendpoint/v1/checkUserData
    //https://com-pgmacdesign-androidtest2.appspot.com/_ah/api/testendpoint/v1/updateEmployee
    //Misc
    private static final String tutorialURL = "https://pgmacdesign.wordpress.com/";
    private static final String signinURL = "https://com-pgmacdesign-androidtest2.appspot.com/_ah/api/testendpoint/v1/checkUserData";
    private static final String updateEmployeeURL = "https://com-pgmacdesign-androidtest2.appspot.com/_ah/api/testendpoint/v1/updateEmployee";

    private static final String sessionId = "ah5zfmNvbS1wZ21hY2Rlc2lnbi1hbmRyb2lkdGVzdDJyDgsSBVRva2VuIgM3OTEM";
    private static final Long testId = 4755868826468352L; //Should be lastName 94

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initialize();
        setDefaults();

        String sessionId = SharedPrefs.getString("sessionId", null);
        if(sessionId != null){
            Intent intent = new Intent(this,MainActivity.class);
            this.startActivity(intent);
            this.finish();
        }

        //TESTING
        User user = new User();
        user.setFirstName("patrick");
        user.setLastName("MacDowell");
        user.setPassword("password123");
        MyUtilities.SendNetworkRequest sendNetworkRequest = new MyUtilities.SendNetworkRequest(
                this, MyUtilities.pojoObjects.USER, signinURL, user);
        Void[] param = null;
        //sendNetworkRequest.execute(param);


        //OTHER TESTING
        Employee emp = new Employee();
        emp.setId(testId);
        emp.setFirstName("TESTING");
        emp.setLastName("CHANGED LAST NAME");
        emp.setAttendedHrTraining(true);
        emp.setSessionId(sessionId);
        MyUtilities.SendNetworkRequest sendNetworkRequest2 = new MyUtilities.SendNetworkRequest(
                this, MyUtilities.pojoObjects.EMPLOYEE, updateEmployeeURL, emp);
        Void[] param2 = null;
        //sendNetworkRequest2.execute(param2);


        //Other other testing

        //END TESTING
    }

    /**
     * Initializes variables and sets Listeners
     */
    private void initialize(){

        //UI
        splash_progress_view = (RelativeLayout) this.findViewById(R.id.splash_progress_view);
        splash_loading_bar = (ProgressBar) this.findViewById(R.id.splash_loading_bar);
        splash_first_name = (EditText) this.findViewById(R.id.splash_first_name);
        splash_last_name = (EditText) this.findViewById(R.id.splash_last_name);
        splash_pw = (EditText) this.findViewById(R.id.splash_pw);
        splash_button = (Button) this.findViewById(R.id.splash_button);
        splash_halp = (TextView) this.findViewById(R.id.splash_halp);

        //Click Listeners
        splash_button.setOnClickListener(this);
        splash_halp.setOnClickListener(this);

        //Text listeners
        splash_first_name.addTextChangedListener(this);
        splash_last_name.addTextChangedListener(this);
        splash_pw.addTextChangedListener(this);


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

    /**
     * Manages the onClick events
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.splash_button:
                //Set the progress view visibility and to start spinning
                //Also hides the buttons so they cannot be pressed during loading
                spinProgressWheel(true);

                //Hide the keyboard
                MyUtilities.hideTheKeyboard(this);

                //Build our user object and Make our network call to Server
                User user = new User();

                /*
                We can safely assume these are not null here as the text listener would not let
                the button be clickable (enabled would be false) and the user could not get here
                 */
                String fName = splash_first_name.getText().toString();
                String lName = splash_last_name.getText().toString();
                String password = splash_pw.getText().toString();

                //Trim the strings to get rid of whitespace
                fName = fName.trim();
                lName = lName.trim();
                password = password.trim();

                //Set them to the object
                user.setFirstName(fName);
                user.setLastName(lName);
                user.setPassword(password);

                //Build a new Async object here, setup the params and pass it
                MyUtilities.SendNetworkRequest sendNetworkRequest = new MyUtilities.SendNetworkRequest(
                        this, MyUtilities.pojoObjects.USER, signinURL, user);
                /*
                NOTE: To be honest, I am unsure why this was erroring out, but normally you do NOT
                need to pass anything into the execute() method call; for whatever reason this was
                crashing every time and this solved the problem. If yours breaks, try removing
                the passed array and see if that solves it.
                 */
                Void[] param = null;
                sendNetworkRequest.execute(param);

                break;
            case R.id.splash_halp:
                //Open up a page to the url of the tutorial here, that way they can get up to speed
                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                intent2.setData(Uri.parse(tutorialURL));
                //So that it will reset back to this after back button clicked
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                startActivity(intent2);

                break;
        }
    }

    /**
     * This class will be used for spinning the progress wheel, displaying the semi-transparent
     * overlay screen, and then dismissing both.
     * @param bool Boolean, if true, it will START the spinning process, if false, it will stop
     */
    private void spinProgressWheel(boolean bool){
        if(bool){
            try {
                //Progress View
                splash_progress_view.setVisibility(View.VISIBLE);
                splash_progress_view.bringToFront();
                splash_loading_bar.setVisibility(View.VISIBLE);
                splash_loading_bar.setIndeterminate(true);
                splash_loading_bar.setBackgroundColor(this.getResources().getColor(R.color.Semi_Transparent2));
                splash_loading_bar.bringToFront();

                //Buttons and clickables
                splash_halp.setEnabled(false);
                splash_button.setEnabled(false);

            } catch (Exception e){
                e.printStackTrace();
            }
        } else {
            try {
                //Progress View
                splash_loading_bar.setIndeterminate(false);
                splash_progress_view.setVisibility(View.GONE);
                splash_loading_bar.setVisibility(View.GONE);

                //Buttons and clickables
                splash_halp.setEnabled(true);
                if(validateEditTexts()) {
                    splash_button.setEnabled(true);
                }

            } catch (Exception e){
                e.printStackTrace();
            }
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
        String firstNameEditText = null;
        String lastNameEditText = null;
        String usernamePassword = null;

        try {
            //Get the String from the Edit Text Fields
            firstNameEditText = splash_first_name.getText().toString();
            lastNameEditText = splash_last_name.getText().toString();
            usernamePassword = splash_pw.getText().toString();
        } catch (NullPointerException e){
            //This will only be caught in the event that the ETs are null, in which case, false
            return false;
        } catch (Exception e){} //Insurance policy

        //Check for null first
        if(firstNameEditText == null || lastNameEditText == null || usernamePassword == null){
            return false;
        }

        //If they are both of length > 0, good to go
        if(firstNameEditText.length() > 0 && lastNameEditText.length() > 0 && usernamePassword.length() > 0){
            return true;
        }

        //Return false by default in case everything else makes it past (IE 1 is length > 0 but not the other
        return false;
    }

    @Override
    public void userFinishedLoading(User pojo) {
        //Stop the progress wheel from spinning here as the call has ended
        spinProgressWheel(false);
        //If the returned object is null, return null
        if(pojo == null){
            return;
        }
        //Get the sessionId as this what we signed in for.
        String sessionID = pojo.getSessionId();

        if(sessionID != null){
            //Put the session ID into shared prefs and then continue on
            SharedPrefs.save("sessionId", sessionID);
            Intent intent = new Intent(this,MainActivity.class);
            this.startActivity(intent);
            this.finish();
        }
    }

    @Override
    public void usersFinishedLoading(List<User> pojos) {
        spinProgressWheel(false);
        //
    }

    @Override
    public void employeeFinishedLoading(Employee pojo) {
        spinProgressWheel(false);
        //
    }

    @Override
    public void employeesFinishedLoading(List<Employee> pojos) {
        spinProgressWheel(false);
        L.toast(this, "POJO SIZE = " + pojos.size());

    }

    @Override
    public void simpleNetworkResponseInt(int response) {
        spinProgressWheel(false);
        //
    }

    @Override
    public void simpleNetworkResponseString(String response) {
        spinProgressWheel(false);
        //
    }
}