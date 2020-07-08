package com.example.newcomer_io.ui.main.SignIn;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.newcomer_io.MainActivity;
import com.example.newcomer_io.R;
import com.example.newcomer_io.ui.main.Onboarding.OnboardingLogistics;
import com.example.newcomer_io.ui.main.Onboarding.ProfileInformation;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.*;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static com.example.newcomer_io.ui.main.UserDetails.UserData.jsonToMap;

public class SignIn extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;
    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private LinearLayout googleButton;
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        mAuth = FirebaseAuth.getInstance();
/*        if (account != null){
            firebaseAuthWithGoogle(account.getIdToken());
       }*/
        googleButton = findViewById(R.id.googleButton);

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }

    private void signIn() {
        Intent signInIntent = getmGoogleSignInClient().getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("fireabse_auth_google", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("fireabse_auth_google", "Google sign in failed", e);
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("vsignInWithCredential", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //Uri photoUrl = user.getPhotoUrl();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("signInWithCredential", "signInWithCredential:failure", task.getException());
                           updateUI(null);
                        }
                        // ...
                    }
                });
    }

    private void updateUI(final FirebaseUser user) {
        //Chck with the database before authenticating to the next step
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("UserData/" + user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null){
                    //Then the user doesn't exist and we send them to the login screen
                    JSONObject user_data = new JSONObject();
                    JSONObject containerf = new JSONObject();

                    try {
                        user_data.put("Name", user.getDisplayName());
                        containerf.put(user.getUid(),user_data);
                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
                        reference1.child("UserData").updateChildren(jsonToMap(containerf));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(SignIn.this, OnboardingLogistics.class);
                    //String photoUrl = userData.getPhotoUrl().toString();
                    String name = user.getDisplayName();
                    String uuid = user.getUid();

                    intent.putExtra("Name", name);
                    intent.putExtra("Uuid",uuid);

                    startActivity(intent);

                    //startActivityIntent(OnboardingLogistics.class, user);
                }
                else{
                    //Then we go to another part of the UI which is the default launch page
                    //Then we pass in the image as well which will require calling the Firebase storage
                    String first_name = dataSnapshot.child("First Name").getValue().toString();
                    String last_name = dataSnapshot.child("Last Name").getValue().toString();
                    String school_name = dataSnapshot.child("School Name").getValue().toString();
                    String image_name = dataSnapshot.child("Image Name").getValue().toString();

                    Intent intent = new Intent(SignIn.this, MainActivity.class);
                    //String photoUrl = userData.getPhotoUrl().toString();
                    String name = user.getDisplayName();
                    String uuid = user.getUid();

                    intent.putExtra("Name", name);
                    intent.putExtra("Uuid",uuid);
                    intent.putExtra("First Name",first_name);
                    intent.putExtra("Last Name",last_name);
                    intent.putExtra("School Name",school_name);
                    intent.putExtra("Image Name",image_name);
                    intent.putExtra("ACTIVITY_NAME", "SignIn");

                    startActivity(intent);

                    //startActivityIntent(MainActivity.class,user);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

    private void startActivityIntent(Type classT, FirebaseUser userData) {
        Intent intent = new Intent(SignIn.this, (Class<?>) classT);
        //String photoUrl = userData.getPhotoUrl().toString();
        String name = userData.getDisplayName();
        String uuid = userData.getUid();

        intent.putExtra("Name", name);
        intent.putExtra("Uuid",uuid);
        //intent.putExtra
        //intent.putExtra("Profile Image", (Parcelable) imageData);
        //intent.putExtra("PhotoUrl", photoUrl);
        //intent.putExtra("User", userData);
        startActivity(intent);
    }

    private GoogleSignInClient getmGoogleSignInClient(){return this.mGoogleSignInClient; }

    public LinearLayout getGoogleButton() {
        return googleButton;
    }

    public void setGoogleButton(LinearLayout googleButton) {
        this.googleButton = googleButton;
    }

    public FirebaseStorage getFirebaseStorage() {
        return firebaseStorage;
    }
}

