package com.example.allysaas.heple;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.allysaas.heple.model.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LihatDeskripsiActivity extends AppCompatActivity implements View.OnClickListener {

    String authorId, authorMail, nama_penjual, image, desc, postId;
    Long time;
    int like;
    Map<String, Object> komentar = new HashMap<>();

    FirebaseDatabase database;
    DatabaseReference ref;

    TextView author, waktu, namapenjual, totalLike, deskripsi;
    EditText comment;
    ImageView gambarPenjual, buttonComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_deskripsi);

        Intent intent = getIntent();
        authorId = intent.getStringExtra("authorId");
        Log.d("Author Id", authorId);
        authorMail = intent.getStringExtra("authorMail");
        time = Long.parseLong(intent.getStringExtra("time"));
        nama_penjual = intent.getStringExtra("nama_penjual");
        image = intent.getStringExtra("image");
        like = Integer.parseInt(intent.getStringExtra("like"));
        desc = intent.getStringExtra("deskripsi");
        postId = intent.getStringExtra("postId");
        Log.d("Post id", postId);

        author = findViewById(R.id.author_lihatdeskripsi);
        waktu = findViewById(R.id.time_lihatdeskripsi);
        namapenjual = findViewById(R.id.nama_penjual_lihatdeskripsi);
        totalLike = findViewById(R.id.like_lihatdeskripsi);
        gambarPenjual = findViewById(R.id.gambar_penjual_lihatdeskripsi);
        deskripsi = findViewById(R.id.deskripsi_lihatdeskripsi);
        comment = findViewById(R.id.comment);
        buttonComment = findViewById(R.id.button_comment);
        buttonComment.setOnClickListener(this);

        author.setText(authorMail);
        waktu.setText(time.toString());
        namapenjual.setText(nama_penjual);
        totalLike.setText("" + like);
        Glide.with(this).load(image).into(gambarPenjual);
        deskripsi.setText(desc);
    }

    public void tambahKomentar(){
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        String komentarId = FirebaseDatabase.getInstance().getReference("posting/" + this.postId + "/" + "komentar").push().getKey();
        komentar.put("id_member", authorId);
        komentar.put("isi_komentar", comment.getText().toString());

        ref.child("posting").child(postId).child("komentar").child(komentarId).setValue(komentar).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(LihatDeskripsiActivity.this, "Berhasil komentar", Toast.LENGTH_SHORT);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LihatDeskripsiActivity.this, "Gagal komentar", Toast.LENGTH_SHORT);
            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == buttonComment.getId()){
            tambahKomentar();
        }
    }

    public void fetchKomentar(){

    }


}
