package com.google.sayanbanik1997.notebook;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    NavigationView navView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION   // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN        // hide status bar
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );


        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.main);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navDrClose, R.string.navDrOpen);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navView = (com.google.android.material.navigation.NavigationView) findViewById(R.id.navView);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            Fragment fragment;
            Bundle bundle;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                    fragment = new EtFrag();
//                    bundle = new Bundle();
//                    bundle.putString("fragType", "cusSup");
//                    fragment.setArguments(bundle);
//                    getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, fragment).commit();

                if(item.getItemId()==R.id.notesMenu) getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, new NotesFrag()).commit();

                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.mainFrameLayout, new NotesFrag()).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION   // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN        // hide status bar
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
    }
}