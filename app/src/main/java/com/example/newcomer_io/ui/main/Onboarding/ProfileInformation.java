package com.example.newcomer_io.ui.main.Onboarding;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import com.example.newcomer_io.BuildConfig;
import com.example.newcomer_io.R;
import com.example.newcomer_io.ui.main.UserDetails.UserData;
import com.firebase.ui.auth.data.model.User;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;
import de.hdodenhof.circleimageview.CircleImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

///            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
public class ProfileInformation extends AppCompatActivity implements ImageSelection.OnImageEdit {

    //Specifying the gallery photo picking
    private static final int PICK_IMAGE = 2 ;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int CROP_COMPLETED = 4;
    private static final int CAMERA_REQUEST = 16;

    private File outputFile;

    private EditText firstName;
    private EditText lastName;
    private EditText schoolName;
    private ArrayList<CheckBox> checkBoxArrayList;
    private LinearLayout subjectName;
    private ImageButton imageOptions;
    private Button saveChanges;
    private CircleImageView profileImage;
    private LinearLayout faculty_Container;
    private LinearLayout year_Container;

    private Spinner faculty;
    private Spinner studyYear;
    private UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_information);


        userData = (UserData) getApplicationContext();
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.editText);
        schoolName = findViewById(R.id.schoolName);
        subjectName = findViewById(R.id.subjectName);

        faculty_Container = findViewById(R.id.facultyContainer);
        year_Container = findViewById(R.id.yearContainer);
        saveChanges = findViewById(R.id.saveChanges);

        faculty = findViewById(R.id.faculty);
        studyYear = findViewById(R.id.year_Spinner);

        imageOptions = findViewById(R.id.imageButton2);

        checkBoxArrayList = new ArrayList<CheckBox>();
        checkBoxArrayList.add((CheckBox) findViewById(R.id.psychology));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.accounting));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.mathematics));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.chemistry));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.computerScience));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.engineering));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.statistics));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.biology));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.physics));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.physiology2));
        checkBoxArrayList.add((CheckBox) findViewById(R.id.economics));

        for (int i =0; i < checkBoxArrayList.size();i++){
            //Then we get the current checkbox and set hte checkbox listener
            setCheckBoxListener(checkBoxArrayList.get(i));
        }

        setEditListener(firstName);
        setEditListener(lastName);
        setEditListener(schoolName);

        setImageOptionsListener(imageOptions);
        setSaveButtonListener(saveChanges);

        //Set the adapters for the spinner drop downs that we will display on the profile information page
        String[] faculty_Strs = {"Arts", "Business","Engineering", "Science","Other"};
        String[] year_Strs = {"1st Year","2nd Year", "3rd Year", "4th Year", "5th Year", "6th Year"};

        ArrayAdapter<String> faculty_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,faculty_Strs);
        ArrayAdapter<String> years_Adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,year_Strs);

        faculty.setAdapter(faculty_Adapter);
        studyYear.setAdapter(years_Adapter);

        setSpinnerListener(faculty, faculty_Container);
        setSpinnerListener(studyYear, year_Container);

        faculty_Container.setBackgroundResource(R.drawable.rounded_border_deselected);
        year_Container.setBackgroundResource(R.drawable.rounded_border_deselected);
        profileImage = findViewById(R.id.userProfileImage);

        fillUserData();

    }

    private void setImageOptionsListener(ImageButton imageOptions) {
        imageOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Then we set the onlick listener i
                //openGallery();
                DialogFragment dialogFragment = new ImageSelection();
                dialogFragment.show(getSupportFragmentManager(),"Image Options");
            }
        });
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }

    private void fillUserData() {
        String name = userData.getDisplayName();
        //String photoUrl = userData.getPhotoUrl();

        //Picasso.get().load(photoUrl).into(profileImage);
        //Bitmap bitmapFromURL = getBitmapFromURL(photoUrl);

    }
    public Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setSaveButtonListener(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText firstName = getFirstName();
                EditText lastName = getLastName();
                EditText schoolName = getSchoolName();
                boolean flag = false;

                if (firstName.getText().toString().isEmpty()){
                    //Then we do not go to the next page and instead give a request focus and error on those names stating that they need to have
                    //values inputted into it
                    firstName.requestFocus();
                    firstName.setError("Please enter your first name");
                    flag = true;
                }
                if (lastName.getText().toString().isEmpty()){
                    lastName.requestFocus();
                    lastName.setError("Please enter your last name");
                    flag = true;
                }
                if (schoolName.getText().toString().isEmpty()){
                   schoolName.requestFocus();
                   schoolName.setError("Please enter a school name");
                   flag = true;
                }
                if (!flag){
                    //Then we go to the next screen
                }
            }
        });
    }

    private void setSpinnerListener(final Spinner faculty, final LinearLayout container) {
        faculty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                container.setBackgroundResource(R.drawable.rounded_border);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                container.setBackgroundResource(R.drawable.rounded_border_deselected);
            }
        });
    }

    private void setEditListener(final EditText text) {

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Then we update the colour of the view paramaters by changing the colour of the border
                text.setBackgroundResource(R.drawable.text_backdrop_selected);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0){
                    text.setBackgroundResource(R.drawable.text_backdrop);

                }

            }
        });
    }


    public void setCheckBoxListener(CheckBox currCheckBox){
        currCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               checkArrayBox_Colour(getCheckBoxArrayList());
            }
        });
    }

    private void checkArrayBox_Colour(ArrayList<CheckBox> checkBoxArrayList) {
        boolean flag = false;
        for (int i =0; i < checkBoxArrayList.size();i++){
            CheckBox checkBox = checkBoxArrayList.get(i);
            if (checkBox.isChecked()){
                getSubjectName().setBackgroundResource(R.drawable.rounded_border);
                flag = true;
            }
        }
        if (flag == false) {
            //then we set it back to colour
            getSubjectName().setBackgroundResource(R.drawable.rounded_border_deselected);
        }
    }

    private void updateLayoutColor(LinearLayout linearLayout) {
        linearLayout.setBackgroundResource(R.drawable.rounded_border);
    }

    public EditText getLastName() {
        return lastName;
    }

    public void setLastName(EditText lastName) {
        this.lastName = lastName;
    }

    public EditText getFirstName() {
        return firstName;
    }

    public void setFirstName(EditText firstName) {
        this.firstName = firstName;
    }

    public EditText getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(EditText schoolName) {
        this.schoolName = schoolName;
    }

    public Spinner getFaculty() {
        return faculty;
    }

    public void setFaculty(Spinner faculty) {
        this.faculty = faculty;
    }

    public Spinner getStudyYear() {
        return studyYear;
    }

    public void setStudyYear(Spinner studyYear) {
        this.studyYear = studyYear;
    }
    public ArrayList<CheckBox> getCheckBoxArrayList(){
        return this.checkBoxArrayList;
    }

    public LinearLayout getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(LinearLayout subjectName) {
        this.subjectName = subjectName;
    }

    public LinearLayout getFaculty_Container() {
        return faculty_Container;
    }

    public void setFaculty_Container(LinearLayout faculty_Container) {
        this.faculty_Container = faculty_Container;
    }

    public LinearLayout getYear_Container() {
        return year_Container;
    }

    public void setYear_Container(LinearLayout year_Container) {
        this.year_Container = year_Container;
    }

    public Button getSaveChanges() {
        return saveChanges;
    }

    public void setSaveChanges(Button saveChanges) {
        this.saveChanges = saveChanges;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public CircleImageView getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(CircleImageView profileImage) {
        this.profileImage = profileImage;
    }

    public ImageButton getImageOptions() {
        return imageOptions;
    }

    public void setImageOptions(ImageButton imageOptions) {
        this.imageOptions = imageOptions;
    }

    @Override
    public void sendImageFileUri(Uri imageUri) {
        profileImage.setImageURI(imageUri);
    }

    @Override
    public void sendCameraImage(Bitmap photo) {

    }

    @Override
    public void sendCameraIntent() {
        outputFile = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "profile.jpg");
        String applicationId = BuildConfig.APPLICATION_ID;
        Uri outputUri = FileProvider.getUriForFile(getApplicationContext(),applicationId + ".provider", outputFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> resInfoList = this.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            this.grantUriPermission(packageName, outputUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            startActivityForResult(intent,CAMERA_REQUEST);

        }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        CircleImageView profileImage = getProfileImage();
        if (resultCode == -1 && requestCode == CAMERA_REQUEST){
            if (data != null)
            {
                if (data.hasExtra("data")){
                    Bitmap thumbnail = data.getParcelableExtra("data");
                    profileImage.setImageBitmap(thumbnail);
              /*      Intent intent = new Intent(ProfileInformation.this, CropImage.class);
                    intent.putExtra("BitmapImage",thumbnail);
                    startActivityForResult(intent, CROP_COMPLETED);*/
                }
            }else{

                //profileImage.setImageBitmap(bitmap);
                //Intent intent = new Intent(ProfileInformation.this, CropImage.class);
                Bitmap bitmapPhoto = getBitmapPhoto();
                Bitmap bitmap = RotateBitmap( bitmapPhoto,90);

                Intent intent = CropImage.activity(getImageUri(this,bitmap))
                        .getIntent(getApplicationContext());
                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
                //intent.putExtra("Output File Path",getOutputFile().getPath());
                //startActivityForResult(intent, CROP_COMPLETED);


            }

            //Then we can f

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Bitmap bitmapResult = convert_bitmap(resultUri);
                Bitmap bitmap = scale_ImagePhoto(bitmapResult, getProfileImage());
                profileImage.setImageBitmap(bitmap);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private Bitmap scale_ImagePhoto(Bitmap bitmapResult, CircleImageView profileImage) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int profileHeight = profileImage.getHeight();
        int width = size.x;

        int bitmapHeight = bitmapResult.getHeight();
        int bitmapWidth = bitmapResult.getWidth();

        if (bitmapHeight >= profileHeight || bitmapWidth >= width){
            //Then we scale down
            int scaledFactor = Math.min(profileHeight / bitmapHeight, width / bitmapWidth);
            return Bitmap.createScaledBitmap(bitmapResult, scaledFactor * bitmapWidth, scaledFactor * bitmapHeight, false);
        }else{
            int scaledFactor = (1 / Math.min(profileHeight / bitmapHeight, width / bitmapWidth));
            return Bitmap.createScaledBitmap(bitmapResult, scaledFactor * bitmapWidth, scaledFactor * bitmapHeight, false);
        }
    }

    private Bitmap convert_bitmap(Uri resultUri) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        return BitmapFactory.decodeFile(new File(resultUri.getPath()).getAbsolutePath(), options);
}

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Profile Image", null);
        return Uri.parse(path);
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
    public void setOutputFile(File file){
        this.outputFile = file;
    }
    public File getOutputFile(){return this.outputFile; }
}
