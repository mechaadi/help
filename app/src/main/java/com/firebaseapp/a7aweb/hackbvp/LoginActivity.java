package com.firebaseapp.a7aweb.hackbvp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {


    private EditText email, password;
    private Button login;
    private FirebaseDatabase mref;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        login = (Button) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Please wait...");
                progressDialog.setMessage("Logging you in...");
                progressDialog.show();

                mAuth = FirebaseAuth.getInstance();
                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Login successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, begin.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }

                        else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}
