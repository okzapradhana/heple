package com.example.allysaas.heple;

import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button loginButton;
    TextView email, password;
    FirebaseAuth mAuth;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = findViewById(R.id.btnLogin);
        loginButton.setOnClickListener(this);
        email = findViewById(R.id.email);
        firebaseAuth = FirebaseAuth.getInstance();
        password = findViewById(R.id.password);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            finish();
            Intent successIntentToHome = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(successIntentToHome);
        }
    }

    public void goToRegister(View v) {

        Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(myIntent);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == loginButton.getId()) {
            Log.d("Email", email.getText().toString());
            Log.d("Pass", password.getText().toString());
            login(email.getText().toString(), password.getText().toString());
        }
    }

    public void login(String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("Login", "Cek succesfully task" + task.isSuccessful());
                if (!task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_LONG);
                } else {
                    Toast.makeText(LoginActivity.this, "Anda berhasil Login", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent successIntentToHome = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(successIntentToHome);
                }
            }
        });
    }


}
