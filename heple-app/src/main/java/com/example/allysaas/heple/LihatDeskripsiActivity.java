package com.example.allysaas.heple;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.example.allysaas.heple.adapter.KomentarAdapter;
import com.example.allysaas.heple.model.Komentar;
import com.example.allysaas.heple.model.Member;
import com.example.allysaas.heple.model.Posting;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LihatDeskripsiActivity extends AppCompatActivity implements View.OnClickListener {

    Long time;
    String member, postId, image, memberId, photoUriString, textToShare;
    List<Komentar> komentarList = new ArrayList<>();
    List<String> memberNameList = new ArrayList<>();

    FirebaseDatabase database;
    DatabaseReference ref;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;

    TextView memberName, waktu, namapenjual, deskripsi, alamat, petaLokasi, lokasiBerjualan, noHp, statusBerjualan;
    EditText comment;
    ImageView gambarPenjual, buttonComment, buttonShare;

    //DECLARE ADAPTER
    RecyclerView recyclerView;
    KomentarAdapter komentarAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_deskripsi);
        user = mAuth.getCurrentUser();
        memberId = user.getUid();
        preparingFindViewById();
        setOnClick();
        initSet();
        fetchKomentar();
        callAdapter();
    }

    public void preparingFindViewById(){
        memberName = findViewById(R.id.author_lihatdeskripsi);
        waktu = findViewById(R.id.time_lihatdeskripsi);
        alamat = findViewById(R.id.alamat_lihatdeskripsi);
        namapenjual = findViewById(R.id.value_nama_penjual_lihatdeskripsi);
        gambarPenjual = findViewById(R.id.gambar_penjual_lihatdeskripsi);
        deskripsi = findViewById(R.id.isi_deskripsi_lihatdeskripsi);
        comment = findViewById(R.id.comment);
        petaLokasi = findViewById(R.id.value_peta_lokasi_detaildeskripsi);
        lokasiBerjualan = findViewById(R.id.value_lokasi_berjualan_lihatdeskripsi);
        noHp = findViewById(R.id.value_no_hp_detaildeskripsi);
        statusBerjualan = findViewById(R.id.value_status_berjualan_detaildeskripsi);
        recyclerView = findViewById(R.id.komentar_recyclerview);
        buttonShare = findViewById(R.id.share_lihatdeskripsi);
        buttonComment = findViewById(R.id.button_comment);
    }

    public void setOnClick(){
        buttonShare.setOnClickListener(this);
        buttonComment.setOnClickListener(this);
    }

    public void callAdapter(){
        komentarAdapter = new KomentarAdapter(LihatDeskripsiActivity.this, komentarList, memberNameList);
        recyclerView.setLayoutManager(new LinearLayoutManager(LihatDeskripsiActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(komentarAdapter);

    }

    public void initSet(){
        Intent intent = getIntent();
        Posting posting = (Posting) intent.getSerializableExtra("postObject");
        member = intent.getStringExtra("memberName");
        Log.d("member in lihat desc", member);
        postId = intent.getStringExtra("postId");
        photoUriString = posting.getPhotoUrl();
        Log.d("Post id", postId);
        Log.d("User Id", member);

        memberName.setText(member);
        waktu.setText(DateUtils.getRelativeDateTimeString(this, posting.getTime() * 1000, DateUtils.SECOND_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL));
        alamat.setText(posting.getAlamat());
        namapenjual.setText(posting.getNamaPenjual());
        Glide.with(this).load(photoUriString).into(gambarPenjual);
        deskripsi.setText(posting.getDeskripsi());
        petaLokasi.setText(posting.getPetaLokasi());
        lokasiBerjualan.setText(posting.getLokasiBerjualan());
        noHp.setText(posting.getNoHp());
        statusBerjualan.setText(posting.getStatusBerjualan());
        deskripsi.setText(posting.getDeskripsi());
    }

    public void tambahKomentar() {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("komentar/" + postId);
        Komentar komentar = new Komentar(memberId, comment.getText().toString(), System.currentTimeMillis() / 1000);
        ref.push().setValue(komentar).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(LihatDeskripsiActivity.this, "Komentar berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                comment.getText().clear();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == buttonComment.getId()) {
            tambahKomentar();
        } else if (view.getId() == buttonShare.getId()) {
            sharePost();
        }
    }

    public void sharePost() {
        textToShare = "Yuk, bantu Beliau! Beliau bernama " + namapenjual.getText().toString() + " yang bertinggal di " + alamat.getText().toString()
        + " dan biasa berjualan di " + lokasiBerjualan.getText().toString() + ". \n\nBeliau biasanya " + statusBerjualan.getText().toString() + " dalam berjualan "
        + ". \n\nJika ingin menghubungi Beliau, dapat menghubungi di " + noHp.getText().toString()
        + " \n\nUntuk mendownload gambar, silakan klik " + photoUriString;

//        textToShare = namapenjual.getText().toString();

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
        shareIntent.setType("text/plain");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(shareIntent);
    }

    public void fetchKomentar() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mRefKomentar = firebaseDatabase.getReference("komentar/" + postId);
        final DatabaseReference mRefProfil = firebaseDatabase.getReference("member");

        mRefKomentar.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                komentarList.clear();
                memberNameList.clear();
                for (final DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                    Log.e("Children", dataSnapshot.getChildren().toString());
                    final Komentar dataKomentar = uniqueKeySnapshot.getValue(Komentar.class);
                    Log.e("Data komentar", dataKomentar.getIsiKomentar());

                    mRefProfil.child(dataKomentar.getMemberId()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Member member = dataSnapshot.getValue(Member.class);
                            Log.e("Member name", member.getName());
                            memberNameList.add(member.getName());
                            komentarList.add(dataKomentar);
                            Log.e("Size member name", String.valueOf(memberNameList.size()));
                            Log.e("Size komentar list", komentarList.size() + "");
                            komentarAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
