package com.example.oireporttool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.example.oireporttool.Database.DatabaseHelper;
import com.example.oireporttool.Database.Post;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class AddPostActivity extends AppCompatActivity {
    EditText etTitle;
    EditText etDescription;
    Button btnSave;
    private String description;
    Button btnAddphoto;
    Button btnAddVoiceNote;
    Button btnInsertFile;
    private String title;
    private String imageUrl;
    private String date;
    private String audioUrl;
    private ImageView imgView;
    private String Url;
    private String user_id;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    TextView textFile;

    private static final int PICKFILE_RESULT_CODE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    SharedPreferences prefs;
    Context context;
    ImageView imageView;
    Spinner projects;
    Spinner category;
    Spinner tag;

    DatabaseHelper databaseHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etDescription=findViewById(R.id.etDescription);
        imageView=findViewById(R.id.imgView);
        imgView=findViewById(R.id.thumbnail);
        databaseHelper= new DatabaseHelper(this);
        btnSave=findViewById(R.id.btnSave);
        prefs= getSharedPreferences("loginPrefs", MODE_PRIVATE);
        user_id= "1";// prefs.getString("id","1");
        btnAddphoto=findViewById(R.id.btnAddPhoto);
        btnAddVoiceNote=findViewById(R.id.btnAddVoiceNote);
        btnInsertFile=findViewById(R.id.btnInsertFile);
        projects= findViewById(R.id.project_array);
        tag= findViewById(R.id.tag_array);
        category= findViewById(R.id.category_array);

        context=this;

        btnAddphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        btnInsertFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent,PICKFILE_RESULT_CODE);

            }

        });
        btnAddVoiceNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),VoiceRecordActivity.class);
                startActivity(intent);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                description= etDescription.getText().toString();
                date = LocalDateTime.now().toString();
                imageUrl = Url;
                String post_projects= projects.getSelectedItem().toString();
                String post_tag = tag.getSelectedItem().toString();
                String post_category = category.getSelectedItem().toString();

                long result = 0;

                //Post post =new Post(title,description,date,imageUrl,audioUrl,user_id);
                //databaseHelper.addPost(post);

                JSONObject post_b = new JSONObject();
                try {
                    post_b.put("userId", user_id);
                    post_b.put("description", description);
                    post_b.put("date", date);
                    post_b.put("imageUrl", imageUrl);
                    post_b.put("project",post_projects);
                    post_b.put("tag",post_tag);
                    post_b.put("category",post_category);
                    //post_b.put("audioUrl", audioUrl);

                    result = databaseHelper.addPost(post_b);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("post_result", String.valueOf(result));

                finish();



            }

        });


    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.oireporttool.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data!= null) {

                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);



                Log.d("bando", String.valueOf(data));

                String FilePath = data.getData().getPath();
                textFile.setText(FilePath);
                Log.d("rada", FilePath);
//                Intent intent= new Intent(getApplicationContext(),AddPostActivity.class);
//                startActivity(intent);

        }

        }





    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        Url = image.getAbsolutePath();
        Log.d("asha", Url);
        return image;
    }





}



