package com.example.oireporttool;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import com.example.oireporttool.Database.DatabaseHelper;
import com.example.oireporttool.Database.Post;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;

public class AddPostActivity extends AppCompatActivity {
    EditText etTitle;
    EditText etDescription;
    Button btnSave;
    private String description;
    private String title;
    private String imageUrl;
    private String date;
    private String audioUrl;
    private ImageView imgView;
    Context context;

    DatabaseHelper databaseHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        etTitle=findViewById(R.id.etTitle);
        etDescription=findViewById(R.id.etDescription);
        btnSave=findViewById(R.id.btnSave);
        imgView=findViewById(R.id.thumbnail);
        databaseHelper= new DatabaseHelper(this);
        context=this;

        btnSave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                title = etTitle.getText().toString();
                description= etDescription.getText().toString();
                date = LocalDateTime.now().toString();
                imageUrl="";
                audioUrl="";
//                Post post =new Post(title,description,date,imageUrl,audioUrl);
//                databaseHelper.addPost(post);


            }

        });



    }
//
}



