package com.example.oireporttool;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.oireporttool.Database.DatabaseHelper;
import com.example.oireporttool.Database.Post;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class ViewPost extends AppCompatActivity implements OnMapReadyCallback {
    TextView tvDescription,tvDate,longitude,latitude;
    Button btnEdit;
    Button btnDelete;
    DatabaseHelper databaseHelper;
    Context context;
    Bundle ibundle;
    String bundleData;
    private BroadcastReceiver broadcastReceiver;
    MapFragment mapFragment;

    int postId;
    GoogleMap mGoogleMap;
    String longi;
    String lat;

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
        longitude=findViewById(R.id.longitude);
        latitude=findViewById(R.id.latitude);
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
        longitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               lat= latitude.getText().toString();
               longi= longitude.getText().toString();
                DisplayMap(lat,longi);


            }



        });


    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getApplicationContext());
        mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);


        LatLng point = new LatLng(Double.parseDouble(lat), Double.parseDouble(longi));
          drawMarker(point);

    }
    public boolean DisplayMap (String lat,String longi){
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        if(status!= ConnectionResult.SUCCESS){ // Google Play Services are not available


            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, getParent(), requestCode);
            dialog.show();

        }
        else {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
           mapFragment.getMapAsync(this);


//            LatLng point = new LatLng(Double.parseDouble(lat), Double.parseDouble(longi));
//
//
//
////            drawMarker(point);

        }
        return false;
    }


    private void drawMarker(LatLng point){
        mGoogleMap.clear();
        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(point);

        markerOptions.title("Post Taken from here");


        markerOptions.snippet("Latitude:"+point.latitude+",Longitude:"+point.longitude);

       mGoogleMap.addMarker(markerOptions);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(point));

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (broadcastReceiver == null){
            broadcastReceiver =new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    longitude.append("/n" +intent.getExtras().get("gps_longitude"));
                    Log.d("coordinates",longitude.toString());
                    latitude.append("/n" +intent.getExtras().get("gps_latitude"));


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
            longitude.setText(jsnobject.getString("post_coordinates"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
}
}
