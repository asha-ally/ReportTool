package com.example.oireporttool;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oireporttool.Database.DatabaseHelper;
import com.example.oireporttool.Database.Post;

import java.io.Externalizable;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

import static com.example.oireporttool.app.AppFunctions.func_formatDateFromString;
import static java.lang.System.currentTimeMillis;


public class postsAdapter extends RecyclerView.Adapter<postsAdapter.myViewHandler> {
    private Context context;
    private ArrayList<Post> postList; //= new ArrayList<>();
    public static final String SHARE_DESCRIPTION = "Look at this new post";
    public static final String HASHTAG_CANDYCODED = " #Nataka";

    public postsAdapter(Context mContext, ArrayList<Post> postList) {
        this.context = mContext;
        this.postList= postList;
    }

    /*@NonNull*/
    @Override
    public postsAdapter.myViewHandler onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_row, parent, false);



        return new myViewHandler(itemView);


    }

    @Override
    public void onBindViewHolder( postsAdapter.myViewHandler holder, int position) {
        Post post = postList.get(position);

        //Log.d("post.post_imageUrl", post.post_imageUrl);
        holder.title.setText(post.getPost_title());
        holder.description.setText(post.getPost_details());
        holder.date.setText(post.getRecord_date());

//        if (post.post_imageUrl != null) {
//            Glide.with(holder.itemView.getContext()).load(post.post_imageUrl).into(holder.thumbnail);
//        }


        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }

    });
    }
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_post, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_project:
                    Toast.makeText(context, "Add to project", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_share:
                    Toast.makeText(context, "share", Toast.LENGTH_SHORT).show();
                    createShareIntent();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        Log.d("waterlilies",String.valueOf(postList.size()));

        return postList.size();
    }


    public class myViewHandler extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, description,date;
        public ImageView thumbnail, overflow;
        public myViewHandler(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            description = itemView.findViewById(R.id.description);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            overflow = itemView.findViewById(R.id.overflow);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.d("position",String.valueOf(position));
            Post clickedPost = postList.get(position);
            Intent intent = new Intent(v.getContext(),ViewPost.class);
            intent.putExtra("Post", String.valueOf(clickedPost));
            v.getContext().startActivity(intent);



        }
    }

    private void  createShareIntent(){
        Intent shareIntent= new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        String shareString= SHARE_DESCRIPTION + HASHTAG_CANDYCODED;
        shareIntent.putExtra(Intent.EXTRA_TEXT,shareString);
        context.startActivity(shareIntent);


    }

}
