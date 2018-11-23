package com.example.allysaas.heple.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.allysaas.heple.LihatDeskripsiActivity;
import com.example.allysaas.heple.R;
import com.example.allysaas.heple.model.Posting;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Context mContext;
    private List<Posting> postingList;
    private List<Long> likeList;
    private List<String> postIdList;
    private List<String> memberNameList;
    private String text;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public PostAdapter(Context mContext, List<Posting> postingList, List<Long> likeList, List<String> postIdList, List<String> memberNameList) {
        this.mContext = mContext;
        this.postingList = postingList;
        this.likeList = likeList;
        this.postIdList = postIdList;
        this.memberNameList = memberNameList;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewPost;
        PostViewHolder viewHolder;

        viewPost = LayoutInflater.from(mContext).inflate(R.layout.post_card, parent, false);
        viewHolder = new PostViewHolder(viewPost);
        return viewHolder;
    }

    private boolean onChange = false;
    private int i = 0;

    @Override
    public void onBindViewHolder(final PostViewHolder holder, final int position) {
        holder.time.setText(DateUtils.getRelativeDateTimeString(mContext, (postingList.get(position).getTime() * 1000), DateUtils.SECOND_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL));
        Log.d("Likelist Value", position + likeList.get(position).toString());

        Log.d("Likelist Value-onchange", position + likeList.get(position).toString());

        Log.d("Item count", getItemCount() + "");

        if (!onChange) {
            Log.e("size postlist", postingList.size() + "");
            Log.e("i val", i + "");
            Log.e("Get item count", getItemCount() + "");
            holder.like.setText(likeList.get(position).toString());
            if (i == getItemCount()) {
                onChange = !onChange;
            }
            i++;
        }

        holder.member.setText(memberNameList.get(position));

        Glide.with(this.mContext).load(postingList.get(position).getPhotoUrl()).into(holder.image);

        try {
            if (postingList.get(position).getDeskripsi().length() > 100) {
                text = postingList.get(position).getDeskripsi().substring(0, 7) + "...";
                holder.deskripsi.setText(text);
            } else {
                holder.deskripsi.setText(postingList.get(position).getDeskripsi());
            }
        } catch (Exception e) {
            Log.d("EEEEE", e.toString());
        }

        Log.d("Likelist di PostAd", likeList.size() + "");
        Log.d("postlist di PostAd", postingList.size() + "");
        Log.d("Position adap", position + "");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, LihatDeskripsiActivity.class);
                intent.putExtra("postObject", postingList.get(position));
                Log.d("Adapter member name", memberNameList.get(position));
                intent.putExtra("memberName", memberNameList.get(position));
                intent.putExtra("totalLike", likeList.get(position).toString());
                intent.putExtra("postId", postIdList.get(position));
                mContext.startActivity(intent);
            }
        });

        holder.imageLike.setOnClickListener(new View.OnClickListener() {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference mRefLike = database.getReference("like").child(postIdList.get(position));

            @Override
            public void onClick(View view) {
                Log.d("POST ID", postIdList.get(position));
                mRefLike.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String mUid = mAuth.getCurrentUser().getUid();
                        boolean isUserExist = false;
                        for (DataSnapshot uniqueKey : dataSnapshot.getChildren()) {
                            if (uniqueKey.getKey().equals(mUid)) {
                                mRefLike.child(uniqueKey.getKey()).removeValue();
                                isUserExist = true;
                                Log.d("Children count", dataSnapshot.getChildrenCount() + "");
                                Log.e("Uniq key count", uniqueKey.getChildrenCount() + "");
                                holder.like.setText(String.valueOf(Integer.parseInt(holder.like.getText().toString()) - 1));
                            }
                        }
                        if (!isUserExist) {
                            mRefLike.child(mUid).setValue(1);
                            holder.like.setText(String.valueOf(Integer.parseInt(holder.like.getText().toString()) + 1));
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

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

    @Override
    public int getItemCount() {
        return postingList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        public TextView member, time, deskripsi, like;
        public ImageView image, imageLike, imageComment;

        public PostViewHolder(View itemView) {
            super(itemView);
            member = itemView.findViewById(R.id.member_card);
            time = itemView.findViewById(R.id.time_card);
            image = itemView.findViewById(R.id.gambar_penjual_card);
            deskripsi = itemView.findViewById(R.id.deskripsi_card);
            like = itemView.findViewById(R.id.like_card);
            imageLike = itemView.findViewById(R.id.image_like_card);
            imageComment = itemView.findViewById(R.id.image_comment_card);
        }
    }

}
