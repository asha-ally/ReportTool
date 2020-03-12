package com.example.oireporttool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.example.oireporttool.Database.DatabaseHelper;

import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
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
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
    ImageView icon;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;

        setContentView(R.layout.activity_main);
        drawer = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        preferences = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        context= this;

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headView= navigationView.getHeaderView(0);
        icon = headView.findViewById(R.id.nav_imageView);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        textView =headView.findViewById(R.id.nav_name);
        textView.setText(preferences.getString("fname",""));


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PostActivity.class);
                startActivity(intent);
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        db = new DatabaseHelper(this);



        all_posts = db.getRecords(db.TABLE_POSTS);
        populateRv();

//        Log.d("all_posts",String.valueOf(all_posts));
//
//        try {
//            Log.d("TABLE_POSTS", String.valueOf(all_posts.getJSONArray("records")));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }




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
        List<com.example.oireporttool.Database.Post> data =new ArrayList<>();
        JSONArray jsonArray = new JSONArray();

        try {
            jsonArray = all_posts.getJSONArray("records");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("jsonArray", String.valueOf(jsonArray));



        ArrayList <com.example.oireporttool.Database.Post> postList= new ArrayList <com.example.oireporttool.Database.Post> ();
        for (int i = 0; i < jsonArray.length(); i++)
        {
            com.example.oireporttool.Database.Post post = new com.example.oireporttool.Database.Post();
            JSONObject json_data = null;
            try {
                json_data = jsonArray.getJSONObject(i);
                post.post_details = json_data.getString("post_detail");
                post.post_title = json_data.getString("post_title");
                post.record_date = json_data.getString("record_date");
                //post.post_imageUrl =json_data.getString("post_imageUrl");

                postList.add(post);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


            RecyclerView rvposts = findViewById(R.id.rvNotes);
            //rvposts.setHasFixedSize(true);

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



    }






    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        switch (id){
            case R.id.navi_email:
                preferences.getString("email","");
                break;
            case R.id.logOut:
                func_showToast(context,"clicked !!");
                logout();
                break;
            case  R.id.nav_imageView:
                selectImage();


        }

        return true;
    }
    public void logout(){


        //editor.commit();
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Oh noo!...");
        progressDialog.show();

        Intent intent = new Intent(getBaseContext(),LoginActivity.class);
        startActivity(intent);


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        SharedPreferences preferences =getSharedPreferences("loginPrefs",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear().apply();
                        String logout=preferences.getAll().toString();
                        Log.d("logout",logout);
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 5000);





    }
    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider",f));
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    icon.setImageBitmap(bitmap);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "OIReportool" + File.separator + "default";

                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("path", picturePath+"");
                icon.setImageBitmap(thumbnail);
            }
        }
    }




}
