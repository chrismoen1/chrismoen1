package com.example.newcomer_io.ui.main.JoinGroup;

import android.app.FragmentManager;
import android.content.Intent;
import android.util.Pair;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JoinGroup extends AppCompatActivity {

    private UserData userData;
    private Object availableGroups;
    private ArrayList<Pair> arrayList;
    private LinearLayout rowHolder;
    private LinearLayout nestedScrollLayout;
    private ImageView refresh;
    private androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
        // 1. Call the cloud
        userData = (UserData) getApplicationContext();
        arrayList = new ArrayList<>();
        nestedScrollLayout = findViewById(R.id.nestedScrollLayout);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Join a Group");
        setSupportActionBar(toolbar);

        //userData.setUUID();setAvailableGroups(availableGroups);

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
        getNestedScrollLayout().addView(getRowHolder());

        int orientation = 1;
        for (int i=0; i < data1.length;i++){

            final String GUID = data1[i];
            final int position = i;

            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
            final View group_join = inflater.inflate(R.layout.group_join, null);


            group_join.setId(i);

            final TextView eventName = group_join.findViewById(R.id.eventName);
            final TextView eventLocation = group_join.findViewById(R.id.eventLocationDetails_Txt);
            final TextView eventSize = group_join.findViewById(R.id.eventNoteDetails_Txt);

            Button joinGroup = group_join.findViewById(R.id.joinGroup2);

            mDatabase.child("Groups/"+GUID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //Then we use the datasnapshot to implement the UI changs
                    addElement(GUID,dataSnapshot);
                    if (dataSnapshot.getValue() != null) {
                        try {
                            String eventName_Str = dataSnapshot.child("Event Details").child("Event Name").getValue().toString();
                            int groupSize = Integer.parseInt(dataSnapshot.child("Event Details").child("Group Size").getValue().toString());
                            int joined = (int) dataSnapshot.child("Joined").getChildrenCount();
                            String location_Str = dataSnapshot.child("Event Details").child("Location").child("Name").getValue().toString();

                            eventName.setText(eventName_Str);
                            eventLocation.setText(location_Str);
                            eventSize.setText(String.valueOf(joined) + "/" + String.valueOf(groupSize) + " have joined");

                            addView(group_join, position);
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
                    Intent intent = new Intent(getApplicationContext(), GroupConfirmation.class);
                    intent.putExtra("GUID", GUID);
                    startActivity(intent);

                }
            });



        }

    }
    public void addView(View view, int position){
        //Then we will go through and set the view based on positions
        if (getRowHolder().getChildCount() != 0){
            //Then we add to the view and then create a new one
            getRowHolder().addView(view);
            getRowHolder().addView(createSpacer());
            creatLayoutCard();
            getNestedScrollLayout().addView(getRowHolder());

        }else{
            getRowHolder().addView(createSpacer());
            getRowHolder().addView(view);
            getRowHolder().addView(createSpacer());
        }

    }

    private void creatLayoutCard() {
        //Holder for the row cards
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(layoutParams);
        setRowHolder(linearLayout);
    }

    private Space createSpacer(){
        Space space = new Space(getApplicationContext());
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f
        );
        space.setLayoutParams(param);
        return space;
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

    public LinearLayout getNestedScrollLayout() {
        return nestedScrollLayout;
    }

    public void setNestedScrollLayout(LinearLayout nestedScrollLayout) {
        this.nestedScrollLayout = nestedScrollLayout;
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

}
