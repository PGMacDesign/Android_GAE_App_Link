package com.pgmacdesign.demolinktogae.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.pgmacdesign.demolinktogae.R;
import com.pgmacdesign.demolinktogae.misc.ServerCallLoadedListener;
import com.pgmacdesign.demolinktogae.pojo.Employee;
import com.pgmacdesign.demolinktogae.pojo.MasterObject;
import com.pgmacdesign.demolinktogae.pojo.User;
import com.pgmacdesign.demolinktogae.utility.MyUtilities;
import com.pgmacdesign.demolinktogae.utility.VolleySingleton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pmacdowell on 12/18/2015.
 */
public class RecyclerViewAdapterCustom<T extends MasterObject> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<T> mListObjects = new ArrayList<>();
    private LayoutInflater mInflater;
    private VolleySingleton mVolleySingleton;
    private ImageLoader mImageLoader;
    //keep track of the previous position for animations where scrolling down
    private int mPreviousPosition = 0;
    private MyUtilities.pojoObjects type;
    private Context context;

    //Link to activity
    private ServerCallLoadedListener listener;

    /**
     * Constructor
     * @param context Context (Needed for some calls)
     * @param listener1 Server Call Loaded Listener to respond to
     */
    public RecyclerViewAdapterCustom(Context context, ServerCallLoadedListener listener1,
                                     MyUtilities.pojoObjects type1) {
        mInflater = LayoutInflater.from(context);
        mVolleySingleton = VolleySingleton.getInstance();
        mImageLoader = mVolleySingleton.getImageLoader();
        this.listener = listener1;
        this.context = context;
        this.type = type1;
    }

    public void setData(List<T> mListObjects){
        this.mListObjects = mListObjects;
        notifyDataSetChanged();
    }

    /**
     * Constructs a ContestsCustomViewHolder object. All types share the same layout
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.employee_recycler_tile, parent, false);
        EmployeeHolder viewHolder = new EmployeeHolder(view);
        return viewHolder;
    }

    /**
     * Manages all the drawerData to be inserted into the recyclerview.
     * @param holder The view holder being used (Of the layouts)
     * @param position The position in the list being traversed (IE 0, 1, 2, 3)
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //Check position/ type first and cast the Holder
        switch (type){

            case EMPLOYEE:
                //Cast the  holder
                EmployeeHolder holder1 = (EmployeeHolder) holder;

                //Get the current Employee
                Employee employee = (Employee) mListObjects.get(position);

                //Extract data to set
                String firstName, lastName, fullName, convertedDate;
                Boolean attendedHRTraining;
                Date hireDate;
                byte[] photo;

                firstName = employee.getFirstName();
                lastName = employee.getLastName();
                attendedHRTraining = employee.getAttendedHrTraining();
                hireDate = employee.getHireDate();
                photo = employee.getPicture();

                //First let's combine first and last name
                if(firstName != null){
                    if(lastName != null){
                        fullName = firstName + " " + lastName;
                    } else {
                        //No last name
                        fullName = firstName;
                    }
                } else {
                    if(lastName != null){
                        fullName = lastName;
                    } else {
                        //Both first and last are null!
                        fullName = "No Name";
                    }
                }

                //Next, have they attended HR training
                if(attendedHRTraining == null){
                    //Now it will either be true or false
                    attendedHRTraining = false;
                }

                //Next Hire Date
                if(hireDate != null) {
                    String dateFormat = "yyyy/MM/dd";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
                    try {
                        convertedDate = simpleDateFormat.format(hireDate);
                    } catch (Exception e){
                        convertedDate = ""; //In case parsing fails
                    }
                } else {
                    convertedDate = ""; //In case no date retrieved
                }

                //Set UI Fields with data
                holder1.employee_recycler_name.setText(fullName);
                if(attendedHRTraining){
                    holder1.employee_recycler_hr_training.setText("Yes");
                    holder1.employee_recycler_hr_training.setTextColor(
                            context.getResources().getColor(R.color.Green));
                } else {
                    holder1.employee_recycler_hr_training.setText("No");
                    holder1.employee_recycler_hr_training.setTextColor(
                            context.getResources().getColor(R.color.Red));
                }
                holder1.employee_recycler_hire_date.setText(convertedDate);

                //Lastly, set the photo
                boolean succeeded = false;
                try {
                    if(photo != null){
                        if(photo.length > 0){
                            Bitmap bitmap = null;
                            try {
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length, options); //Convert bytearray to bitmap
                                //for performance free the memory allocated by the bytearray
                                photo = null;
                                if (bitmap != null) {
                                    //Bitmap completed, set to image
                                    holder1.employee_recycler_picture.setImageBitmap(bitmap);
                                    succeeded = true;
                                }

                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }
                if(!succeeded){
                    holder1.employee_recycler_picture.setImageResource(R.mipmap.ic_launcher);
                }

                break;

            case USER:
                //Cast the holder
                //UserHolder holder2 = (UserHolder) holder;

                //Get the current User
                User user = (User) mListObjects.get(position);

                break;

            case EMPLOYEE_LIST:
                break;
            case USER_LIST:
                break;
            default:
                return;
        }

        if (position > mPreviousPosition) {
            //Animate Here
        } else {
            //Animate Here
        }
        mPreviousPosition = position;

    }


    /**
     * @return Returns the size of the list of Passed items
     */
    @Override
    public int getItemCount() {
        return mListObjects.size();
    }

    /**
     * Currently have this setup to determine type and change layout returned
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        //We would adjust here for types
        return super.getItemViewType(position);
    }

    /**
     * Custom view holder class.
     */
    class EmployeeHolder extends RecyclerView.ViewHolder {
        //UI Components of the contact_individual xml file.
        RelativeLayout employee_recycler_top_layout;
        ImageView employee_recycler_picture;
        TextView employee_recycler_name, employee_recycler_hire_date, employee_recycler_hr_training;

        //Constructor
        public EmployeeHolder(View itemView) {
            super(itemView);
            //Define UI Components
            employee_recycler_top_layout = (RelativeLayout) itemView.findViewById(R.id.employee_recycler_top_layout);
            employee_recycler_picture = (ImageView) itemView.findViewById(R.id.employee_recycler_picture);
            employee_recycler_name = (TextView) itemView.findViewById(R.id.employee_recycler_name);
            employee_recycler_hire_date = (TextView) itemView.findViewById(R.id.employee_recycler_hire_date);
            employee_recycler_hr_training = (TextView) itemView.findViewById(R.id.employee_recycler_hr_training);
        }
    }

}
