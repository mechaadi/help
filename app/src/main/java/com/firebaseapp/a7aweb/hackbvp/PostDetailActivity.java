package com.firebaseapp.a7aweb.hackbvp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import com.firebaseapp.a7aweb.hackbvp.models.User;
import com.firebaseapp.a7aweb.hackbvp.models.Comment;
import com.firebaseapp.a7aweb.hackbvp.models.Post;

import java.util.ArrayList;
import java.util.List;

//import aadi.com.firebase2.BaseActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PostDetailActivity";

    public static final String EXTRA_POST_KEY = "post_key";

    private DatabaseReference mPostReference;
    private DatabaseReference mCommentsReference;
    private ValueEventListener mPostListener;
    private String mPostKey;
    private CommentAdapter mAdapter;

    private TextView mAuthorView;
    private TextView mTitleView;
    private TextView fullname;
    private TextView mBodyView;
    private EditText mCommentField;
    private TextView mLinkView;
    private Button mCommentButton;
    private CircleImageView mAuthorPic;
    public TextView achi, details;
    public ImageView postpic;
    public CardView cardView;


    private RecyclerView mCommentsRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);


        // Get post key from intent
        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }

        // Initialize Database
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("posts").child(mPostKey);
        mCommentsReference = FirebaseDatabase.getInstance().getReference()
                .child("post-comments").child(mPostKey);

        // Initialize Views
        mAuthorView = findViewById(R.id.post_author);
        mLinkView = findViewById(R.id.post_links);
        mTitleView = findViewById(R.id.post_title);
        achi = (TextView) findViewById(R.id.details);
        details = (TextView) findViewById(R.id.details1);
        mAuthorPic = findViewById(R.id.post_author_photo);
        mBodyView = findViewById(R.id.post_body);
        mCommentField = findViewById(R.id.field_comment_text);
        mCommentButton = findViewById(R.id.button_post_comment);
        mCommentsRecycler = findViewById(R.id.recycler_comments);
        fullname = findViewById(R.id.fullname);

        cardView = (CardView) findViewById(R.id.postImageCard);

        postpic = (ImageView) findViewById(R.id.postImage);


        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference uid = ref.child("posts").child(mPostKey).child("uid");
        uid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String uidS = dataSnapshot.getValue().toString();

                DatabaseReference fullnameS = ref.child("Users").child(uidS).child("name");
                fullnameS.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       final String fullnameString = dataSnapshot.getValue().toString();

                        DatabaseReference usernameS = ref.child("Users").child(uidS).child("username");
                        usernameS.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String UsernameString = dataSnapshot.getValue().toString();

                                String name = getColoredSpanned(fullnameString, "#ffffff");
                                // String usernameS = dataSnapshot.getValue().toString();
                                String surName = getColoredSpanned(UsernameString,"#5fbff9");
                                String at = getColoredSpanned("@", "#5fbff9" );


                                fullname.setText(Html.fromHtml(name+" " + at +surName));

                                DatabaseReference userDetails = FirebaseDatabase.getInstance().getReference().child("Users").child(uidS).child("achievements");
                                userDetails.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        try {
                                            String occ = dataSnapshot.child("occ").getValue().toString();
                                            String kcl = dataSnapshot.child("kcl").getValue().toString();
                                            String interests = dataSnapshot.child("Interests").getValue().toString();
                                            //  Log.d("msg123", kcl);
                                            details.setText(occ);
                                            String hash = getColoredSpanned("#", "#5fbff9" );
                                            String interestsC = getColoredSpanned(interests,"#5fbff9");

                                            achi.setText(Html.fromHtml( kcl + "    "+hash+interestsC));
                                            // Toast.makeText(ctx, kcl, Toast.LENGTH_LONG).show();


                                            // achi.setText(occ + " . " +  kcl + " . " + interests);
                                        }catch (Exception e){

                                        }finally {
                                            //achi.setText("No user data available");
                                        }


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

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mCommentButton.setOnClickListener(this);
        mCommentsRecycler.setLayoutManager(new LinearLayoutManager(this));



    }

    @Override
    public void onStart() {
        super.onStart();
        final Context mContext1 = this;
        // Add value event listener to the post
        // [START post_value_event_listener]
        ValueEventListener postListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                final Post post = dataSnapshot.getValue(Post.class);

                // [START_EXCLUDE]
                mAuthorView.setText(post.author);
                mTitleView.setText(post.title);
                mBodyView.setText(post.body);
                mLinkView.setText(post.url);

                if (TextUtils.isEmpty(post.postpic)){
                    postpic.setVisibility(View.GONE);
                }

                else {
                    postpic.setVisibility(View.VISIBLE);
                    cardView.setVisibility(View.VISIBLE);
                    Picasso.get().load(post.postpic).into(postpic);
                }

                mLinkView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      //  Intent intent = new Intent(PostDetailActivity.this, Gmail.class);
                        String uri = mLinkView.getText().toString();

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(browserIntent);


                    }
                });

                mAuthorView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // Intent intent = new Intent(mContext1, ProfileActivity.class);
                       // intent.putExtra(ProfileActivity.EXTRA_POST_KEY, post.uid);
                        //mContext1.startActivity(intent);
                    }
                });

                mAuthorPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      /*  Intent intent = new Intent(mContext1, ProfileActivity.class);
                        intent.putExtra(ProfileActivity.EXTRA_POST_KEY, post.uid);
                        mContext1.startActivity(intent);*/
                    }
                });

                DatabaseReference pic = FirebaseDatabase.getInstance().getReference().child("Users").child(post.uid)
                        .child("dp");
                pic.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       String pic = dataSnapshot.getValue().toString();
                        Picasso.get().load(pic).placeholder(R.drawable.ic_action_account_circle_40).into(mAuthorPic);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(PostDetailActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mPostReference.addValueEventListener(postListener);
        // [END post_value_event_listener]

        // Keep copy of post listener so we can remove it when app stops
        mPostListener = postListener;

        // Listen for comments
        mAdapter = new CommentAdapter(this, mCommentsReference);
        mCommentsRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove post value event listener
        if (mPostListener != null) {
            mPostReference.removeEventListener(mPostListener);
        }

        // Clean up comments listener
        mAdapter.cleanupListener();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_post_comment) {
            postComment();
        }
    }

    private void postComment() {
        final String uid = getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user information
                        User user = dataSnapshot.getValue(User.class);
                        String authorName = user.name;
                        String dp = user.dp;
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        // Create new comment object
                        String commentText = mCommentField.getText().toString();
                        Comment comment = new Comment(uid, authorName, commentText);

                        // Push the comment, it will appear in the list
                        mCommentsReference.push().setValue(comment);

                        // Clear the field
                        mCommentField.setText(null);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private static class CommentViewHolder extends RecyclerView.ViewHolder {

        public TextView authorView;
        public TextView bodyView;
        public CircleImageView imageView;

        public CommentViewHolder(View itemView) {
            super(itemView);

            authorView = itemView.findViewById(R.id.comment_author);
            bodyView = itemView.findViewById(R.id.comment_body);
            imageView = itemView.findViewById(R.id.comment_photo);

        }
    }

    private static class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {

        private Context mContext;
        private DatabaseReference mDatabaseReference;
        private ChildEventListener mChildEventListener;

        private List<String> mCommentIds = new ArrayList<>();
        private List<Comment> mComments = new ArrayList<>();

        public CommentAdapter(final Context context, DatabaseReference ref) {
            mContext = context;
            mDatabaseReference = ref;

            // Create child event listener
            // [START child_event_listener_recycler]
            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                    // A new comment has been added, add it to the displayed list
                    Comment comment = dataSnapshot.getValue(Comment.class);

                    // [START_EXCLUDE]
                    // Update RecyclerView
                    mCommentIds.add(dataSnapshot.getKey());
                    mComments.add(comment);
                    notifyItemInserted(mComments.size() - 1);
                    // [END_EXCLUDE]
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so displayed the changed comment.
                    Comment newComment = dataSnapshot.getValue(Comment.class);
                    String commentKey = dataSnapshot.getKey();

                    // [START_EXCLUDE]
                    int commentIndex = mCommentIds.indexOf(commentKey);
                    if (commentIndex > -1) {
                        // Replace with the new data
                        mComments.set(commentIndex, newComment);

                        // Update the RecyclerView
                        notifyItemChanged(commentIndex);
                    } else {
                        Log.w(TAG, "onChildChanged:unknown_child:" + commentKey);
                    }
                    // [END_EXCLUDE]
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so remove it.
                    String commentKey = dataSnapshot.getKey();

                    // [START_EXCLUDE]
                    int commentIndex = mCommentIds.indexOf(commentKey);
                    if (commentIndex > -1) {
                        // Remove data from the list
                        mCommentIds.remove(commentIndex);
                        mComments.remove(commentIndex);

                        // Update the RecyclerView
                        notifyItemRemoved(commentIndex);
                    } else {
                        Log.w(TAG, "onChildRemoved:unknown_child:" + commentKey);
                    }
                    // [END_EXCLUDE]
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                    // A comment has changed position, use the key to determine if we are
                    // displaying this comment and if so move it.
                    Comment movedComment = dataSnapshot.getValue(Comment.class);
                    String commentKey = dataSnapshot.getKey();

                    // ...
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                    Toast.makeText(mContext, "Failed to load comments.",
                            Toast.LENGTH_SHORT).show();
                }
            };
            ref.addChildEventListener(childEventListener);
            // [END child_event_listener_recycler]

            // Store reference to listener so it can be removed on app stop
            mChildEventListener = childEventListener;
        }

        @Override
        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.item_comment, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final CommentViewHolder holder, int position) {
            final Comment comment = mComments.get(position);
            holder.authorView.setText(comment.author);
            holder.bodyView.setText(comment.text);

            DatabaseReference pic = FirebaseDatabase.getInstance().getReference().child("Users").child(comment.uid)
                    .child("dp");
            pic.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   // String pic = dataSnapshot.getValue().toString();
                  //  Picasso.get().load(pic).placeholder(R.drawable.ic_action_account_circle_40).into(holder.imageView);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /*Intent intent = new Intent(mContext, ProfileActivity.class);
                    intent.putExtra(ProfileActivity.EXTRA_POST_KEY, comment.uid);
                    mContext.startActivity(intent);*/

                }
            });
        }

        @Override
        public int getItemCount() {
            return mComments.size();
        }

        public void cleanupListener() {
            if (mChildEventListener != null) {
                mDatabaseReference.removeEventListener(mChildEventListener);
            }
        }

    }

    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}