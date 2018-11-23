package com.example.allysaas.heple;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.allysaas.heple.adapter.PostAdapter;
import com.example.allysaas.heple.model.Member;
import com.example.allysaas.heple.model.Posting;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;

    private List<Posting> postingList = new ArrayList<>();
    private List<Long> likeList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<String> postIdList = new ArrayList<>();
    private List<String> memberNameList = new ArrayList<>();
    long counter;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firebaseAuth = FirebaseAuth.getInstance();
        fetchPost();

        //SET RECYCLERVIEW CONTENT USING ADAPTER
        recyclerView = findViewById(R.id.recycler_view);
        Log.d("Size", "" + postingList.size());
        Log.d("Size Likelist", "" + likeList.size());

        //CALL ADAPTER
        postAdapter = new PostAdapter(HomeActivity.this, postingList, likeList, postIdList, memberNameList);
        recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, true));
        recyclerView.setAdapter(postAdapter);
    }

    private boolean onChange = false;

    public void getLike(final long postCount, final String postId, final Posting mPosting, final String key, final String name) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mRefLike = database.getReference("like").child(postId);
        mRefLike.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                counter = 0;
                for (DataSnapshot uniqueKey : dataSnapshot.getChildren()) {
                    if (uniqueKey.getValue(Long.class) == 1) {
                        counter++;
                    }
                }
                Log.d("COBA LIKE", String.valueOf(counter));
                Log.d("Likelist di Home", likeList.size() + "");
                Log.d("Item count in home", postAdapter.getItemCount() + "");
                Log.d("Member name", name);

                if (!onChange) {
                    likeList.add(counter);
                    memberNameList.add(name);

                    Log.d("TIDAK NULL", mPosting.getDeskripsi());
                    postIdList.add(key);
                    postingList.add(mPosting);
                    Log.d("Posting List", postingList.toString());

                    Log.e("Post count", postCount + "");
                    Log.e("size", postingList.size() + "");
                    if (postingList.size()  == postCount) {
                        Log.d("MASUK", "");
                        onChange = !onChange;
                    }
                }
                    postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void fetchPost() {
        final DatabaseReference mRefPosting = database.getReference("posting");
        final DatabaseReference mRefProfil = database.getReference("member");

        mRefPosting.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshotPosting) {
                likeList.clear();
                memberNameList.clear();
                postingList.clear();
                for (final DataSnapshot uniqueKeySnapshot : dataSnapshotPosting.getChildren()) {
                    Log.d("Children post count", dataSnapshotPosting.getChildrenCount() + "");

                    final Posting mPosting = uniqueKeySnapshot.getValue(Posting.class);
                    Log.d("M Posting", mPosting.getDeskripsi());
                    Log.d("Key", uniqueKeySnapshot.getKey());

                    Log.d("COBA1", uniqueKeySnapshot.toString());
                    mRefProfil.child(mPosting.getMemberId()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Member mMember = dataSnapshot.getValue(Member.class);
                            Log.d("COBA", uniqueKeySnapshot.toString());
                            Log.d("COBADataDiri", mMember.getName());
                            Log.d("COBA", "onDataChange: " + mPosting.getDeskripsi());

                            getLike(dataSnapshotPosting.getChildrenCount(), uniqueKeySnapshot.getKey(), mPosting, uniqueKeySnapshot.getKey(), mMember.getName());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout_menu_icon) {
            firebaseAuth.signOut();
            Toast.makeText(this, "Anda berhasil Logout", Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void createPost(View v) {
        Intent postIntent = new Intent(this, PostInformasiActivity.class);
        startActivity(postIntent);
    }
}
