package com.example.oireporttool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.example.oireporttool.Database.DatabaseHelper;
import com.example.oireporttool.Database.Post;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.example.oireporttool.app.AppFunctions.func_showToast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Activity activity;

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    ImageView fab;
    SharedPreferences preferences;
    Context context;
    DatabaseHelper db;

    JSONObject all_posts;

    postsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;

        setContentView(R.layout.activity_main);
        drawer = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        preferences = MainActivity.this.getPreferences(Context.MODE_PRIVATE);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddPostActivity.class);
                startActivity(intent);
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        db = new DatabaseHelper(this);

        all_posts = db.getRecords(db.TABLE_POSTS);
//        Log.d("all_posts",String.valueOf(all_posts));

        try {
            Log.d("TABLE_POSTS", String.valueOf(all_posts.getJSONArray("records")));
        } catch (JSONException e) {
            e.printStackTrace();
        }




    }



    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private  void populateRv(){

        //JSONObject all_posts = db.getRecords(db.TABLE_POSTS);
        Iterator x = all_posts.keys();
        List<Post> data =new ArrayList<>();
        JSONArray jsonArray = new JSONArray();

        try {
            jsonArray = all_posts.getJSONArray("records");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("jsonArray", String.valueOf(jsonArray));



        ArrayList < Post > postList= new ArrayList < Post > ();
        for (int i = 0; i < jsonArray.length(); i++)
        {
            Post post = new Post();
            JSONObject json_data = null;
            try {
                json_data = jsonArray.getJSONObject(i);
                post.post_details = json_data.getString("post_detail");
                post.post_title = json_data.getString("post_title");
                post.record_date = json_data.getString("record_date");
                post.post_imageUrl =json_data.getString("post_imageUrl");

                postList.add(post);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


            RecyclerView rvposts = findViewById(R.id.rvNotes);
            rvposts.setHasFixedSize(true);

            //Log.d("postList", String.valueOf(postList));

            adapter = new postsAdapter(getApplicationContext(), postList);
            rvposts.setAdapter(adapter);

            LinearLayoutManager postlayoutmanager=
                    new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
            rvposts.setLayoutManager(postlayoutmanager);

        }

    @Override
    protected void onResume() {
        super.onResume();
        populateRv();



    }






    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        switch (id){
            case R.id.nav_email:
                preferences.getString("email","");
                break;
            case R.id.logOut:
                logout();
                break;
        }

        return true;
    }
    public void logout(){
        SharedPreferences preferences =getSharedPreferences("loginPrefs",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().apply();
        String logout=preferences.getAll().toString();
        Log.d("logout",logout);
        //editor.commit();
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Oh noo!...");
        progressDialog.show();

        Intent intent = new Intent(getBaseContext(),LoginActivity.class);
        startActivity(intent);
        finish();
    }


}
