package com.pgmacdesign.demolinktogae.utility;

import android.content.Context;
import android.view.View;

import com.pgmacdesign.demolinktogae.misc.CustomClickInterface;
import com.pgmacdesign.demolinktogae.pojo.Employee;
import com.pgmacdesign.demolinktogae.pojo.User;

/**
 * Created by pmacdowell on 12/18/2015.
 */
public class CustomClickListener implements View.OnClickListener{

    //Main Objects
    private Context context;
    private CustomClickInterface listener;
    private Employee employee;
    private User user;

    //Tags to use
    public static final Integer EMPLOYEE_TAG = 50;
    public static final Integer USER_TAG = 51;


    /**
     * Constructor
     * @param context1 the context
     * @param listener1 listener to pass data back through
     * @param employee1 The employee clicked
     */
    public CustomClickListener(Context context1, CustomClickInterface listener1, Employee employee1){
        this.listener = listener1;
        this.context = context1;
        this.employee = employee1;
    }

    /**
     * Overloaded Constructor
     * @param context1 the context
     * @param listener1 listener to pass data back through
     * @param user1 The user clicked
     */
    public CustomClickListener(Context context1, CustomClickInterface listener1, User user1){
        this.listener = listener1;
        this.context = context1;
        this.user = user1;
    }

    /**
     * This is the master onClick section. It is used as a central hub for onClicks via their tags
     * @param v The view clicked
     */
    @Override
    public void onClick(View v) {
        //Get the tag, check it and act accordingly
        Integer tag = (Integer) v.getTag();
        if (tag != null) {
            if(tag == EMPLOYEE_TAG){

            }
            if(tag == USER_TAG){

            }
        }
    }

}
