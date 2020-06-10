package com.example.newcomer_io.ui.main.EventDetails;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import com.example.newcomer_io.R;
import com.example.newcomer_io.ui.main.LocationSettings.TrendingContent;
import com.example.newcomer_io.ui.main.UserDetails.EventCreate;
import com.example.newcomer_io.ui.main.UserDetails.UserData;
import com.google.firebase.database.*;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class tab1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EventCreate eventCreate;
    private TrendingContent content;
    // TODO: Rename and change types of parameters
    private Button addPostButton;

    private LinearLayout scrollView;
    private boolean flag_Button_Added;
    private Button addPost;
    private boolean clicked_like;
    private boolean isJoined;
    private View inflate;
    private LayoutInflater inflater;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public tab1(EventCreate eventCreate) {
        // Required empty public constructor
        //this.content = content;
        this.eventCreate = eventCreate;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setFlag_Button_Added(boolean val) {
        this.flag_Button_Added = val;
    }

    private boolean getFlag_Button_Added() {
        return this.flag_Button_Added;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View inflate = inflater.inflate(R.layout.fragment_tab1, container, false);
        setInflate(inflate); //Set the layout inflate so that we can referrence it later
        setInflater(inflater);
        setAddPostButton(createAddPostButton());

        scrollView = inflate.findViewById(R.id.scrollLayout); //This represents the scroll for all of the posts
        setFlag_Button_Added(false);
        setClicked_like(false);

        //This button represents the functionality to add a post to a portion of the UI.
        boolean isJoined = getEventCreate().isJoined();
        if (isJoined){
            updatePostContent(eventCreate);
        }else{
            //Ottherwise they haven't joined the group yet and display to that to the usere
            if (scrollView.getChildCount() != 0){
                scrollView.removeAllViews();
            }
            //Then we go ahead and show a message screen
            final Button joinGroup = createJoinGroupButton();
            joinGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Then we set the view to
                    EventCreate eventCreate = getEventCreate();
                    eventCreate.setJoined(true);
                    updatePostContent(eventCreate);
                    scrollView.removeView(joinGroup);

                }
            });
            scrollView.addView(joinGroup);
        }

        return inflate;
    }

    private Button createJoinGroupButton() {
        Button button = new Button(getContext());
        button.setText("Join Group");
        button.setTextSize(20f);
        button.setBackgroundResource(R.drawable.rounded_border); 
        
        try{
            Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.open_sans);
            int paddingDp = 8;
            float density = getContext().getResources().getDisplayMetrics().density;
            int paddingPixel = (int) (paddingDp * density);
            button.setTypeface(typeface);
            button.setPadding(paddingPixel, 0, paddingPixel, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        button.setLayoutParams(layoutParams);

        return button;
    }


    private Button createAddPostButton() {
        addPost = new Button(getInflate().getContext());
        addPost.setId(200);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        addPost.setLayoutParams(layoutParams);
        addPost.setText("Click to Create A Post");

        try{
            Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.open_sans);
            int paddingDp = 8;
            float density = getContext().getResources().getDisplayMetrics().density;
            layoutParams.setMargins(0,paddingDp,0,0);

            int paddingPixel = (int) (paddingDp * density);
            addPost.setTypeface(typeface);
            addPost.setPadding(paddingPixel, 0, paddingPixel, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        addPost.setBackgroundResource(R.drawable.rounded_border);
        return addPost;
    }

    private Button getButtonView() {
        return addPost;
    }

    public void updateScrollPostContentView() {

        ArrayList<EventCreate.Posts> postsArrayList = this.eventCreate.getPostsArrayList();

        this.scrollView.removeAllViews();

        if (postsArrayList.size() == 0) {
            //then it is not empty and we can add the posts in
            this.scrollView.addView(getAddPostButton());
        } else {
            //Then we display the people of have added a post in
            for (int i = 0; i < postsArrayList.size(); i++) {
                EventCreate.Posts posts = postsArrayList.get(i);
                if (posts.getPostParamsView() != null && this.scrollView != null) {
                    View postParamsView = posts.getPostParamsView();
                    if (postParamsView.getParent() != null){
                        ((ViewGroup) postParamsView.getParent()).removeView(postParamsView);
                        this.scrollView.addView(posts.getPostParamsView());

                    }else{
                        this.scrollView.addView(posts.getPostParamsView());
                    }
                    ImageView likeButton = postParamsView.findViewById(R.id.likes);
                    boolean alreadyLiked = posts.isAlreadyLiked();
                }

            }
            this.scrollView.addView(addPost);
            getAddPostButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Then we will place the add comments section
                    //Then we create a post by inflating the current view to the user
                    final View add_comment = getLayoutView(R.layout.add_comment);
                    scrollView.removeView(getAddPostButton());
                    scrollView.addView(add_comment);
                    //Send Button
                    addSendListeners(add_comment);
                    addCancelListeners(add_comment);
                }
            });


        }
    }

    private void addCancelListeners(final View add_comment) {
        ImageView cancel_button = add_comment.findViewById(R.id.cancel);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Then we erase the text and move on
                scrollView.removeView(add_comment);
                scrollView.addView(getAddPostButton());
            }
        });

    }

    public void addSendListeners(final View add_comment){
        ImageView send = add_comment.findViewById(R.id.send);
        final EditText postMessage = add_comment.findViewById(R.id.addComment);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Then we will add the comment to the scrollivew
                String messs = postMessage.getText().toString();
                //Get current date/time

                Date time = Calendar.getInstance().getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm");
                String format = simpleDateFormat.format(time);

                View individualComments = getLayoutView(R.layout.user_row);
                TextView posterName = individualComments.findViewById(R.id.name);
                TextView posterMessage = individualComments.findViewById(R.id.message);

                if (messs.equals("") == false){
                    //it is not empty then we can go ahead with addding the post to the view
                    posterMessage.setText(messs);

                    scrollView.removeView(add_comment);
                    scrollView.addView(individualComments);
                    scrollView.addView(getAddPostButton());

                    EventCreate eventCreate = getEventCreate();
                    eventCreate.addPost(posterName.getText().toString(),messs,0,0,"",Calendar.getInstance().getTime());

                    //setLikeListeners(currPost);
                    //setCommentListeners(currPost,message, name, userId, postDate_Str,comments);
                }
            }
        });
    }

    public View getLayoutView(int layoutID){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View layoutf = inflater.inflate(layoutID,null);
        return layoutf;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void updatePostContent(EventCreate eventCreate) {

        String guid = eventCreate.getGUID();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Groups/" + guid + "/Posts").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sendData_Post(dataSnapshot.getChildren());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendData_Post(Iterable<DataSnapshot> posts) {

        int postID = 1;
        for (DataSnapshot childNode : posts) {
            //Then we will get each fo the element
            final int comments = Integer.parseInt(childNode.child("Comments").getValue().toString());

            final String postDate_Str = childNode.child("Date").getValue().toString();

            final int likes = Integer.parseInt(childNode.child("Likes").getValue().toString());
            Date postDate = getDate_Str(postDate_Str);
            String userId = childNode.child("Id").getValue().toString();
            final String message = childNode.child("Message").getValue().toString();
            final String name = childNode.child("Name").getValue().toString();
            //this.eventCreate.addPost(name, message, likes, comments, userId, postDate);

            //Now we want to increment whenever there is a potential like button click
            if ( this.eventCreate.getPost_Id(userId) == null){
                this.eventCreate.addPost(name, message, likes, comments, userId, postDate);
            }else{
                //update the post ID
                this.eventCreate.setIntPostNumber(postID);
            }

            final EventCreate.Posts currPost = this.eventCreate.getPost_Id(userId);


            setLikeListeners(currPost);
            setCommentListeners(currPost,message, name, userId, postDate_Str,comments);

            postID += 1;
        }
        updateScrollPostContentView();
    }

    private void setLikeListeners(final EventCreate.Posts currPost) {

        final ImageView likeButton = currPost.getLikeButton();
        final TextView likeText = currPost.getLikeText();

        //set the onclick listener
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean alreadyLiked = currPost.isAlreadyLiked();

                if (!alreadyLiked) {

                    //Then if we get the onclick listener
                    likeButton.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.ic_thumb_up_alt_24_bluepx));
                    int num_likes = Integer.parseInt(likeText.getText().toString());
                    likeText.setText(String.valueOf(num_likes + 1));
                    currPost.setAlreadyLiked(true);
                } else {
                    //Then if we get the onclick listener
                    likeButton.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.ic_thumb_up_alt_24px));
                    int num_likes = Integer.parseInt(likeText.getText().toString());
                    likeText.setText(String.valueOf(num_likes - 1));
                    currPost.setAlreadyLiked(false);
                }
            }
        });
    }
    private void setCommentListeners(final EventCreate.Posts currPost, String s, final String name, final String message, final String postDate_Str, final int comments){

        final ImageView commentButton = currPost.getCommentButton();
        final int postNumber = currPost.getPostId();
        final TextView likeText = currPost.getLikeText();

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Thne we go tot he comments sections
                Intent intent = new Intent(getActivity(), CommentsPage.class);
                //We want to pass through the comments
                intent.putExtra("Group GUID", eventCreate.getGUID());
                intent.putExtra("Post Number", postNumber);
                intent.putExtra("Message", message);
                intent.putExtra("Post Date", postDate_Str);
                intent.putExtra("Comment Number", comments);
                intent.putExtra("Like Number", Integer.parseInt(likeText.getText().toString()));
                intent.putExtra("Post Name", name);
                intent.putExtra("Already Liked", currPost.isAlreadyLiked());

                startActivityForResult(intent,1);
            }
        });
    }

    private Date getDate_Str(String date) {
        SimpleDateFormat simpleDateFormat_Days = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date1;
        try {
            date1 = simpleDateFormat_Days.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
            date1 = null;
        }
        return date1;
    }


    public boolean isClicked_like() {
        return clicked_like;
    }

    public void setClicked_like(boolean clicked_like) {
        this.clicked_like = clicked_like;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null){

            //then we get the data and pass it to the current UI
            int comments = data.getIntExtra("Comment Number", -1);
            int likes = data.getIntExtra("Likes Number", -1);
            boolean isLiked = data.getBooleanExtra("Already Liked", false);
            int postId = data.getIntExtra("Post Number", -1);

            EventCreate.Posts posts = eventCreate.getPostsArrayList().get(postId - 1);
            posts.setPostId(postId);
            TextView likeText = posts.getLikeText();

            setClicked_like(isLiked);
            likeText.setText(String.valueOf(likes));
            posts.getComments_Txt().setText(String.valueOf(comments));

            ImageView likeButton = posts.getLikeButton();
            if (isLiked) {

                //Then if we get the onclick listener
                likeButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_thumb_up_alt_24_bluepx));
                int num_likes = Integer.parseInt(likeText.getText().toString());
                likeText.setText(String.valueOf(num_likes));

            } else {
                //Then if we get the onclick listener
                likeButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_thumb_up_alt_24px));
                int num_likes = Integer.parseInt(likeText.getText().toString());
                likeText.setText(String.valueOf(num_likes));
            }
        }
    }

    public boolean isJoined() {
        return isJoined;
    }

    public void setJoined(boolean joined) {
        isJoined = joined;
    }
    public EventCreate getEventCreate(){return this.eventCreate;}
    public void setEventCreate(EventCreate eventCreate){this.eventCreate = eventCreate;}

    public View getInflate() {
        return inflate;
    }

    public void setInflate(View inflate) {
        this.inflate = inflate;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    public void setInflater(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public Button getAddPostButton() {
        return addPostButton;
    }

    public void setAddPostButton(Button addPostButton) {
        this.addPostButton = addPostButton;
    }
}
