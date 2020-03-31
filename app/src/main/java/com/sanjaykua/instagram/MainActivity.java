package com.sanjaykua.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.sanjaykua.instagram.Fragment.HomeFragment;
import com.sanjaykua.instagram.Fragment.NotificationFragment;
import com.sanjaykua.instagram.Fragment.ProfileFragment;
import com.sanjaykua.instagram.Fragment.SearchFragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment selectedFrame = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
    }

        BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener= new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        selectedFrame = new HomeFragment();
                        break;
                    case R.id.nav_search:
                        selectedFrame = new SearchFragment();
                        break;
                    case R.id.nav_add:
                        selectedFrame = null;
                        startActivity(new Intent(MainActivity.this,PostActivity.class));
                        break;
                    case R.id.nav_favourite:
                        selectedFrame = new NotificationFragment();
                        break;
                    case R.id.nav_profile:
                        SharedPreferences.Editor editor= getSharedPreferences("Pref",MODE_PRIVATE).edit();
                        editor.putString("Pref", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        editor.apply();
                        selectedFrame = new ProfileFragment();
                        break;
                }
                if (selectedFrame != null)
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFrame).commit();
                return false;
            }
        };

}
