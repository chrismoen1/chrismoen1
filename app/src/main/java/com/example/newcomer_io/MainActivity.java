package com.example.newcomer_io;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import com.example.newcomer_io.ui.main.FindFriends.FindFriends;
import com.example.newcomer_io.ui.main.GroupTiming.CreateStudyGroup;
import com.example.newcomer_io.ui.main.JoinGroup.JoinGroup;
import com.example.newcomer_io.ui.main.MainFragment;
import com.example.newcomer_io.ui.main.Onboarding.ImageSelection;
import com.example.newcomer_io.ui.main.SignIn.SignIn;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.newcomer_io.ui.main.Onboarding.ProfileInformation.RotateBitmap;
import static com.example.newcomer_io.ui.main.UserDetails.UserData.toMap;

public class MainActivity extends AppCompatActivity implements ImageSelection.OnImageEdit{
    private BottomNavigationView mBottomNavView;
    private static final int CAMERA_REQUEST = 16;
    private static final int PICK_IMAGE = 2 ;

    //Lets set somme
    private TextView schoolName;
    private TextView displayName;
    private TextView findFriends;

    private ImageView profilePhoto;
    private File outputFile;

    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    private ImageView editIcon;
    private String Uuid;

    private Uri outputUri;
    private Toolbar toolbar;
    private ImageView profileImage;
    private String image_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        profilePhoto = findViewById(R.id.profilePhoto);
        displayName = findViewById(R.id.userName);
        schoolName = findViewById(R.id.schoolName);
        findFriends = findViewById(R.id.findFriends);
        editIcon = findViewById(R.id.editProfileImage);

        Intent intent = getIntent();

