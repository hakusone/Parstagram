package com.parstagram;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parstagram.fragments.ComposeFragment;
import com.parstagram.fragments.PostsFragment;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FragmentManager fragmentManager = getSupportFragmentManager();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.action_home:
                    fragment = new PostsFragment();
                    Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_post:
                    fragment = new ComposeFragment();
                    Toast.makeText(MainActivity.this, "Post", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_user:
                    fragment = new ComposeFragment();
                    Toast.makeText(MainActivity.this, "User", Toast.LENGTH_SHORT).show();
                    break;
//                case R.id.action_logout:
//                    logoutCurrentUser();
//                    break;
                default:
                    fragment = new ComposeFragment();
                    break;
            }
            fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();

            return true;
        });

//        queryPosts();
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

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with querying finding posts", e);
                    return;
                }
                for (Post post: objects) {
                    Log.i(TAG, "Post: " + post.getDescription() + " Username: " + post.getUser().getUsername());
                }
            }
        });
    }
}