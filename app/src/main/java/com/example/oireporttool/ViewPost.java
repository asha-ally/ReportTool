package com.example.oireporttool;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.oireporttool.Database.DatabaseHelper;
import com.example.oireporttool.Database.Post;

public class ViewPost extends AppCompatActivity {
    TextView tvDescription,tvDate;
    Button btnEdit;
    Button btnDelete;
    DatabaseHelper databaseHelper;
    Context context;
    int postId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvDescription= findViewById(R.id.tvDescription);
        tvDate=findViewById(R.id.tvDate);
        btnDelete=findViewById(R.id.btnDelete);
        btnEdit= findViewById(R.id.btnEdit);
        getPostId();
        context=this;
        databaseHelper=new DatabaseHelper(context);

        displayPost();

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.deletePost(postId);
                finish();
            }
        });

    }

    public void getPostId(){
        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            postId=bundle.getInt("KEY_POST_ID",0);

        }

    }
    public void displayPost(){
        Post post= databaseHelper.getRecordById(postId);
//        tvDate.setText(post.getRecord_date());
        tvDescription.setText(post.getPost_details());
}
}
