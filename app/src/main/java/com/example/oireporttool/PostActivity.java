package com.example.oireporttool;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.oireporttool.Database.DatabaseHelper;
import com.example.oireporttool.Database.Post;
import com.google.android.gms.maps.model.LatLng;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class PostActivity extends AppCompatActivity {
    EditText etTitle;
    EditText etDescription;
    Button btnSave;
    private String description;
    Button btnAddphoto;
    private BroadcastReceiver broadcastReceiver;
    Button btnAddVoiceNote;
    Button btnInsertFile;
    private String title;
    private String imageUrl;
    private String date;
    private String audioUrl;
    private ImageView imgView;
    private String Url;

    private String user_id;
    int postId;
    private String post_projects;
    private String post_tag;
    private String post_category;
    private String post_longitude;
    private String post_latitude;



    private String POSTS_API_URL="http://10.0.2.2/oireporting_web/ort_orgs_api.php";

    Bundle bundle;
    String bundleData;
    String form_action;
    String form_data;
    String form_post_id;

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
    TextView tvlongitude;
    TextView tvlatitude;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bundle = getIntent().getExtras();
        form_action = bundle.getString("PostAction");
        form_data = bundle.getString("PostData");
        form_post_id = bundle.getString("PostId");

        Log.d("form_data", form_data);

        etDescription=findViewById(R.id.etDescription);

        imageView=findViewById(R.id.imgView);
        imgView=findViewById(R.id.thumbnail);
        databaseHelper= new DatabaseHelper(this);
        btnSave=findViewById(R.id.btnSave);
        prefs= getSharedPreferences("loginPrefs", MODE_PRIVATE);
        user_id = prefs.getString("id","1");
        btnAddphoto=findViewById(R.id.btnAddPhoto);
        btnAddVoiceNote=findViewById(R.id.btnAddVoiceNote);
        btnInsertFile=findViewById(R.id.btnInsertFile);
        projects= findViewById(R.id.project_array);
        tag= findViewById(R.id.tag_array);
        category= findViewById(R.id.category_array);
        tvlongitude=findViewById(R.id.tvlongitude);
        tvlatitude=findViewById(R.id.tvlatitude);

        Intent intent = new Intent(getApplicationContext(),GPS_Service.class);
        startService(intent);

        getPostId();

        context=this;
        displayPost();
        CheckPermissions();




        btnAddphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();

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


                post_projects= projects.getSelectedItem().toString();
                post_tag = tag.getSelectedItem().toString();
                post_category = category.getSelectedItem().toString();
                post_longitude = tvlongitude.getText().toString();
                post_latitude= tvlatitude.getText().toString();
//                imageUrl= selectImage();


                String result = "0";
//

                //PostActivity post =new PostActivity(title,description,date,imageUrl,audioUrl,user_id);
                //databaseHelper.addPost(post);

                JSONObject post_b = new JSONObject();
                try {

                    if(form_action.equals("_edit")){
                        post_b.put("post_id", form_post_id);
                    }
                    //post_b.put("imageUrl", Url);
                    post_b.put("user_id", user_id);
                    post_b.put("description", description);
                    post_b.put("post_project",post_projects);
                    post_b.put("post_tag",post_tag);
                    post_b.put("post_category",post_category);
                    post_b.put("post_longitude",post_longitude);
                    post_b.put("post_latitude",post_latitude);
                    post_b.put("image_url", imageUrl);
                    Log.d("url",imageUrl);


                    if(form_action.equals("_edit")){
                        result = databaseHelper.post_Edit(post_b);
                    } else {
                        result = databaseHelper.post_Add(post_b);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("post_result", String.valueOf(result));

                refreshMainActivity();

                finish();


                //postNote("none",description,user_id);

                stopService(intent);

            }


        });



    }
    @Override
    protected void onResume() {
        super.onResume();
        if (broadcastReceiver == null){
            broadcastReceiver =new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    tvlongitude.append("" + intent.getExtras().get("gps_longitude")); /*"/n" +*/
                    tvlatitude.append("" + intent.getExtras().get("gps_latitude"));
                    Log.d("coordinates",tvlongitude.toString());



                }
            };
            registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);

        }
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds options to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    public void refreshMainActivity(){

        /*MainActivity.getInstance().refreshList();*/
        MainActivity.getmInstanceActivity().refreshList();
    }


    private String selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
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
                    Intent intent = new   Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

        return imageUrl;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
//                    if(f.exists()){
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
                    imageView.setImageBitmap(bitmap);
//                    Intent intent =new Intent(getBaseContext(),MainActivity.class);
//                    intent.putExtra("data",bitmap);

                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "OIReportool" + File.separator + "default";

                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    imageUrl = String.valueOf(file);
//                    Log.d("image_path_a",imageUrl);
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
                Log.w("image_path_b", picturePath+"");
                imageView.setImageBitmap(thumbnail);
                imageUrl = picturePath;
            }
        }
    }



    public void getPostId(){
        /*Bundle bundle = getIntent().getExtras();*/
        if(bundle != null){
            postId = bundle.getInt("KEY_POST_ID",0);
        }
    }

    public void displayPost(){

        String post = form_data;

        try {
            //JSONArray jsonArray = new JSONArray(post);
            JSONObject jsonObject = new JSONObject(post);
            Log.d("displayPostObject", String.valueOf(jsonObject));
            Log.d("displayPostObject", jsonObject.getString("post_Id"));

            etDescription.setText( jsonObject.getString("post_details"));
            //etTitle.setText(jsonObject.getString("record_date"));
            tvlongitude.setText(jsonObject.getString("post_longitude"));
            tvlatitude.setText(jsonObject.getString("post_latitude"));
            //projects.setSelection();
        }
        catch (JSONException e) {
            Log.e("displayPost", e.getMessage());
        }

        /*Post post= new Post();
        JSONObject jsnobject = post.getPostAll();
        try {
            etDescription.setText( jsnobject.getString("post_details"));
            Log.d("edited", jsnobject.getString("post_details"));
            etTitle.setText(jsnobject.getString("record_date"));

        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }


    public boolean CheckPermissions() {
            if (Build.VERSION.SDK_INT >=23 && ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

                requestPermissions(new String[]{ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION,CAMERA,WRITE_EXTERNAL_STORAGE,RECORD_AUDIO},100);
                return true;
            }
            return false;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100){
            if (grantResults [0] == PackageManager.PERMISSION_GRANTED && grantResults [1]== PackageManager.PERMISSION_GRANTED  && grantResults [2]== PackageManager.PERMISSION_GRANTED && grantResults [3]== PackageManager.PERMISSION_GRANTED&&grantResults [4]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();

            }
        }
    }

    private void RequestPermissions() {
        ActivityCompat.requestPermissions(PostActivity.this, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_CAPTURE);
    }

    private  void postNote(final String title, final String description,final String user_id){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, POSTS_API_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("post response",s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int user_id=jsonObject.getInt("user_id");
                    String title= jsonObject.getString("title");
                    String description=jsonObject.getString("description");


                    finish();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("json_error",volleyError.getMessage());

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<String, String>();
                params.put("user_id",String.valueOf(user_id));
                params.put("title",title);
                params.put("description",description);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getBaseContext());
        requestQueue.add(stringRequest);

    }




}



