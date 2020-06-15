package com.example.newcomer_io.ui.main.Onboarding;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.newcomer_io.R;
import com.theartofdev.edmodo.cropper.CropImageView;

public class CropImage extends AppCompatActivity {

    public static final int CROP_IMAGE_ACTIVITY_REQUEST_CODE = 3 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);

        Intent intent = getIntent();
        Bitmap crop_image = (Bitmap) intent.getParcelableExtra("BitmapImage");

        CropImageView cropImageView = findViewById(R.id.cropImageView);
        cropImageView.setImageBitmap(crop_image);
        cropImageView.setOnCropImageCompleteListener(new CropImageView.OnCropImageCompleteListener() {
            @Override
            public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
                //Then we get the activity results and send back
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",view.getCroppedImage());
                setResult(Activity.RESULT_OK,returnIntent);
                finish();

            }
        });
    }


}
