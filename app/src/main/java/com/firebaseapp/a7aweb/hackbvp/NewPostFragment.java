package com.firebaseapp.a7aweb.hackbvp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.firebaseapp.a7aweb.hackbvp.models.Post;
import com.firebaseapp.a7aweb.hackbvp.models.User;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;
//import static com.chootdev.csnackbar.Snackbar.show;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewPostFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewPostFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public String dp;
    Context ctx;

    private static final String TAG = "NewPostActivity";
    private static final String REQUIRED = "Required";

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    private EditText mTitleField, mLinkField;
    private EditText mBodyField;
    private TextView name, addphotos, addtags, addlocations;
    private ImageView postPic;
    int isPhoto = 0;   //no photo
    private CircleImageView propic;
    private FloatingActionButton mSubmitButton, mGetPhoto;
    Uri fileUriS;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public NewPostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewPostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewPostFragment newInstance(String param1, String param2) {
        NewPostFragment fragment = new NewPostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_post, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setContentView(R.layout.activity_settings);

        View v = getView();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        name = (TextView) v.findViewById(R.id.name);
        propic = (CircleImageView) v.findViewById(R.id.dp);
        addphotos = (TextView) v.findViewById(R.id.addPhotos);
        addlocations = (TextView) v.findViewById(R.id.addLocation);
        addtags = (TextView) v.findViewById(R.id.tags);





        String uid = mUser.getUid();

        DatabaseReference userData = mDatabase.child("Users").child(uid);

        userData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nameS = dataSnapshot.child("name").getValue().toString();
               // String username = dataSnapshot.child("username").getValue().toString();
                String dplink = dataSnapshot.child("dp").getValue().toString();


                //String usernameC = getColoredSpanned(username,"#5fbff9");
                String at = getColoredSpanned("@", "#5fbff9" );
                name.setText(Html.fromHtml("posting as  " + nameS + "  "));
                // fullname.setText(Html.fromHtml(name+" " + at +surName));

                Picasso.get().load(dplink).placeholder(R.drawable.common_full_open_on_phone).into(propic);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mTitleField = v.findViewById(R.id.field_title);
        mBodyField = v.findViewById(R.id.field_body);
        mLinkField = v.findViewById(R.id.field_links);

        mSubmitButton = v.findViewById(R.id.fab_submit_post);
        mGetPhoto = v.findViewById(R.id.fab_get_photo);


        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });
        addphotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)

                        .setMaxCropResultSize(3000,3000)
                        .start(getActivity());
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ProgressDialog progressDialog;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        final String userS = user.getUid();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Uploading image");
        progressDialog.setMessage("please wait while we upload your picture");
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                isPhoto = 1;   //photo available
                File file = new File(resultUri.getPath());



                try {
                    File imageBitmap = new Compressor(getActivity()).setQuality(40).compressToFile(file);
                    Uri fileuri = Uri.fromFile(new File(imageBitmap.getPath()));
                    //  Picasso.get().load(fileuri).placeholder(R.drawable.default_avata).into(pic);

                    if (TextUtils.isEmpty(fileuri.toString())){
                        String emp = "empty";
                        fileUriS = Uri.parse(emp);
                    }
                    else {
                        fileUriS = fileuri;
                    }
                    Toast.makeText(getActivity(), fileUriS.toString(), Toast.LENGTH_LONG).show();

                }catch (IOException e) {
                    e.printStackTrace();
                };

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void submitPost() {

        final String title = mTitleField.getText().toString();
        final String body = mBodyField.getText().toString();
        final String url = mLinkField.getText().toString();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("image");

        DatabaseReference pickpref = ref;

        pickpref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                dp = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // Title is required
        if (TextUtils.isEmpty(title)) {
            mTitleField.setError(REQUIRED);
            return;
        }

       /* if (TextUtils.isEmpty(url)) {
            mTitleField.setError(REQUIRED);
            return;
        }*/

        // Body is required
        if (TextUtils.isEmpty(body)) {
            mBodyField.setError(REQUIRED);
            return;
        }

        // Disable button so there are no multi-posts
        setEditingEnabled(false);
        Toast.makeText(getActivity(), "Posting...", Toast.LENGTH_SHORT).show();

        // [START single_value_read]
        final String userId = FirebaseAuth.getInstance().getUid();
        mDatabase.child("Users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(getActivity(),
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                            if (isPhoto == 1) {
                                writeNewPost(userId, user.name, title, body, url, dp, fileUriS);
                            }
                            else if (isPhoto == 0) {
                                writeNewPostWithoutPic(userId, user.name, title, body, url, dp);
                            }

                        }

                        // Finish this Activity, back to the stream
                        setEditingEnabled(true);
                        // finish();
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        setEditingEnabled(true);
                        // [END_EXCLUDE]
                    }
                });
        // [END single_value_read]
    }

    private void writeNewPostWithoutPic(String userId, String username, String title, String body, String url, String dp) {

        String key = mDatabase.child("posts").push().getKey();
        Post post = new Post(userId, username, title, body, url, dp, "");
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }

    private void setEditingEnabled(boolean enabled) {
        mTitleField.setEnabled(enabled);
        mBodyField.setEnabled(enabled);
        mLinkField.setEnabled(enabled);
        if (enabled) {
         //   mSubmitButton.setVisibility(View.VISIBLE);
        } else {
          //  mSubmitButton.setVisibility(View.GONE);
        }
    }



    // [START write_fan_out]
    private void writeNewPost(final String userId, final String username, final String title, final String body, final String url, final String dp, Uri postpic) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        final String key = mDatabase.child("posts").push().getKey();

        if (!fileUriS.equals("empty")) {
            final StorageReference ref = FirebaseStorage.getInstance().getReference().child("posts").child(key).child(postpic.toString());
            ref.putFile(postpic).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String downloadurl = uri.toString();
                            Post post = new Post(userId, username, title, body, url, dp, downloadurl);
                            Map<String, Object> postValues = post.toMap();

                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put("/posts/" + key, postValues);
                            childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

                            mDatabase.updateChildren(childUpdates);
                        }
                    });
                }
            });
        }
        else
        {
            Post post = new Post(userId, username, title, body, url, dp, "");
            Map<String, Object> postValues = post.toMap();

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/posts/" + key, postValues);
            childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

            mDatabase.updateChildren(childUpdates);
        }

    }
    // [END write_fan_out]

    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

}