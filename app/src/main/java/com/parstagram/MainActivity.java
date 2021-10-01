package com.parstagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;
import com.parstagram.fragments.ComposeFragment;
import com.parstagram.fragments.PostsFragment;
import com.parstagram.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final String SELECTED_ITEM_ID_KEY = "selected_item";

    private static String POSTS_TAG = "posts";
    private static String COMPOSE_TAG = "compose";
    private static String PROFILE_TAG = "profile";
    ComposeFragment composeFragment;
    PostsFragment postsFragment;
    ProfileFragment profileFragment;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FragmentManager fm = getSupportFragmentManager();

        postsFragment = (PostsFragment) fm.findFragmentByTag(POSTS_TAG);
        if (postsFragment == null) {
            postsFragment = PostsFragment.newInstance();
        }

        composeFragment = (ComposeFragment) fm.findFragmentByTag(COMPOSE_TAG);
        if (composeFragment == null) {
            composeFragment = ComposeFragment.newInstance();
        }

        profileFragment = (ProfileFragment) fm.findFragmentByTag(PROFILE_TAG);
        if (profileFragment == null) {
            profileFragment = ProfileFragment.newInstance();
        }

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.action_home:
                    fragment = postsFragment;
//                    Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_post:
                    fragment = composeFragment;
//                    Toast.makeText(MainActivity.this, "Post", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_logout:
                    fragment = profileFragment;
                    logoutCurrentUser();
                    break;
                case R.id.action_user:
//                    Toast.makeText(MainActivity.this, "User", Toast.LENGTH_SHORT).show();
                default:
                    fragment = profileFragment;
                    break;
            }
            fm.beginTransaction().replace(R.id.flContainer, fragment).commit();

            return true;
        });

        if (savedInstanceState != null) {
            int selected_bottom_item = savedInstanceState.getInt(SELECTED_ITEM_ID_KEY);
            bottomNavigationView.setSelectedItemId(selected_bottom_item);
        }
        else {
            bottomNavigationView.setSelectedItemId(R.id.action_home);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void logoutCurrentUser() {
        ParseUser.logOut();

        Toast.makeText(MainActivity.this, "You have successfully logged out.", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Log.i("MainActivity", "Logging that it's saving to savedInstanceState");
        outState.putInt(SELECTED_ITEM_ID_KEY, bottomNavigationView.getSelectedItemId());
        super.onSaveInstanceState(outState);
    }
}