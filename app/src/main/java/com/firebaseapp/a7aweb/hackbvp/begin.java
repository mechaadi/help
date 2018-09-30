package com.firebaseapp.a7aweb.hackbvp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.firebaseapp.a7aweb.hackbvp.fragments.RecentPostFragment;
import com.firebaseapp.a7aweb.hackbvp.ml.textRecognition;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
//import com.firebaseapp.a7aweb.hackbvp.models.Experimentally;


public class begin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.timeline:
                    fragment = new RecentPostFragment();
                    loadFragment(fragment);
                    return true;

               /* case R.id.chat:
                    fragment = new NewPostFragment();
                    loadFragment(fragment);
                    return true;*/

                case R.id.post:
                    fragment = new NewPostFragment();
                    loadFragment(fragment);
                    return true;


                case R.id.person:
                    startActivity(new Intent(begin.this, Reputaion.class));
                    return true;

                /*case R.id.exp:
                    fragment = new Experimentally();
                    loadFragment(fragment);
                    return true;
*/

                /*case R.id.chat:
                    fragment = new NewPostFragment();
                    loadFragment(fragment);
                    return true;
*/


                /* case R.id.switch_notifications:
                 *//*  fragment = new RecentForumFragment();
                    loadFragment(fragment);
                    return true;*//*


                case R.id.profile:
                   *//* fragment = new profileSettings();
                    loadFragment(fragment);
                    return true;*/
            }

            return false;
        }
    };



    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);


        // Return true to display menu
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_logout:

                FirebaseAuth.getInstance().signOut();
               /* GoogleSignInClient mGoogleSignInClient;
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("660746747162-hie2995igv8uibbmsbq2f0jo3dq62mn1.apps.googleusercontent.com")
                        .requestEmail()
                        .build();*/


               /* mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // ...
                            }
                        });*/

                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;

          /*  case R.id.action_tutorial:

                startActivity(new Intent(this, LanguageSelection.class));
                break;*/

            case R.id.compiler:
                //startActivity(new Intent(this, Compiler.class));

            case R.id.youtube:
              //  startActivity(new Intent(this, VideoPlayer.class));

            case android.R.id.home:
//                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);

    }

}