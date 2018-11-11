package com.example.allysaas.heple.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.allysaas.heple.LihatDeskripsiActivity;
import com.example.allysaas.heple.R;
import com.example.allysaas.heple.model.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Context mContext;
    private List<Post> postList;
    private String text;
    private String photoUrl;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();
//    final int POST = 0, KOMENTAR = 1;

    public PostAdapter(Context mContext, List<Post> postList) {
        this.mContext = mContext;
        this.postList = postList;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewPost;
//        View viewKomentar;
        PostViewHolder viewHolder;

//        switch (viewType) {
//            case POST:
//                viewPost = LayoutInflater.from(mContext).inflate(R.layout.post_card, parent, false);
//                viewHolder = new PostViewHolder(viewPost);
//                break;
//            case KOMENTAR:
//                viewKomentar = LayoutInflater.from(mContext).inflate(R.layout.komentar_card, parent, false);
//                viewHolder = new CommentViewHolder(viewKomentar);
//                break;
//        }
        viewPost = LayoutInflater.from(mContext).inflate(R.layout.post_card, parent, false);
        viewHolder = new PostViewHolder(viewPost);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PostViewHolder holder, final int position) {


        holder.authorId.setText(postList.get(position).getAuthorId());
        holder.authorId.setVisibility(View.INVISIBLE);

        holder.author.setText(postList.get(position).getAuthorMail());
        holder.time.setText(postList.get(position).getTime().toString());

        holder.nama_penjual.setText(postList.get(position).getNama());
        holder.nama_penjual.setVisibility(View.INVISIBLE);

        photoUrl = postList.get(position).getPhotoUrl();
        Glide.with(this.mContext).load(postList.get(position).getPhotoUrl()).into(holder.image);

        if (postList.get(position).getDeskripsi().length() > 100) {
            text = postList.get(position).getDeskripsi().substring(0, 7) + "...";
            holder.deskripsi.setText(text);
        } else {
            holder.deskripsi.setText(postList.get(position).getDeskripsi());
        }

        holder.like.setText("" + postList.get(position).getLike());

        holder.postId.setText(postList.get(position).getPostId());
        holder.postId.setVisibility(View.INVISIBLE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, LihatDeskripsiActivity.class);

                intent.putExtra("authorId", holder.authorId.getText().toString());
                intent.putExtra("authorMail", holder.author.getText().toString());
                intent.putExtra("time", holder.time.getText().toString());
                intent.putExtra("nama_penjual", holder.nama_penjual.getText().toString());
                intent.putExtra("image", photoUrl);
                intent.putExtra("deskripsi", holder.deskripsi.getText().toString());
                intent.putExtra("like", holder.like.getText().toString());
                intent.putExtra("postId", holder.postId.getText().toString());

                mContext.startActivity(intent);
            }
        });

        holder.imageLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Post id", holder.postId.getText().toString());
                ref.child("posting").child(holder.postId.getText().toString()).child("like").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("Snapshot value", dataSnapshot.getValue().toString());
                        int like = Integer.parseInt(dataSnapshot.getValue().toString());
                        ref.child("posting").child(holder.postId.getText().toString()).child("like").setValue(like + 1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("Error", databaseError.getMessage());
                    }
                });
            }
        });

        holder.imageComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

//    @Override
//    public int getItemViewType(int position) {
//        if (postList.get(position) instanceof Post) {
//            return POST;
//        }
//        return KOMENTAR;
//    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        public TextView authorId, author, time, deskripsi, like, nama_penjual, postId;
        public ImageView image, imageLike, imageComment;

        public PostViewHolder(View itemView) {
            super(itemView);
            authorId = itemView.findViewById(R.id.authorid_card);
            author = itemView.findViewById(R.id.author_card);
            time = itemView.findViewById(R.id.time_card);
            image = itemView.findViewById(R.id.gambar_penjual_card);
            deskripsi = itemView.findViewById(R.id.deskripsi_card);
            like = itemView.findViewById(R.id.like_card);
            nama_penjual = itemView.findViewById(R.id.nama_penjual_card);
            postId = itemView.findViewById(R.id.postid_card);
            imageLike = itemView.findViewById(R.id.image_like_card);
            imageComment = itemView.findViewById(R.id.image_comment_card);
        }
    }

//    public class CommentViewHolder extends RecyclerView.ViewHolder {
//
//        public CommentViewHolder(View itemView) {
//            super(itemView);
//
//        }
//    }
}
