package com.firebaseapp.a7aweb.hackbvp.fragments;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class RecentPostFragment extends timeline {

    public RecentPostFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // [START recent_posts_query]
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        Query recentPostsQuery = databaseReference.child("posts")
                .limitToFirst(1000);
        // [END recent_posts_query]

        return recentPostsQuery;
    }
}
