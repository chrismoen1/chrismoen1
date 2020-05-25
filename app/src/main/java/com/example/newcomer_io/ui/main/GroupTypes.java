package com.example.newcomer_io.ui.main;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TableLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import com.example.newcomer_io.R;

public class GroupTypes extends AppCompatActivity {
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_types);

        createCardView("Night Clubs",R.drawable.night_club);


    }

    public void createCardView(String name, int val){

        int NUM_HEIGHT = 3;
        int NUM_ROW = 2;

        //This function will be responsible for taking the values of hte
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        int eachHeight = height/NUM_HEIGHT - (NUM_HEIGHT * 2);
        int eachWidth = width/NUM_ROW - (NUM_ROW * 2);

        //Implementing the display of the image
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(val);
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(eachWidth,eachHeight);
        imageView.setLayoutParams(layoutParams);

        //Implementing the card view which hold the dispaly of the image
        CardView cardView = new CardView(this);
        TableLayout.LayoutParams layoutParams1 = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cardView.setPadding(1,1,1,1);


    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public TableLayout getTableLayout() {
        return tableLayout;
    }

    public void setTableLayout(TableLayout tableLayout) {
        this.tableLayout = tableLayout;
    }
}
