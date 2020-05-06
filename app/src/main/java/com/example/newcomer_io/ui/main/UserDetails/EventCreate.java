package com.example.newcomer_io.ui.main.UserDetails;

import android.app.Activity;
import android.graphics.Bitmap;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.newcomer_io.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EventCreate {
    //This class summarizes the event that has been created in terms of its details regarding the name of the place, the location time, the location place, the people that have made a post
    //the people that have joined the group

    private String eventName;
    private Date startTime;
    private Date endTime;
    private Location location;

    private int postNumber;
    private int userNumber;

    private ArrayList<Posts> postsArrayList;
    private ArrayList<JoinedUsers> joinedUsersArrayList;
    private EventDetails eventDetails;
    private Activity activity;
    private String eventNotes;
    private String locationName;
    private Bitmap photo;

    public EventCreate(Activity activity, String eventName, Date startTime, Date endTime, String eventNotes, String locationName, Bitmap photo){

        this.activity = activity;
        this.eventName = eventName;
        this.startTime = startTime;
        this.locationName = locationName;
        this.endTime = endTime;
        this.eventNotes = eventNotes;
        this.postsArrayList = new ArrayList<Posts>();
        this.postNumber = 0;
        this.userNumber = 0;
        this.photo = photo;
        this.joinedUsersArrayList = new ArrayList<JoinedUsers>();
        this.eventDetails = new EventDetails(activity,eventName,eventNotes,locationName, startTime,endTime,photo);
//        public EventDetails(Activity activity_group, String eventName, String eventNotes, String locationName, Date startTime, Date endTime, Bitmap displayPhoto){
    }
    public void addPost(String personName, String postText, LinearLayout scrollLayout_Tab1){
        this.postNumber += 1;
        Posts posts = new Posts(this.activity, personName, postText,  scrollLayout_Tab1, postNumber);
        posts.setPostText(postText);
        postsArrayList.add(posts);
        //we also want to add it to the view

    }
    public void addUser(String userName, int eventsAttended, String Location, int postNumber,LinearLayout linearLayout){
        this.userNumber += 1;
        JoinedUsers joinedUsers = new JoinedUsers(this.activity,userName, eventsAttended,Location,postNumber);
        joinedUsersArrayList.add(joinedUsers);
        View layoutView_users = joinedUsers.getLayoutView_Users();
        linearLayout.addView(layoutView_users);
    }

    public String getEventNotes(){return this.eventNotes; }
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }


    public class EventDetails{
        private Bitmap displayPhoto;
        private TextView displayTitle;
        private Activity activity_group;

        private String eventName;
        private String eventNotes;
        private String locationName;
        private Date startTime;
        private Date endTime;


        public EventDetails(Activity activity_group, String eventName, String eventNotes, String locationName, Date startTime, Date endTime, Bitmap displayPhoto){

            this.activity_group = activity_group;
            this.eventName = eventName;
            this.eventNotes = eventNotes;
            this.locationName = locationName;
            this.startTime = startTime;
            this.endTime = endTime;
            this.displayPhoto = displayPhoto;
            fillEventDetails();

        }
        private void fillEventDetails() {
            //this function will fill in all of the event details for the current page
            TextView eventTitle = this.activity_group.findViewById(R.id.eventTitle);
            LinearLayout eventNoteDetails = this.activity_group.findViewById(R.id.eventNoteDetails);
            TextView eventNotes = this.activity_group.findViewById(R.id.eventNoteDetails_Txt);
            TextView eventLocationDetails = this.activity_group.findViewById(R.id.eventLocationDetails_Txt);
            TextView eventTimeDetails_Txt = this.activity_group.findViewById(R.id.eventTimeDetails_Txt);
            ImageView displayPhoto_image = this.activity_group.findViewById(R.id.displayPhoto);

            String timeStampe_EventStart_EventStop = getTimeStamp_Start_Stop(endTime,startTime);
            eventTimeDetails_Txt.setText(timeStampe_EventStart_EventStop);
            displayPhoto_image.setImageBitmap(this.displayPhoto);
            eventTitle.setText(this.eventName);

            eventNotes.setText(this.eventNotes);

        }

        private String getTimeStamp_Start_Stop(Date endTime, Date startTime) {
            int endTime_Day = endTime.getDay();
            int startTime_Day = startTime.getDay();
            long time_diff = (endTime.getTime() - startTime.getTime());
            int diff_hours = (int) convertMilli_Hours(time_diff);

            if (startTime_Day == endTime_Day) {
                //Then we only need to show the hour
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
                String startTime_str = simpleDateFormat.format(startTime);
                String endTime_str = simpleDateFormat.format(endTime);
                return startTime_str + " to " + endTime_str;
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, MMMM dd ");
                String startTime_str = simpleDateFormat.format(startTime);
                String endTime_str = simpleDateFormat.format(endTime);

                return startTime_str + " to " + endTime_str;
            }
        }

        private long convertMilli_Hours(long diff){
            return diff / (60 * 60 * 1000);
        }
        private String getTimeStamp_TimeRemaining(Date eventStartTime, Date currentTime) {

            //SO the purpose of this function is to determine the difference in time stamps betweent he current time and the start of the event time and return a readable string
      /*
      if it is less than 24 hours (display hours + minutes)
      if it is greater than 24 hours (display days + minutes
       */
            long diff = eventStartTime.getTime() - currentTime.getTime();
            long diff_hours = convertMilli_Hours(diff);

            if ((int) diff_hours < 24){
                //Then we will display
                //if it is less than 24 hours (display hours + seconds)
                Date diff_hours_date = new Date(diff_hours);
                String hours = String.valueOf(diff_hours_date.getHours());
                String minutes = String.valueOf(diff_hours_date.getMinutes());

                return (hours + " hrs and " + minutes + " mins until"); //Return the proper value

            }
            else{

                Date diff_hours_date = new Date(diff_hours);
                String days = String.valueOf(diff_hours_date.getDay());
                String hours = String.valueOf(diff_hours_date.getHours());

                return (days + " days and " + hours + " hrs"); //Return the proper value
            }
        }
    }

    //Both these extension classes represent hte
    public class Posts{

        //This function represents the posts that occur on an individual group
        private  String personName;
        private String postText;
        private TextView postText_Txt;

        private ImageView profilePicture;

        private int likes;

        private TextView likes_Txt;
        private TextView comments_Txt;
        private TextView personName_Txt;

        private int comments;
        private int postId;
        private Date postDate;
        private TextView postDate_Txt;

        private int MAXPOSTNUMBER;
        private View user_row;

        private ConstraintLayout constraintLayout;
        // SimpleDateFormat newFormat_Day = new SimpleDateFormat("E, MMMM dd ");
        // String startTime_Str = newFormat_clock.format(startDate_Time);

        public Posts(Activity activity, String personName, String postText, LinearLayout scrollLayout_Tab1, int postId){

            this.personName = personName;
            this.postText = postText;
            this.postId = postId;

            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
            user_row = inflater.inflate(R.layout.user_row,null);

            constraintLayout = user_row.findViewById(R.id.constraintLayout);
            constraintLayout.setId(postId);

            user_row.setId(postId); //set the unique ID to be different so that it doens't get mixed up with other stuff
            scrollLayout_Tab1.addView(user_row);

            this.postText_Txt = user_row.findViewById(R.id.location);
            this.personName_Txt = user_row.findViewById(R.id.name);
            this.postDate_Txt = user_row.findViewById(R.id.textView16);

            this.likes_Txt = user_row.findViewById(R.id.textView19);
            this.comments_Txt = user_row.findViewById(R.id.textView18);

            this.MAXPOSTNUMBER = 52;

            likes_Txt.setText("0");
            comments_Txt.setText("0");
        }
        public View getLayoutView(){
            return this.user_row;
        }

        //Hey guys, I am looking forward to meeting everyone!
        public void setPostText(String post){
            if (post.length() > this.MAXPOSTNUMBER){
                //Then we will need to cut the string off after 52 to concatenate  "..."
                String conc = post.substring(0, MAXPOSTNUMBER);
                conc += "...";
                postText_Txt.setText(conc);
            }
            else{
                this.postText_Txt.setText(post);
            }
        }
        public void setPostDate(Date date){
            int day = date.getDay();
            if (day == 1){
                this.postDate_Txt.setText("1 day ago");
            }
            else if(day < 30){
                this.postDate_Txt.setText(day + " days ago");
            }
            else{
                SimpleDateFormat newFormat_Day = new SimpleDateFormat("E-MMMM-dd ");
                String startTime_Str = newFormat_Day.format(date);
                this.postDate_Txt.setText(startTime_Str);
            }
        }

        public void setLikes(int likes){

            //sets the number of likes
            this.likes = likes;
            //Also we need to set the number beside the icon
            this.likes_Txt.setText(String.valueOf(likes));
        }
        public void setComments(int comments){

            //sets the number of likes
            this.comments = comments;
            //Also we need to set the number beside the icon
            this.comments_Txt.setText(String.valueOf(comments_Txt));
        }

        public Date getPostDate() {
            return this.postDate;
        }

        public int getLikes() {
            return likes;
        }

        public int getComments() {
            return comments;
        }
    }
    public class JoinedUsers{

        private TextView userName;
        private View user_overviewer;
        private TextView events_attended;
        private TextView location;
        private TextView postNumber;
        private ConstraintLayout constraintLayout;
        public JoinedUsers(Activity currActvity, String userName, int eventsAttended, String location, int postNumber){

            LayoutInflater inflater = (LayoutInflater) currActvity.getSystemService(currActvity.LAYOUT_INFLATER_SERVICE);
            user_overviewer = inflater.inflate(R.layout.user_overview,null);

            this.constraintLayout = user_overviewer.findViewById(R.id.constraintInner);

            constraintLayout.setId(postNumber);

            this.userName = user_overviewer.findViewById(R.id.name);
            this.events_attended = user_overviewer.findViewById(R.id.eventsAttended);
            this.postNumber = user_overviewer.findViewById(R.id.eventsAttended);
            this.location = user_overviewer.findViewById(R.id.location);

            this.userName.setText(userName);
            this.events_attended.setText(String.valueOf(eventsAttended));
            this.location.setText(location);

        }

        public TextView getUserName() {
            return userName;
        }

        public void setUserName(TextView userName) {
            this.userName = userName;
        }

        public TextView getEvents_attended() {
            return events_attended;
        }

        public void setEvents_attended(TextView events_attended) {
            this.events_attended = events_attended;
        }

        public TextView getLocation() {
            return location;
        }

        public void setLocation(TextView location) {
            this.location = location;
        }

        public TextView getPostNumber() {
            return postNumber;
        }

        public void setPostNumber(TextView postNumber) {
            this.postNumber = postNumber;
        }

        public ConstraintLayout getConstraintLayout() {
            return constraintLayout;
        }

        public void setConstraintLayout(ConstraintLayout constraintLayout) {
            this.constraintLayout = constraintLayout;
        }
        public View getLayoutView_Users(){
            return this.user_overviewer;
        }
    }

}
