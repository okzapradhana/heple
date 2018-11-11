package com.example.allysaas.heple;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.example.allysaas.heple.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = database.getReference();

    private List<Post> postList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;

    AlertDialog.Builder builder;
    private String[] sortOptions = {"Ascending", "Descending"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firebaseAuth = FirebaseAuth.getInstance();
        fetchPosts();
        updatePostWhenUpdateOccurs();
        recyclerView = findViewById(R.id.recycler_view);
        Log.d("Size", "" + postList.size());
        postAdapter = new PostAdapter(HomeActivity.this, postList);
        recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(postAdapter);
    }

    public void fetchPosts() {
        dbRef.child("posting").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post item = snapshot.getValue(Post.class);

                    if (item != null) {
                        Log.d("a", item.getTime().toString());
                        postList.add(new Post(item.getPostId(), item.getAuthorId(), item.getAuthorMail(), item.getNama(), item.getAlamat(), item.getLokasiBerjualan(), item.getPetaLokasi(), item.getNoHp(), item.getTime(), item.getStatusBerjualan(), item.getDeskripsi(), item.getPhotoUrl(), item.getLike()));
                        Log.d("Postlis", "" + postList.size());
                    }
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Database Error", databaseError.toException().toString());
            }
        });
    }

    public void updatePostWhenUpdateOccurs() {
        dbRef.child("posting").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Database Error", databaseError.toException().toString());
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
        } else if (item.getItemId() == R.id.sort_menu_icon) {
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Sort Like By").setIcon(R.drawable.sortlike).setItems(sortOptions, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(i == 0){
                        //ASCENDING: DEFAULT
                        Log.d("Ascending", "Sorted Ascending");

                    } else if(i == 1){
                        //DESCENDING: NON DEFAULT
                        Log.d("Descending", "Sorted Descending");

                    }
                }
            }).create().show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void sortBasedOnLike() {

    }

    public void createPost(View v) {
        Intent postIntent = new Intent(this, PostInformasiActivity.class);
        startActivity(postIntent);
    }
}
