package com.example.oireporttool;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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

import java.util.ArrayList;



public class postsAdapter extends RecyclerView.Adapter<postsAdapter.myViewHandler> {
    private Context context;
    private ArrayList<Post> postList; //= new ArrayList<>();

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

        Log.d("post.post_imageUrl", post.post_imageUrl);



            holder.thumbnail.setColorFilter(000000);

            Glide.with(context).load(post.post_imageUrl).override(50,30).into(holder.thumbnail);

//            post.post_imageUrl
        //}

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
                case R.id.action_add_favourite:
                    Toast.makeText(context, "Add to project", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(context, "share", Toast.LENGTH_SHORT).show();
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

    public class myViewHandler extends RecyclerView.ViewHolder {
        public TextView title, description,date;
        public ImageView thumbnail, overflow;
        public myViewHandler(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            date=itemView.findViewById(R.id.date);
            description = itemView.findViewById(R.id.description);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            overflow = itemView.findViewById(R.id.overflow);
        }
    }



}
