package com.example.newcomer_io;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.example.newcomer_io.ui.main.GroupTiming.CreateStudyGroup;
import com.example.newcomer_io.ui.main.JoinGroup.JoinGroup;
import com.example.newcomer_io.ui.main.MainFragment;
import com.example.newcomer_io.ui.main.SignIn.SignIn;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

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
        toolbar.setTitle("Profile View");
        mBottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.group_add:
                        //userData.updateUserData();
                        Intent intent = new Intent(MainActivity.this, CreateStudyGroup.class);
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

        LinearLayout signOut = findViewById(R.id.signOutContainer);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Then we sign out and go back to the initial page
                FirebaseAuth.getInstance().signOut();
                //Then we return to the main page
                Intent intent = new Intent(MainActivity.this, SignIn.class);
                startActivity(intent);
            }
        });
    }

}
