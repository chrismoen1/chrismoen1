package com.example.newcomer_io.ui.main.UserDetails;

import android.app.Activity;
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
import java.util.Date;

public class EventCreate {
    //This class summarizes the event that has been created in terms of its details regarding the name of the place, the location time, the location place, the people that have made a post
    //the people that have joined the group

    private String eventName;
    private Date startTime;
    private Date endTime;
    private Location location;
    private int postNumber;

    private ArrayList<Posts> postsArrayList;

    private Activity activity;
    private String eventNotes;

    public EventCreate(Activity activity, String eventName, Date startTime, Date endTime, String eventNotes){

        this.activity = activity;
        this.eventName = eventName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventNotes = eventNotes;
        this.postsArrayList = new ArrayList<Posts>();
        this.postNumber = 0;

    }
    public void addPost(String personName, String postText, LinearLayout scrollLayout_Tab1){
        this.postNumber += 1;
        Posts posts = new Posts(this.activity, personName, postText,  scrollLayout_Tab1, postNumber);
        posts.setPostText(postText);
        postsArrayList.add(posts);
        //we also want to add it to the view

    }

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

            this.postText_Txt = user_row.findViewById(R.id.textView17);
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
        String name;
        int eventsAttended;
        float eventsCommited;
        public JoinedUsers(String name, int eventsAttended, float eventsCommited){
            this.name = name;
            this.eventsAttended = eventsAttended;
            this.eventsCommited = eventsCommited;
        }
    }
}
