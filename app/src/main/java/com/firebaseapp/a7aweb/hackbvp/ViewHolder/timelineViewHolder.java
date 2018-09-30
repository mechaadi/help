package com.firebaseapp.a7aweb.hackbvp.ViewHolder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebaseapp.a7aweb.hackbvp.R;
import com.firebaseapp.a7aweb.hackbvp.models.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class timelineViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView fullname;
    public ImageView share;
    public TextView authorView;
    public TextView urlView;
    public ImageView starView;
    public TextView numStarsView;
    public TextView bodyView;
    public CircleImageView dp;
    public ImageView postpic;
    public TextView achi, details;
    public CardView cardView;

    public timelineViewHolder(@NonNull View itemView) {
        super(itemView);

        titleView = itemView.findViewById(R.id.post_title);
        authorView = itemView.findViewById(R.id.post_author);
        starView = itemView.findViewById(R.id.star);
        numStarsView = itemView.findViewById(R.id.post_num_stars);
        bodyView = itemView.findViewById(R.id.post_body);
        urlView = itemView.findViewById(R.id.post_links);
        dp = itemView.findViewById(R.id.post_author_photo);
        fullname = itemView.findViewById(R.id.fullname);
        achi = (TextView) itemView.findViewById(R.id.details);
        details = (TextView) itemView.findViewById(R.id.details1);
        cardView = (CardView) itemView.findViewById(R.id.postImageCard);

        share = (ImageView) itemView.findViewById(R.id.share);
        postpic = (ImageView) itemView.findViewById(R.id.postImage);




    }

    public void bindToPost(final Post post, View.OnClickListener starClickListener) {
        titleView.setText(post.title);
        authorView.setText("@" + post.author);
        urlView.setText(post.url);
        numStarsView.setText(String.valueOf(post.starCount));
        bodyView.setText(post.body);

        DatabaseReference pic = FirebaseDatabase.getInstance().getReference().child("Users").child(post.uid).child("dp");
        pic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String dplink = dataSnapshot.getValue().toString();
                Picasso.get().load(dplink).placeholder(R.drawable.ic_action_account_circle_40).into(dp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        DatabaseReference email = FirebaseDatabase.getInstance().getReference().child("Users").child(post.uid).child("Email");
        email.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              String email = dataSnapshot.getValue().toString();
              details.setText(email + "\n" + "+91860XXX6260");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        DatabaseReference names = FirebaseDatabase.getInstance().getReference().child("Users").child(post.uid).child("name");
        names.addValueEventListener(new ValueEventListener() {




    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        final String nameS = dataSnapshot.getValue().toString();
        DatabaseReference username = FirebaseDatabase.getInstance().getReference().child("Users").child(post.uid).child("username");
        username.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        final String name = getColoredSpanned(nameS, "#ffffff");

        String usernameS  = dataSnapshot.getValue().toString();
        final String username = getColoredSpanned(usernameS, "#17869F");

        fullname.setText(Html.fromHtml(name + "   @" + username));
        //avb

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});

    }
    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }
}
