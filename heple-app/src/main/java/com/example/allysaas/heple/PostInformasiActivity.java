package com.example.allysaas.heple;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.allysaas.heple.model.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class PostInformasiActivity extends AppCompatActivity {

    EditText nama, alamat, lokasi, urlPeta, noHp, deskripsi;
    ImageView choosenPhoto;
    RadioButton menetap, berkeliling;
    boolean choosen;
    String status;
    private int PICK_IMAGE_REQUEST = 1;
    String photoUrl, uid, authorMail;

    Uri file;
    FirebaseStorage storage;
    StorageReference storageReference;

    FirebaseDatabase database;
    DatabaseReference ref;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_informasi);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user != null) {
            uid = user.getUid();
            authorMail = user.getEmail();
            Log.d("User", uid);
        } else {
            Log.d("Check User", user.toString());
        }

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        nama = findViewById(R.id.nama_penjual);
        alamat = findViewById(R.id.alamat);
        lokasi = findViewById(R.id.lokasi_berjualan);
        urlPeta = findViewById(R.id.peta_lokasi);
        noHp = findViewById(R.id.no_hp);
        deskripsi = findViewById(R.id.deskripsi);
        menetap = findViewById(R.id.radio_menetap);
        berkeliling = findViewById(R.id.radio_berkeliling);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post_informasi, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.post_menu_icon) {
            uploadPic();
        }
        return super.onOptionsItemSelected(item);
    }

    private void postInformasi() {

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        String postId = FirebaseDatabase.getInstance().getReference().push().getKey();

        Post post = new Post(postId, uid, authorMail,
                nama.getText().toString(), alamat.getText().toString(), lokasi.getText().toString(),
                urlPeta.getText().toString(), noHp.getText().toString(), status, deskripsi.getText().toString(), photoUrl);

        if (validate()) {
            ref.child("posting").child(postId).setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(PostInformasiActivity.this, "Berhasil post", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PostInformasiActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostInformasiActivity.this, "Gagal post", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    public boolean validate() {
        if (nama.getText().toString().isEmpty() || alamat.getText().toString().isEmpty()
                || lokasi.getText().toString().isEmpty() || urlPeta.getText().toString().isEmpty()
                || noHp.getText().toString().isEmpty() || deskripsi.getText().toString().isEmpty() || choosenPhoto == null
                || status.isEmpty()
                ) {
            Toast.makeText(this, "Form yang diisi belum lengkap", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void openMap(View v) {
        if (ContextCompat.checkSelfPermission(PostInformasiActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            Uri gmmIntentUri = Uri.parse("geo:0,0");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }
    }

    public void chooseStatusBerjualan(View v) {
        choosen = ((RadioButton) v).isChecked();

        switch (v.getId()) {
            case R.id.radio_menetap:
                if (choosen) {
                    status = "Menetap";
                }
                Log.d("Status jual", status);
                break;
            case R.id.radio_berkeliling:
                if (choosen) {
                    status = "Berkeliling";
                }
                Log.d("Status jual", status);
                break;
            default:
                status = "";
        }
    }

    public void choosePhoto(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            file = data.getData();
            Log.d("File Log", file.toString());

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), file);
                choosenPhoto = findViewById(R.id.photo);
                choosenPhoto.setImageBitmap(bitmap);
                choosenPhoto.getLayoutParams().height = 700;
                choosenPhoto.getLayoutParams().width = 600;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadPic() {

        if (file != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Sedang mengupload...");
            progressDialog.show();

            final StorageReference storageRef = storageReference.child("images/user/" + uid + "/" + file.getLastPathSegment());
            storageRef.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(PostInformasiActivity.this, "Gambar berhasil diupload", Toast.LENGTH_SHORT).show();
                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.d("Uri Photo", uri.toString());
                                    photoUrl = uri.toString();
                                    postInformasi();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(PostInformasiActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploading " + (int) progress + "%");
                        }
                    });
        }
    }
}