        //First check to see if the intent has data that has been passed from especially for the profile sign up
        if (intent.getStringExtra("ACTIVITY_NAME").contains("ProfileInformation")) {

            //Then we want to gather all of the data that pertains to the uiser
            //Get all details from the prior intent
            //byte[] profile_image = intent.getByteArrayExtra("Profile Image");
            String profile_image = intent.getStringExtra("ProfileImage");
            setImageName(intent.getStringExtra("ImageName"));
            Uri profile_image_uri = Uri.parse(profile_image);
            //Bitmap bmp = BitmapFactory.decodeByteArray(profile_image, 0, profile_image.length);

            String first_name = intent.getStringExtra("FirstName");
            String last_name = intent.getStringExtra("LastName");
            String school_name = intent.getStringExtra("SchoolName");
            setUuid(intent.getStringExtra("Uuid"));
            //String image_name = intent.getStringExtra("Image Name");

            displayName.setText(first_name + " " + last_name);
            profilePhoto.setImageURI(profile_image_uri);
            schoolName.setText(school_name);

            //downloadImagePhoto(Uuid,image_Name);

        }
        else if (intent.getStringExtra("ACTIVITY_NAME").contains("SignIn"))
        {
         //Then we want to download the user profile based on their UUID
            setUuid(intent.getStringExtra("Uuid"));
            String name = intent.getStringExtra("Name");
            String first_name = intent.getStringExtra("FirstName");
            String last_name = intent.getStringExtra("LastName");
            String school_name = intent.getStringExtra("SchoolName");
            setImageName(intent.getStringExtra("ImageName"));

            downloadImagePhoto(Uuid,getImageName());

        }
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }

        mBottomNavView = findViewById(R.id.bottomNavigationView);
        toolbar = findViewById(R.id.toolbar1);
        toolbar.setTitle("Profile View");

        mBottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.group_add:
                        //userData.updateUserData();
                        Intent intent = new Intent(MainActivity.this, CreateStudyGroup.class);
                        startActivity(intent);
                        return true;
                    case R.id.merge:
                        Intent intent1 = new Intent(MainActivity.this, JoinGroup.class);
                        startActivity(intent1);
                        return true;
                }
                return false;
            }
        });

        LinearLayout signOut = findViewById(R.id.signOutContainer);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Then we sign out and go back to the initial page
                FirebaseAuth.getInstance().signOut();
                //Then we return to the main page
                Intent intent = new Intent(MainActivity.this, SignIn.class);
                startActivity(intent);
            }
        });
        editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Then we set the icon
                //Then we set the onlick listener i
                //openGallery();
                DialogFragment dialogFragment = new ImageSelection();
                dialogFragment.show(getSupportFragmentManager(),"Image Options");
            }
        });

        findFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Then we navigate to the next intent where we use find friends
                Intent intent = new Intent(MainActivity.this, FindFriends.class);
                intent.putExtra("Uuid",getUuid());
                startActivity(intent);
            }
        });

    }

    private void downloadImagePhoto(String uuid,String imageName) {

        StorageReference reference = getFirebaseStorage().getReference("User Images").child(uuid).child(imageName);
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(getProfilePhoto());
            }
        });
    }


    //Function to map
    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }
    public void setProfilePhoto(ImageView profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public ImageView getEditIcon() {
        return editIcon;
    }

    public void setEditIcon(ImageView editIcon) {
        this.editIcon = editIcon;
    }
    @Override
    public void sendImageFileUri(Uri imageUri) {
        profilePhoto.setImageURI(imageUri);
    }


    @Override
    public void sendCameraImage(Bitmap photo) {

    }

    @Override
    public void sendCameraIntent() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        String format = simpleDateFormat.format(new Date());

        Date date = new Date();
        outputFile = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "profile"+ format +".jpg");

        String applicationId = BuildConfig.APPLICATION_ID;
        outputUri = FileProvider.getUriForFile(getApplicationContext(),applicationId + ".provider", outputFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> resInfoList = this.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            this.grantUriPermission(packageName, outputUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        startActivityForResult(intent,CAMERA_REQUEST);
    }
    public ImageView getProfilePhoto() {
        return profilePhoto;
    }

    public Bitmap getBitmapPhoto(){

        //int width = cropImageView.getWidth();
        //int height = cropImageView.getHeight();

        BitmapFactory.Options factoryOptions = new BitmapFactory.Options();
        factoryOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(getOutputFile().getPath(),factoryOptions);
        int imageWidth = factoryOptions.outWidth;
        int imageHeight = factoryOptions.outHeight;

        //int scaleFactor = Math.min(imageWidth/width,imageHeight/height);

        factoryOptions.inJustDecodeBounds = false;
        //factoryOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeFile(getOutputFile().getPath(), factoryOptions);
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Profile Image", null);
        return Uri.parse(path);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView profileImage = getProfilePhoto();
        //StorageReference user_images = getFirebaseStorage().getReference("User Images").child(uuid);

        Uri resultUri;
        if (resultCode == -1 && requestCode == CAMERA_REQUEST){
            if (data != null)
            {
                if (data.hasExtra("data")){
                    Bitmap thumbnail = data.getParcelableExtra("data");
                    //reUpload(getUuid(),thumbnail);
                    resultUri = getImageUri(this,thumbnail);
                    startCropActivity(resultUri);

                    profileImage.setImageBitmap(thumbnail);
                }
            }else{

                Bitmap bitmapPhoto = getBitmapPhoto();
                Bitmap bitmap = RotateBitmap( bitmapPhoto,90);
                resultUri = getImageUri(this,bitmap);
                startCropActivity(resultUri);

                //reUpload(getUuid(),bitmap);
                //(getImageUri(this,bitmap));

            }

            //Then we can f

        } /*else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Bitmap bitmapResult = convert_bitmap(resultUri);
                reUpload(getUuid(),bitmapResult);
                Bitmap bitmap = scale_ImagePhoto(bitmapResult, getProfilePhoto());
                profileImage.setImageBitmap(bitmap);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }else if (requestCode == PICK_IMAGE && data != null){
            Uri bitmapResult = data.getData();
            reUpload(getUuid(),convert_bitmap(bitmapResult));

            //Bitmap bbb = convert_bitmap(bitmapResult);
            startCropActivity(bitmapResult);
        }*/
        else if (requestCode == UCrop.REQUEST_CROP && data != null) {
            //CropImage.ActivityResult result = CropImage.getActivityResult(data);
            resultUri = UCrop.getOutput(data);
            if (resultCode == RESULT_OK) {
                ///Bitmap bitmapResult = convert_bitmap(resultUri);
                //Bitmap bitmap = scale_ImagePhoto(bitmapResult, getProfileImage());
                reUpload(getUuid(),convert_bitmap(resultUri));
                profileImage.setImageURI(resultUri);
            } else if (resultCode == UCrop.RESULT_ERROR) {
                //Exception error = result.getError();
            }
        }else if (requestCode == PICK_IMAGE  && data != null){
            resultUri = data.getData();
            //reUpload(getUuid(),convert_bitmap(resultUri));
            //Bitmap bbb = convert_bitmap(bitmapResult);
            startCropActivity(resultUri);
        }
    }
    private void startCropActivity(Uri imageUri) {
        String destinationFileName = UUID.randomUUID().toString() + ".jpg";

        UCrop.of(imageUri, Uri.fromFile(new File(getCacheDir(),destinationFileName)))
                .withAspectRatio(4, 3)
                .withMaxResultSize(600, 600)
                .start(this);
    }

    private void reUpload(String uuid, final Bitmap image) {
        // Get the data from an ImageView as bytes

        StorageReference user_images = getFirebaseStorage().getReference().child("User Images").child(uuid).child(getImageName());

        user_images.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Date date = new Date();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd-hh-mm");
                String format = simpleDateFormat.format(date);

                setImageName(format);

                firebaseDatabase.getReference().child("UserData").child(getUuid()).child("ImageName").setValue(format);
                StorageReference user_images = getFirebaseStorage().getReference().child("User Images").child(getUuid()).child(format);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                //Then we re=upload the user's image
                UploadTask uploadTask = user_images.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                    }
                });
            }
        });
    }

