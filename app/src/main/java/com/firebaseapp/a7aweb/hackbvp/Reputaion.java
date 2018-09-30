package com.firebaseapp.a7aweb.hackbvp;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class Reputaion extends AppCompatActivity {


    private TextView name, Email;
    private CircleImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reputaion);

        image = (CircleImageView) findViewById(R.id.dp);
        name = (TextView) findViewById(R.id.name);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser  = mAuth.getCurrentUser();
        String uid = mUser.getUid();


        ref.child("Users").child(uid).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nameS = dataSnapshot.getValue().toString();
                name.setText(nameS);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference emailref = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Email");
        emailref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Email = (TextView) findViewById(R.id.email);

                String emailS = dataSnapshot.getValue().toString();
               // Email.setText(emailS);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference pic = ref.child("Users").child(uid).child("dp");
        pic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String picS = dataSnapshot.getValue().toString();
                Picasso.get().load(picS).placeholder(R.drawable.profile_placeholder).into(image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
