package com.example.newcomer_io.ui.main.EventDetails;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TrendingContent content;
    private LinearLayout scrollView;
    private boolean flag_Button_Added;
    private Button addPost;
    private boolean clicked_like;

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
        scrollView = inflate.findViewById(R.id.scrollLayout); //This represents the scroll for all of the posts
        setFlag_Button_Added(false);
        setClicked_like(false);

        //This button represents the functionality to add a post to a portion of the UI.
        updatePostContent(eventCreate, inflate, inflater);

        return inflate;
    }

    private void createButton(View inflate) {
        addPost = new Button(inflate.getContext());
        addPost.setId(200);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        addPost.setLayoutParams(layoutParams);
        addPost.setText("Click to Create A Post");
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.open_sans);
        int paddingDp = 8;
        float density = getContext().getResources().getDisplayMetrics().density;
        int paddingPixel = (int) (paddingDp * density);
        addPost.setTypeface(typeface);
        addPost.setPadding(paddingPixel, 0, paddingPixel, 0);
        addPost.setBackgroundResource(R.drawable.rounded_border);
    }

    private Button getButtonView() {
        return addPost;
    }

    public void updateScrollPostContentView(View inflate, LayoutInflater inflater) {

        ArrayList<EventCreate.Posts> postsArrayList = this.eventCreate.getPostsArrayList();
        boolean flag_button_added = getFlag_Button_Added();

        //if (this.scrollView.getChildCount() != 0) {
        this.scrollView.removeAllViews();
        //}
        if (!flag_button_added) {
            createButton(inflate);
        }

        if (postsArrayList.size() == 0) {
            //then it is not empty and we can add the posts in
            this.scrollView.addView(this.addPost);
            setFlag_Button_Added(true);
        } else {
            //Then we display the people of have added a post in
            for (int i = 0; i < postsArrayList.size(); i++) {
                EventCreate.Posts posts = postsArrayList.get(i);
                if (posts.getPostParamsView() != null && this.scrollView != null) {
                    this.scrollView.addView(posts.getPostParamsView());

                }

            }
            this.scrollView.addView(addPost);
            this.addPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Then we will place the add comments section

                }
            });


        }
    }

    public void updatePostContent(EventCreate eventCreate, final View inflate, final LayoutInflater inflater) {
        String guid = eventCreate.getGUID();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Groups/" + guid + "/Posts").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sendData_Post(dataSnapshot.getChildren(), inflate, inflater);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendData_Post(Iterable<DataSnapshot> posts, final View inflate, LayoutInflater inflater) {

        for (DataSnapshot childNode : posts) {
            //Then we will get each fo the element
            final int comments = Integer.parseInt(childNode.child("Comments").getValue().toString());

            final String postDate_Str = childNode.child("Date").getValue().toString();

            final int likes = Integer.parseInt(childNode.child("Likes").getValue().toString());
            Date postDate = getDate_Str(postDate_Str);
            String userId = childNode.child("Id").getValue().toString();
            final String message = childNode.child("Message").getValue().toString();
            final String name = childNode.child("Name").getValue().toString();
            this.eventCreate.addPost(name, message, likes, comments, userId, postDate);

            //Now we want to increment whenever there is a potential like button click
            final EventCreate.Posts currPost = this.eventCreate.getPost_Id(userId);
            final ImageView likeButton = currPost.getLikeButton();
            final TextView likeText = currPost.getLikeText();
            final ImageView commentButton = currPost.getCommentButton();
            final int postNumber = currPost.getPostId();

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
        updateScrollPostContentView(inflate, inflater);
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

}
