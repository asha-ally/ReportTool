package com.example.oireporttool;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

public class AddPostActivity extends AppCompatActivity {
    EditText etTitle;
    EditText etDescription;
    Button btnSave;private String description;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etTitle=findViewById(R.id.etTitle);
        etDescription=findViewById(R.id.etDescription);
        btnSave=findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                addPost();
//
            }

        });


    }

//    private void addPost() {
//        title = etTitle.getText().toString();
//        details = etDescription.getText().toString();
//        String number = _number.getText().toString();
//        String email = _emailText.getText().toString();
//        String password = _passwordText.getText().toString();
//
//
//
//        JSONObject user =new JSONObject();
//        try {
//            user.put("fname",fname);
//            user.put("lname",lname);
//            user.put("number",number);
//            user.put("email",email);
//            user.put("pwd",password);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        //
//
//    }

}
