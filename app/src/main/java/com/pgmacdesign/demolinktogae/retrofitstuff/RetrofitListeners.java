package com.pgmacdesign.demolinktogae.retrofitstuff;

import com.pgmacdesign.demolinktogae.pojo.Employee;
import com.pgmacdesign.demolinktogae.pojo.MasterObject;
import com.pgmacdesign.demolinktogae.pojo.User;

/**
 * This interface houses the listeners to be used for callbacks within the Retrofit web calls
 * Created by pmacdowell on 4/12/2016.
 */
public interface RetrofitListeners {
    public void onServerCodeLoaded(int serverCode);
    public void onUserLoaded(User user);
    public void onEmployeeLoaded(Employee employee);
    public void onMasterObjectLoaded(MasterObject masterObject);
    public void onErrorListener(String errorResponse);
}
