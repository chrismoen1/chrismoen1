package com.example.newcomer_io.ui.main.FindFriends;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.example.newcomer_io.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile {


    final static int USER_NAME = 1;
    final static int LOCATION = 2;
    final static int FOLLOW = 3;
    final static int PROFILE_IMAGE = 4;

    public LinearLayout createUserProfileView(String profileName, String location, Context context){
        //LinearLayout
        LinearLayout completeContainer = createLinearLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.VERTICAL,context);

        //Large container to support the entire frame
        LinearLayout largeContainer = createLinearLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.HORIZONTAL,context);
        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) largeContainer.getLayoutParams();
        layoutParams1.setMargins(0,0,0,dip2px(context,5)); //adjust based on how we want the UI to look
        largeContainer.setLayoutParams(layoutParams1);

        //Smaller container to support hte user name and their respective location
        LinearLayout smallerContainer = createLinearLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.VERTICAL,context);
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) smallerContainer.getLayoutParams();
        layoutParams2.setMargins(dip2px(context,5),0,0,0); //adjust based on how we want the UI to look
        smallerContainer.setLayoutParams(layoutParams2);

        TextView userName = createUserText(profileName,15,"BOLD",USER_NAME,context);
        TextView userLocation = createUserText(location,13, "SANS",LOCATION,context);
        TextView follow = createUserText_BackDrop("Follow", 15, R.drawable.rounded_border,FOLLOW, context);

        smallerContainer.addView(userName);
        smallerContainer.addView(userLocation);

        //Use this as the holder for the user's profile image
        //CircleImageView profileImage = createProfileImage(image);

        Space space1 = createSpace(context);
        Space space2 = createSpace(context);

        CircleImageView circleImageView = new CircleImageView(context);
        circleImageView.setImageResource(R.drawable.nightlife);
        circleImageView.setId(PROFILE_IMAGE);

        //Setting the height/width of the profile imag eto be shown on the UI
        int width = dip2px(context, 40);
        int height = dip2px(context, 37);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        circleImageView.setLayoutParams(layoutParams);

        //Add all of the views to the conatiner
        largeContainer.addView(circleImageView);
        largeContainer.addView(smallerContainer);
        largeContainer.addView(space2);
        largeContainer.addView(follow);

        completeContainer.addView(largeContainer);
        View divider = createDivider(context);
        completeContainer.addView(divider);

        return completeContainer;
    }

    private TextView createUserText_BackDrop(String text, float textSize, int rounded_border,int tag, Context context) {
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setTextSize(textSize);
        textView.setBackground(ContextCompat.getDrawable(context,rounded_border));

        textView.setId(tag);
        int i = dip2px(context, 7);
        textView.setPadding(i,i,i,i);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        textView.setLayoutParams(layoutParams);

        return textView;

    }

    private View createDivider(Context context){
        View divider = new View(context);
        int height = dip2px(context, 1);
        int margin = dip2px(context,4);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height);
        layoutParams.setMargins(0,margin,0,margin);
        divider.setBackgroundColor(R.color.LightGrey);
        divider.setLayoutParams(layoutParams);
        return divider;
    }
    private Space createSpace(Context context){
        Space space = new Space(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1;
        space.setLayoutParams(layoutParams);

        return space;
    }
    private Button createButtonClick(String follow, float textSize, Context context) {
        Button button = new Button(context);
        button.setBackground(ContextCompat.getDrawable(context,R.drawable.rounded_border));

        button.setTextSize(textSize);
        button.setText(follow);
        button.setTypeface(Typeface.SANS_SERIF);

        int i = dip2px(context, 1);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //button.setPadding(i,i,i,i);
        button.setLayoutParams(layoutParams);
        return button;

    }

    private LinearLayout createLinearLayout(int width, int height, int orientation,Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);

        linearLayout.setOrientation(orientation);
        linearLayout.setLayoutParams(layoutParams);
        return linearLayout;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private CircleImageView createProfileImage(Bitmap image, Context context){
        CircleImageView circleImageView = new CircleImageView(context);
        circleImageView.setImageBitmap(image);

        //Setting the height/width of the profile imag eto be shown on the UI
        int width = dip2px(context, 38);
        int height = dip2px(context, 33);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        circleImageView.setLayoutParams(layoutParams);
        return circleImageView;
    }

    private TextView createUserText(String profileName, float textSize, String typeFace, int id,Context context) {
        TextView textView = new TextView(context);
        textView.setText(profileName);
        textView.setTextSize(textSize);
        //Typeface typeface = ResourcesCompat.getFont(this,R.font.open_sans);

        textView.setId(id);
        if (typeFace.equals("BOLD")){
            textView.setTypeface(Typeface.DEFAULT_BOLD);
        }else if(typeFace.equals("ITALIC")){
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        }
        else if(typeFace.equals("SANS")){
            textView.setTypeface(Typeface.SANS_SERIF);
        }
        LinearLayout.LayoutParams textWrap = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(textWrap);
        return textView;
    }

}
