package com.example.newcomer_io.ui.main.JoinGroup;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import com.example.newcomer_io.R;
import com.example.newcomer_io.ui.main.EventDetails.FilterView;
import com.example.newcomer_io.ui.main.EventDetails.GroupConfirmation;
import com.example.newcomer_io.ui.main.FilterFragment;
import com.example.newcomer_io.ui.main.GroupTiming.CalendarDialogFragment;
import com.example.newcomer_io.ui.main.UserDetails.UserData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.*;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JoinGroup extends AppCompatActivity {

    private UserData userData;
    private Object availableGroups;
    private LinearLayout layoutRow;
    private LinearLayout layoutContainer;

    private ArrayList<Pair> arrayList;
    private ArrayList<String> tags;
    private LinearLayout rowHolder;
    private ImageView refresh;
    private androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
        // 1. Call the cloud
        userData = (UserData) getApplicationContext();
        arrayList = new ArrayList<>();

        layoutContainer = findViewById(R.id.layoutContainer);
        tags = new ArrayList<String>();

        toolbar = findViewById(R.id.toolbar1);
        toolbar.setTitle("Join a Group");

        setSupportActionBar(toolbar);

        //userData.setUUID();

        setAvailableGroups(availableGroups);
        getAvailbleGroups(45.3873,-75.7346);

    }

    public Object getAvailableGroups() {
        return availableGroups;
    }

    public void setAvailableGroups(Object availableGroups) {
        this.availableGroups = availableGroups;
    }

    public void getAvailbleGroups(double lat, double lon) {
        FirebaseFunctions instance = FirebaseFunctions.getInstance();
        // Create the arguments to the callable function.
        final Map<String, Object> data = new HashMap<>();

        data.put("lat", lat);
        data.put("lon", lon);

        instance.getHttpsCallable("getNearestGroups").call(data).addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
            @Override
            public void onSuccess(HttpsCallableResult httpsCallableResult) {
                String data1 = (String) httpsCallableResult.getData().toString();
                String replace = data1.replace("[", "");
                String replace1 = replace.replace("]", "");
                String replace2 = replace1.replace(" ", "");
                String[] split = replace2.split(",");
                updateGroupsView(split);
            }
        });

    }

    private void updateGroupsView(final String[] data1) {
        //Update the main UI

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        creatLayoutCard();
        //getNestedScrollLayout().addView(getRowHolder());

        int orientation = 1;

        for (int i=0; i < data1.length;i++){

            final String GUID = data1[i];

            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
            final View group_join = inflater.inflate(R.layout.group_join, null);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            int width = displayMetrics.widthPixels;

            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            group_join.setLayoutParams(layoutParams);
            group_join.setId(i);

            final ArrayList<String> tags_List = new ArrayList<String>();
            final TextView eventName = group_join.findViewById(R.id.eventName);
            final TextView eventNotes = group_join.findViewById(R.id.eventNotes);
            final TextView eventLocation = group_join.findViewById(R.id.eventLocationDetails_Txt);
            final TextView eventSize = group_join.findViewById(R.id.eventNoteDetails_Txt);
            final TextView subjectName = group_join.findViewById(R.id.eventNoteDetails_Txt2);
            final LinearLayout groupTags = group_join.findViewById(R.id.groupTags);
            final TextView eventTiming = group_join.findViewById(R.id.eventTiming_Txt);

            Button joinGroup = group_join.findViewById(R.id.joinGroup2);

            mDatabase.child("Groups/"+GUID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //LinearLayout layoutContainer = getLayoutContainer();
                    /*if (layoutContainer.getChildCount() ==  0){
                        //remove all of the layout containers
                        layoutContainer.removeAllViews();
                    }*/
                    //Then we use the datasnapshot to implement the UI changs
                    addElement(GUID,dataSnapshot);
                    if (dataSnapshot.getValue() != null) {
                        try {
                            String eventName_Str = dataSnapshot.child("Event Details").child("Event Name").getValue().toString();
                            String subjectType_Str = dataSnapshot.child("Event Details").child("Subject").getValue().toString();
                            String eventNotes_Str = dataSnapshot.child("Event Details").child("Event Notes").getValue().toString();

                            boolean allDay = convert_AllDay(dataSnapshot.child("Event Details").child("Timing").child("All Day").getValue().toString());
                            String startTime_Str = dataSnapshot.child("Event Details").child("Timing").child("Start Time").getValue().toString();
                            String endTime_Str = dataSnapshot.child("Event Details").child("Timing").child("End Time").getValue().toString();
                            String displayTime = getDisplayTime(startTime_Str,endTime_Str,allDay);
                            eventTiming.setText(displayTime);
                            //TextView subjectType_Txt = createSubjectTypeView(subjectType);

                            LinearLayout groupTagsLayout = createGroupTagsLayout(dataSnapshot.child("Event Details").child("Type").getChildren(),tags_List);

                            //groupTags.addView(subjectType_Txt);
                            if (groupTags.getChildCount() == 0){
                                groupTags.addView(createSpacer("HORIZONTAL"));
                                groupTags.addView(groupTagsLayout);
                            }else{
                                groupTags.removeAllViews();
                                groupTags.addView(createSpacer("HORIZONTAL"));
                                groupTags.addView(groupTagsLayout);
                            }

                            subjectName.setText(subjectType_Str);

                            //groupTags.addView(createCheckMarksLayout("Test"));

                            int groupSize = Integer.parseInt(dataSnapshot.child("Event Details").child("Group Size").getValue().toString());
                            int joined = (int) dataSnapshot.child("Joined").getChildrenCount();
                            String location_Str = dataSnapshot.child("Event Details").child("Location").child("Name").getValue().toString();

                            //Setting the pararamaters in the text field to indicate the values for various fields
                            eventName.setText(eventName_Str);
                            eventLocation.setText(location_Str);
                            eventSize.setText(String.valueOf(joined) + "/" + String.valueOf(groupSize) + " have joined");
                            eventNotes.setText(eventNotes_Str);


                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            joinGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Then we will go to the group confirmation page

                    //Considering all of the information that we need
                    //1.

                    Intent intent = new Intent(getApplicationContext(), GroupConfirmation.class);
                    intent.putStringArrayListExtra("Subject Tags", tags_List);
                    intent.putExtra("GUID", GUID);
                    intent.putExtra("Event Location", eventLocation.getText().toString());
                    intent.putExtra("Event Name", eventName.getText().toString());
                    intent.putExtra("Event Notes",eventNotes.getText().toString());
                    intent.putExtra("Subject", subjectName.getText().toString());
                    intent.putExtra("Event Time",eventTiming.getText().toString());

                    startActivity(intent);

                }
            });
            addLayoutView(group_join);

        }
    }

    private boolean convert_AllDay(String toString) {
        if (toString.equals("True")){
            return true;
        }else{
            return false;
        }
    }

    private String getDisplayTime(String startTime_str, String endTime_str, boolean allDay) {
        //Then we will go through and make sure that the strign is configured as correctly to display in a date format
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat clock_format_Time = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat clock_format_Days = new SimpleDateFormat("E, MMMM dd");

        try{
            Date parse_Start = simpleDateFormat.parse(startTime_str);
            Date parse_End = simpleDateFormat.parse(endTime_str);

            String startTime_Timing = clock_format_Time.format(parse_Start); 
            String endTime_Timing = clock_format_Time.format(parse_End);
            
            String startTime_Days = clock_format_Days.format(parse_Start); 
            String endTime_Days = clock_format_Days.format(parse_End); 

            if (allDay){
                return startTime_Days  + " (All day)";
            }
            else if (startTime_Days.equals(endTime_Days) == true){
                //Then we just display the 
                String ret = startTime_Days + ":\n " + startTime_Timing + " to " + endTime_Timing;
                return ret; 
            }else {
                String ret = startTime_Days + " to " + endTime_Days + ":\n " + startTime_Timing + " to " + endTime_Timing;
                return ret; 
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Then we will display that the timing to the user
        return "";
    }
/*

    private CardView createCardLayout(){
        //This will create the card layout view used to display the current group tot he user
        CardView cardView = new CardView(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;

        CardView.LayoutParams layoutParams = new ViewGroup.LayoutParams((width/2) - dip2px(this,5),
                CardView.LayoutParams.WRAP_CONTENT);
        cardView.setLayoutParams(layoutParams);


    }
*/

    private void addLayoutView(View group_join) {
        LinearLayout layoutContainer = getLayoutContainer();

            //Then we create the row and add it the table layout
         layoutContainer.addView(group_join);

    }

    private LinearLayout createGroupTagsLayout(Iterable<DataSnapshot> children, ArrayList<String> tags) {

        LinearLayout viewHolder = new LinearLayout(this);
        viewHolder.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (DataSnapshot childs : children){

            //Then we iterate and get each tag
            String typeName = childs.getValue().toString();
            if (tags.contains(typeName) == false){
                tags.add(typeName);
            }
            LinearLayout checkMarksLayout = createCheckMarksLayout(typeName);
            viewHolder.addView(checkMarksLayout);

        }
        return viewHolder;
    }

    private LinearLayout createCheckMarksLayout(String typeName){

        LinearLayout container = new LinearLayout(this);
        LinearLayout.LayoutParams container_Parms = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setLayoutParams(container_Parms);

        int x = dip2px(this, 10);
        int y = dip2px(this, 10);

        LinearLayout.LayoutParams checkMarkBoxes_Parms = new LinearLayout.LayoutParams(x,y);
        //Create the checkbox which will go beside the text
        ImageView checkBox = new ImageView(this);
        checkBox.setBackgroundResource(R.drawable.checkicon);
        checkBox.setLayoutParams(checkMarkBoxes_Parms);

        TextView typeName_Txt = new TextView(this);
        typeName_Txt.setMaxLines(3);
        typeName_Txt.setTextSize(10f); 
        LinearLayout.LayoutParams textDisplay_Parms = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textDisplay_Parms.setMargins(dip2px(this,2),0,0,0);
        typeName_Txt.setText(typeName);
        typeName_Txt.setLayoutParams(textDisplay_Parms);

        //Add it to the views
        container.addView(checkBox);
        container.addView(typeName_Txt);

        return container;
    }
/*
    private TextView createSubjectTypeView(String subjectType) {
        int tagNum = 60;
        TextView textView = new TextView(getApplicationContext());
        textView.setText(subjectType);
        textView.setId(tagNum);


        LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        textView.setLayoutParams(layoutParams);
        int i = dip2px(this, 4f);
        layoutParams.setMargins(0,i,0,0);
        textView.setPadding(i,i,i,i);

        return textView;
    }*/

/*    public void addView(View view, int position){
        //Then we will go through and set the view based on positions
        if (getRowHolder().getChildCount() != 0){
            //Then we add to the view and then create a new one
            getRowHolder().addView(view);
            //getRowHolder().addView(createSpacer("HORIZONTAL"));
            creatLayoutCard();
            getNestedScrollLayout().addView(getRowHolder());

        }else{
            //getRowHolder().addView(createSpacer());
            getRowHolder().addView(view);
            //getRowHolder().addView(createSpacer());
        }

    }*/

    private void creatLayoutCard() {
        //Holder for the row cards
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(layoutParams);
        setRowHolder(linearLayout);
    }

    private Space createSpacer(String orientation){
        if (orientation.equals("HORIZONTAL") == true){
            Space space = new Space(getApplicationContext());
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            space.setLayoutParams(param);
            return space;

        }else{
            Space space = new Space(getApplicationContext());
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            );
            space.setLayoutParams(param);
            return space;
        }
    }
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public ArrayList<Pair> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<Pair> arrayList) {
        this.arrayList = arrayList;
    }
    public void addElement(String guid, DataSnapshot dataSnapshot){
        Pair pair = new Pair(guid,dataSnapshot);
        getArrayList().add(pair);
    }

    public LinearLayout getRowHolder() {
        return rowHolder;
    }

    public void setRowHolder(LinearLayout rowHolder) {
        this.rowHolder = rowHolder;
    }

    public ImageView getRefresh() {
        return refresh;
    }

    public void setRefresh(ImageView refresh) {
        this.refresh = refresh;
    }

    public androidx.appcompat.widget.Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(androidx.appcompat.widget.Toolbar toolbar) {
        this.toolbar = toolbar;
    }
    // Code from Step 4 here ...

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_main_setting) {
            Intent intent = new Intent(this, FilterView.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public LinearLayout getLayoutContainer() {
        return layoutContainer;
    }

    public void setLayoutContainer(LinearLayout layoutContainer) {
        this.layoutContainer = layoutContainer;
    }

    public LinearLayout getLayoutRow() {
        return layoutRow;
    }

    public void setLayoutRow(LinearLayout layoutRow) {
        this.layoutRow = layoutRow;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
}
