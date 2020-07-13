package com.example.newcomer_io.ui.main.FindFriends;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.newcomer_io.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

public class FindFriends extends AppCompatActivity {
    final static int USER_NAME = 1;
    final static int LOCATION = 2;
    final static int FOLLOW = 3;
    final static int PROFILE_IMAGE = 4;

    private DatabaseReference mDatabase;
    private EditText mSearch;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        //setUpRecyclerView();

        mSearch = findViewById(R.id.searchField);
        mSearch.setSingleLine(true);

        recyclerView =findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDatabase = FirebaseDatabase.getInstance().getReference().child("UserData");

        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                firebaseUserSearch(s.toString());


            }
        });

        /*mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = mSearch.getText().toString();
                firebaseUserSearch(searchText);
            }
        });*/
        /* LinearLayout scrollView = findViewById(R.id.scrollView);

        scrollView.addView(userProfileView);
        scrollView.addView(divider);*/
    }

    private void firebaseUserSearch(String searchText) {

        //String proper_format = convertToCamelFormat(searchText);
        //mSearch.setText(searchText);

        //String firstLetterCapital = searchText.substring(0, 1).toUpperCase() + searchText.substring(1);

        Query firebaseSearchQuery = mDatabase.orderByChild("FirstName").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerOptions<Friend> options=  new FirebaseRecyclerOptions.Builder<Friend>()
                .setLifecycleOwner(this)
                .setQuery(firebaseSearchQuery,Friend.class)
                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Friend, FriendHolder>(options) {

            @NonNull
            @Override
            public FriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                FriendHolder loading = new FriendHolder(new UserProfile().createUserProfileView("Loading", "", parent.getContext()));
                return loading;
            }

            @Override
            protected void onBindViewHolder(@NonNull FriendHolder holder, int position, @NonNull Friend model) {
                holder.setDetails(model.getFirstName(),model.getLocation(),model.getUuid(),model.getImageName());
            }
        };

        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public FirebaseStorage getFirebaseStorage() {
        return firebaseStorage;
    }

    public void setFirebaseStorage(FirebaseStorage firebaseStorage) {
        this.firebaseStorage = firebaseStorage;
    }

    class FriendHolder extends RecyclerView.ViewHolder{

        TextView userName;
        TextView location;
        TextView followButton;
        CircleImageView profileImage;

        public FriendHolder(View itemView){
            super(itemView);
            this.userName = itemView.findViewById(USER_NAME);
            this.location = itemView.findViewById(LOCATION);
            this.followButton = itemView.findViewById(FOLLOW);
            this.profileImage = itemView.findViewById(PROFILE_IMAGE);
        }
        public void setDetails(String name, String location,String uuid, String imagePath){
            this.userName.setText(name);
            this.location.setText(location);

            downloadImagePhoto(uuid,imagePath);

            this.followButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Then we will go to the user view intent
                }
            });
        }
        public TextView getFollowButton(){
            return this.followButton;
        }
        private void downloadImagePhoto(String uuid,String imageName) {

            StorageReference reference = getFirebaseStorage().getReference("User Images").child(uuid).child(imageName);
            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(profileImage);
                }
            });
        }
    }

    /* private void setUpRecyclerView(){
        Query query = users.orderBy("name", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Friend> options=  new FirestoreRecyclerOptions.Builder<Friend>()
                .setQuery(query,Friend.class)
                .build();
        friendAdapater = new FriendAdapater(options);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(friendAdapater);

    }
   */
}
