package com.example.newcomer_io;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.example.newcomer_io.ui.main.EventDetails.FilterView;
import com.example.newcomer_io.ui.main.GroupTiming.CreateGroup;
import com.example.newcomer_io.ui.main.JoinGroup.JoinGroup;
import com.example.newcomer_io.ui.main.MainFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNavView;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
        mBottomNavView = findViewById(R.id.bottomNavigationView);
        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        mBottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.group_add:
                        //userData.updateUserData();
                        Intent intent = new Intent(MainActivity.this, CreateGroup.class);
                        startActivity(intent);
                        return true;
                    case R.id.merge:
                        Intent intent1 = new Intent(MainActivity.this, JoinGroup.class);
                        startActivity(intent1);
                        return true;

                }
                return false;
            }
        });

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
