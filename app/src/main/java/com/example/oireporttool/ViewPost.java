package com.example.oireporttool;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.oireporttool.Database.DatabaseHelper;
import com.example.oireporttool.Database.Post;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

public class ViewPost extends AppCompatActivity {
    TextView tvDescription,tvDate;
    Button btnEdit;
    Button btnDelete;
    DatabaseHelper databaseHelper;
    Context context;
    Bundle ibundle;
    String bundleData;


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
        ibundle = getIntent().getExtras();
        bundleData = (String) ibundle.get("PostData");
//        JSONObject data = ibundle.
        Log.d("bundleData", bundleData);



        displayPost();

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.deletePost(postId);
                finish();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Post clickedPost = new Post();
                JSONObject asha = clickedPost.getPostAll();
                String thePostData = String.valueOf(asha);
                Log.d("postList asha", String.valueOf(asha));
                Intent intent = new Intent(v.getContext(),PostActivity.class);
                intent.putExtra("PostActivity", String.valueOf(clickedPost));
                intent.putExtra("PostData", thePostData);
                startActivity(intent);


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
        try {
            JSONObject jsnobject = new JSONObject(bundleData);
            Log.d("ka value", jsnobject.getString("post_details"));
            tvDescription.setText( jsnobject.getString("post_details"));
            tvDate.setText(jsnobject.getString("record_date"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
}
}
