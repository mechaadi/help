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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private EditText name, email, password;
    private Button register, login;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = (EditText) findViewById(R.id.e_name);
        email = (EditText) findViewById(R.id.e_email);
        password = (EditText) findViewById(R.id.e_password);

        register = (Button) findViewById(R.id.register);
        login = (Button) findViewById(R.id.login);

        progressDialog = new ProgressDialog(this);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Please wait..");
                progressDialog.setMessage("creating user account..");
                progressDialog.show();
                final String nameS = name.getText().toString();
                final String emailS = email.getText().toString();
                final String passwordS = password.getText().toString();
                mAuth.createUserWithEmailAndPassword(emailS, passwordS)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "created", Toast.LENGTH_LONG).show();
                                    final FirebaseUser mUser = mAuth.getCurrentUser();
                                    mUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                final String username = usernameFromEmail(emailS);

                                                HashMap<String, String> user = new HashMap<>();
                                                user.put("name", nameS);
                                                user.put("Email", emailS);
                                                user.put("Password", passwordS);
                                                user.put("username", username);
                                                user.put("dp", " ");


                                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                                String uid = mUser.getUid();
                                                ref.child("Users").child(uid).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getApplicationContext(), "Data added", Toast.LENGTH_LONG).show();
                                                    }
                                                });

                                                Toast.makeText(getApplicationContext(), "Email sent", Toast.LENGTH_LONG)
                                                        .show();
                                                progressDialog.dismiss();

                                                if (mUser.isEmailVerified()) {
                                                    Intent intent = new Intent(MainActivity.this, begin.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        });
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(MainActivity.this, begin.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
}
