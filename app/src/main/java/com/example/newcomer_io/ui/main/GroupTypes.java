package com.example.newcomer_io.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import com.example.newcomer_io.R;
import com.example.newcomer_io.ui.main.EventDetails.FilterView;
import org.w3c.dom.Text;

public class GroupTypes extends AppCompatActivity {
    private TableLayout tableLayout;
    private Button addCustomType;
    private androidx.appcompat.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_types);

        CardView night_clubs = createCardView("Night Clubs", R.drawable.night_club);
        CardView study_groups = createCardView("Study Groups",R.drawable.studygroups);
        CardView bars = createCardView("Bars", R.drawable.bar);
        CardView restaurants = createCardView("Restaurants", R.drawable.restaurants);

        toolbar = findViewById(R.id.toolbar1);
        toolbar.setTitle("Group Types");
        setSupportActionBar(toolbar);

        tableLayout = findViewById(R.id.tableLayout);
        TableRow tableRow = getTableRow();

        tableRow.addView(night_clubs);
        tableRow.addView(restaurants);

        tableLayout.addView(tableRow);

        TableRow tableRow1 = getTableRow();
        tableLayout.addView(tableRow1);
        tableRow1.addView(bars);
        tableRow1.addView(study_groups);

        addCustomType = findViewById(R.id.button);

    }

    private TableRow getTableRow() {
        TableRow tableRow = new TableRow(this);
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tableRow.setLayoutParams(layoutParams);
        return tableRow;
    }

    public CardView createCardView(String name, int val){
        int NUM_HEIGHT = 3;
        int NUM_ROW = 2;

        //This function will be responsible for taking the values of hte
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        int eachHeight = height/NUM_HEIGHT - (NUM_HEIGHT * 2);
        int eachWidth = width/NUM_ROW - (NUM_ROW * 2);

        //-----------------------------------------------------------------------------------------------------------------------------------------------------------//
        //Implementing the display of the image
        ImageView imageView = new ImageView(this);
        imageView.setImageDrawable(getResources().getDrawable(val, getApplicationContext().getTheme()));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(eachWidth,eachHeight);
        imageView.setLayoutParams(layoutParams);

        //-----------------------------------------------------------------------------------------------------------------------------------------------------------//
        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(layoutParams2);

        //-----------------------------------------------------------------------------------------------------------------------------------------------------------//
        //Implementing the card view which hold the dispaly of the image
        CardView cardView = new CardView(this);
        cardView.setElevation((float) dip2px(this,2));
        TableRow.LayoutParams layoutParams1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        cardView.setPadding(1,1,1,1);

        int dppp = dip2px(this,5);
        layoutParams1.setMargins(dppp,dppp,dppp,dppp);

        cardView.setLayoutParams(layoutParams1);

        //-----------------------------------------------------------------------------------------------------------------------------------------------------------//
        //Add the Textview and picture to the linear layout
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Typeface typeface = ResourcesCompat.getFont(this,R.font.open_sans);
        textView.setTypeface(typeface);
        //Also want to set the gravity of the text so that this pertains to he center
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
        layoutParams3.gravity = Gravity.CENTER;
        textView.setLayoutParams(layoutParams3);
        textView.setText(name);

        //-----------------------------------------------------------------------------------------------------------------------------------------------------------//
        //1. Add the image view to the linear layout
        linearLayout.addView(imageView);

        //2. Add the text view to the linear layout
        linearLayout.addView(textView);

        //3. Now add the linear layout to the cardview to produce the final resutl
        cardView.addView(linearLayout);

        return cardView;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public Button getAddCustomType() {
        return addCustomType;
    }

    public void setAddCustomType(Button addCustomType) {
        this.addCustomType = addCustomType;
    }
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
