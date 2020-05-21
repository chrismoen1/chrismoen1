package com.example.newcomer_io.ui.main.EventDetails;

import android.content.Intent;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import com.example.newcomer_io.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.*;
import org.w3c.dom.Text;

public class CommentsPage extends AppCompatActivity {
    private String GUID;
    private String message;
    private ImageView likeButton;
    private boolean alreadyLiked;
    private int postNumber;
    private Button addPost;
    private FloatingActionButton backReply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_page);
        Intent intent = getIntent();
        String guid = intent.getStringExtra("Group GUID");

        //onReturn = (OnReturn) this;

        backReply = findViewById(R.id.backGutton);
        postNumber = intent.getIntExtra("Post Number", -1);
        int likes = intent.getIntExtra("Like Number",-1);
        final int comments = intent.getIntExtra("Comment Number", -1);
        String message = intent.getStringExtra("Message");
        String postDate = intent.getStringExtra("Post Date");
        String postName = intent.getStringExtra("Post Name");
        setAlreadyLiked(intent.getBooleanExtra("Already Liked",false));

        likeButton = findViewById(R.id.likeButton);
        TextView posterName = findViewById(R.id.userName);
        TextView postMessage = findViewById(R.id.postMessage);
        TextView postDay = findViewById(R.id.date);
        final TextView likeNum = findViewById(R.id.likeNumber);
        final TextView commentNum = findViewById(R.id.commentNumber);

        posterName.setText(postName);
        likeNum.setText(String.valueOf(likes));
        commentNum.setText(String.valueOf(comments));
        createButton();
        setGUID(guid);
        setPostNumber(postNumber);
        postDay.setText(postDate.split(" ")[0]);
        setMessage(message);
        postMessage.setText(message);
        fillIndividualComments();

        if (isAlreadyLiked()){
            //Then
            likeButton.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_thumb_up_alt_24_bluepx));
        }else{
            likeButton.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_thumb_up_alt_24px));

        }

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //now we set hte color to blue and then updat hte database
                boolean alreadyLiked = isAlreadyLiked();

                if (!alreadyLiked) {

                    //Then if we get the onclick listener
                    likeButton.setImageDrawable(ContextCompat.getDrawable(v.getContext(),R.drawable.ic_thumb_up_alt_24_bluepx));
                    int num_likes = Integer.parseInt(likeNum.getText().toString());
                    likeNum.setText(String.valueOf(num_likes + 1));
                    setAlreadyLiked(true);
                }
                else{
                    //Then if we get the onclick listener
                    likeButton.setImageDrawable(ContextCompat.getDrawable(v.getContext(),R.drawable.ic_thumb_up_alt_24px));
                    int num_likes = Integer.parseInt(likeNum.getText().toString());
                    likeNum.setText(String.valueOf(num_likes - 1));
                    setAlreadyLiked(false);
                }
            }
        });

        backReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Then we go back
                //At the same time we will send the like information back to the page
                Intent intent1 = new Intent(CommentsPage.this, PageAdapter.class);

                //Comments,likes
                //Post updat to the database
                intent1.putExtra("Comment Number", Integer.parseInt(commentNum.getText().toString()));
                intent1.putExtra("Likes Number", Integer.parseInt(likeNum.getText().toString()));
                intent1.putExtra("Already Liked", isAlreadyLiked());
                intent1.putExtra("Post Number", getPostNumber());
                setResult(1,intent1);
                finish();
            }
        });

    }
    private void setMessage(String message) {
        this.message = message;
    }
    private String getMessage(){return this.message; }

    private void fillIndividualComments() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference child = mDatabase.child("Groups/" + getGUID() + "/Reply/" + String.valueOf(getPostNumber()));

        child.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //then we will go through and upate the UI with the correct information regarding the post stuff
                final LinearLayout scrollView = findViewById(R.id.scrollView);
                int top_bottomMargin_dp = getPaddingPixel(4);

                scrollView.setPadding(0,top_bottomMargin_dp,0,top_bottomMargin_dp);
                if (scrollView.getChildCount() != 0){
                    scrollView.removeAllViews();
                }
                int i = 1;
                for (DataSnapshot childNode : dataSnapshot.getChildren()){
                    String date = childNode.child("Date").getValue().toString();
                    String name = childNode.child("Name").getValue().toString();
                    String reply_message = childNode.child("Reply Message").getValue().toString();

                    LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);

                    View individualComments = inflater.inflate(R.layout.individual_comment,null);
                    individualComments.setId(i);

                    //Now we fill in all of the necessary informatino pertaining to the user's comment
                    TextView userName = individualComments.findViewById(R.id.name);
                    TextView message = individualComments.findViewById(R.id.message);
                    TextView postDate = individualComments.findViewById(R.id.postDate);

                    postDate.setText(date);
                    userName.setText(name);
                    message.setText(reply_message);
                    scrollView.addView(individualComments);
                    i += 1;

                }
                scrollView.addView(getCurrButton());
                getCurrButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Then we also want to see when there is an actual button click
                        //Then we remove the button
                        final View addComment_View = getComment_View();
                        scrollView.removeView(getCurrButton());
                        scrollView.addView(addComment_View);
                        ImageView send = addComment_View.findViewById(R.id.send);
                        ImageView cancel = addComment_View.findViewById(R.id.cancel);

                        final EditText postMessage = addComment_View.findViewById(R.id.addComment);

                        send.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Then we will add the comment to the scrollivew
                                String messs = postMessage.getText().toString();
                                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);

                                View individualComments = inflater.inflate(R.layout.individual_comment,null);
                                TextView theMessage = individualComments.findViewById(R.id.message);
                                theMessage.setText(messs);

                                individualComments.setId(getPostNumber());

                                scrollView.removeView(addComment_View);
                                scrollView.addView(individualComments);
                                scrollView.addView(getCurrButton());
                            }
                        });
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Then we erase the text and move on
                                scrollView.removeView(addComment_View);
                                scrollView.addView(getCurrButton());
                            }
                        });

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private View getComment_View() {
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);

        View individualComments = inflater.inflate(R.layout.add_comment,null);
        return individualComments;

    }


    public String getGUID() {
        return GUID;
    }

    public void setGUID(String GUID) {
        this.GUID = GUID;
    }

    public int getPostNumber() {
        return postNumber;
    }

    public void setPostNumber(int postNumber) {
        this.postNumber = postNumber;
    }

    public boolean isAlreadyLiked() {
        return alreadyLiked;
    }

    public void setAlreadyLiked(boolean alreadyLiked) {
        this.alreadyLiked = alreadyLiked;
    }
    private void createButton(){
        addPost = new Button(this);
        addPost.setId(200);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        addPost.setText("Click to Reply to the post");
        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.open_sans);
        int paddingDp = 8;
        addPost.setTypeface(typeface);
        int paddingPixel = getPaddingPixel(paddingDp);

        layoutParams.setMargins(0,paddingDp,0,0);
        addPost.setLayoutParams(layoutParams);
        addPost.setPadding(paddingPixel,0,paddingPixel,0);
        addPost.setBackgroundResource(R.drawable.rounded_border);
    }

    private Button getCurrButton(){return this.addPost; }
    private  int getPaddingPixel(int paddingDp){
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        int paddingPixel = (int)(paddingDp * density);
        return paddingPixel;
    }

    public FloatingActionButton getBackReply() {
        return backReply;
    }

    public void setBackReply(FloatingActionButton backReply) {
        this.backReply = backReply;
    }


}
