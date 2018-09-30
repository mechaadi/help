package com.firebaseapp.a7aweb.hackbvp.models;


import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String name;
    public String email;
    public String dp;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String email, String dp) {
        this.name = name;
        this.email = email;
        this.dp = dp;
    }

}
// [END blog_user_class]