/*    private void startCropActivity(Uri imageUri) {
        Intent intent = CropImage.activity(imageUri)
                .getIntent(getApplicationContext());
        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }*/

    private Bitmap scale_ImagePhoto(Bitmap bitmapResult, ImageView profileImage) {

        int profileHeight = profileImage.getHeight();
        int width = profileImage.getWidth();

        int bitmapHeight = bitmapResult.getHeight();
        int bitmapWidth = bitmapResult.getWidth();

        if (bitmapHeight >= profileHeight || bitmapWidth >= width){

            //Then we scale down
            float scaledFactor = Math.min((float)profileHeight / bitmapHeight, (float) width / bitmapWidth);
            int new_height = (int) (scaledFactor * bitmapHeight);
            int new_width = (int) (scaledFactor * bitmapWidth);
            return Bitmap.createScaledBitmap(bitmapResult, new_width, new_height, false);

        }else{

            float scaledFactor = Math.min((float)profileHeight / bitmapHeight, (float) width / bitmapWidth);
            int new_height = (int) (scaledFactor * bitmapHeight);
            int new_width = (int) (scaledFactor * bitmapWidth);
            return Bitmap.createScaledBitmap(bitmapResult, new_width, new_height, false);

        }
    }

    private Bitmap convert_bitmap(Uri resultUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),resultUri);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void sendGalleryIntent() {
        openGallery();
    }
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }

    public void setProfileImage(ImageView profileImage) {
        this.profileImage = profileImage;
    }

    public File getOutputFile(){return this.outputFile; }
    public void setOutputFile(File file){this.outputFile = file; }

    public FirebaseStorage getFirebaseStorage() {
        return firebaseStorage;
    }

    public void setFirebaseStorage(FirebaseStorage firebaseStorage) {
        this.firebaseStorage = firebaseStorage;
    }

    public ImageView getProfileImage() {
        return profileImage;
    }

    public Uri getOutputUri() {
        return outputUri;
    }

    public void setOutputUri(Uri outputUri) {
        this.outputUri = outputUri;
    }

    public String getUuid() {
        return Uuid;
    }

    public void setUuid(String uuid) {
        Uuid = uuid;
    }
    public String getImageName(){return this.image_name; }
    public void setImageName(String imageName){this.image_name = imageName; }

    public TextView getFindFriends() {
        return findFriends;
    }

    public void setFindFriends(TextView findFriends) {
        this.findFriends = findFriends;
    }
}
