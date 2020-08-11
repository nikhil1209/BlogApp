package com.nikhil.blogapp.Data;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nikhil.blogapp.Model.Blog;
import com.nikhil.blogapp.R;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Blog> blogList;


    public BlogRecyclerAdapter(Context context, List<Blog> blogList) {
        this.context = context;
        this.blogList = blogList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row,parent,false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Blog blog=blogList.get(position);
        String imageUrl=null;

        holder.title.setText(blog.getTitle());
        holder.desc.setText(blog.getDesc());

        java.text.DateFormat dateFormat= java.text.DateFormat.getDateInstance();
        String formattedDate=dateFormat.format(new Date(Long.valueOf(blog.getTimestamp())).getTime());

        holder.timestamp.setText(formattedDate);

        imageUrl=blog.getImage();
        Log.d("CHECK",imageUrl);
        //TODO:use picasso library to load img
        Picasso.get().load(imageUrl).into(holder.image);

//        holder.fname.setText(blog.getFirst());
//        Log.d("nm",blog.getFirst());



//        String user=blog.userid;
//        DatabaseReference path=FirebaseDatabase.getInstance().getReference().child("MUsers").child(user).child("firstname");
//        path.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                User user1=snapshot.getValue(User.class);
//                Log.d("fname",user1.toString());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d("er","Error fetching Data");
//            }
//        });



    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title,desc,timestamp;
        public ImageView image;
        public String userid;
        public TextView fname;


        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);

            context=ctx;

            title=(TextView) itemView.findViewById(R.id.postTitle);
            desc=(TextView) itemView.findViewById(R.id.postTextList);
            timestamp=(TextView) itemView.findViewById(R.id.timestampList);
            image=(ImageView) itemView.findViewById(R.id.postimageList);


            userid=null;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //we can go on another activity
                }
            });

        }
    }
}
