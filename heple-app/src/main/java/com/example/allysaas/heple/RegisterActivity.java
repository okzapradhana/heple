package com.example.allysaas.heple;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

import com.example.allysaas.heple.model.Member;
import com.google.firebase.auth.FirebaseAuth;
import android.util.Log;
import com.google.firebase.auth.AuthResult;
import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etUsername, etEmail, etPassword, etConfirmPassword;
    Button btnRegister;
    TextView txtLogin;

    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.editTextUsername);
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
        etConfirmPassword = findViewById(R.id.editTextConfirm);
        txtLogin = findViewById(R.id.textLogin);
        btnRegister = findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(this);
        txtLogin.setOnClickListener(this);
    }

    private void registerAccount(String email, String password) {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        final Task<AuthResult> authResultTask = mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        user = mAuth.getCurrentUser();

                        String uid = user.getUid();

                        Member member = new Member(user.getEmail());
                        ref.child("member").child(uid).setValue(member);

                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "register failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    boolean validasiRegister(){
        if(etUsername.getText().toString().isEmpty()|| etEmail.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty() || etConfirmPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "Form yang diisi belum lengkap",Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == btnRegister.getId()) {
            if (validasiRegister()) {
                registerAccount(etEmail.getText().toString(), etPassword.getText().toString());
            }
        } else if (v.getId() == txtLogin.getId()) {
            Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(myIntent);
        }
    }
}
