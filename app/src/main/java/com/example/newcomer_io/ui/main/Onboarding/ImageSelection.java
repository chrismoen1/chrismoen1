package com.example.newcomer_io.ui.main.Onboarding;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newcomer_io.R;
import com.example.newcomer_io.ui.main.GroupTiming.TimePickerFragment;

import static androidx.core.content.PermissionChecker.checkSelfPermission;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImageSelection#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageSelection extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final int PICK_IMAGE = 2 ;
    private static final int RESULT_OK = -1;
    private static final int CAMERA_REQUEST = 3;
    private OnImageEdit onImageEdit;
    private static final int PERMISSION_REQUEST_CODE = 200;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImageSelection.
     */
    // TODO: Rename and change types and number of parameters

    @Override
    public void onAttach(@NonNull Context context) {
        onImageEdit = (OnImageEdit) context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_image_selection, container, false);
        //infalte
        LinearLayout gallery = inflate.findViewById(R.id.gallery);
        LinearLayout photo = inflate.findViewById(R.id.photo);

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        return inflate;
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }

    private void openCamera(){
        int permissionCheckStorage = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)
        {
            requestPermission();
            //getDialog().dismiss();
            //getOnImageEdit().requestPermissions();
        }
        else if (permissionCheckStorage == PackageManager.PERMISSION_GRANTED)
        {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
            else
        {
            Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
        }
    }
    private void requestPermission() {

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        OnImageEdit onImageEdit = getOnImageEdit();
        Uri imageUri= data.getData();
        if (resultCode == RESULT_OK && PICK_IMAGE == requestCode){
            onImageEdit.sendImageFileUri(imageUri);
            getDialog().dismiss();
        }
        else if (resultCode == RESULT_OK && CAMERA_REQUEST == requestCode){
            //Then we send back that photo
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            onImageEdit.sendCameraImage(photo);
            getDialog().dismiss();
        }
    }

    public OnImageEdit getOnImageEdit() {
        return onImageEdit;
    }

    public void setOnImageEdit(OnImageEdit onImageEdit) {
        this.onImageEdit = onImageEdit;
    }

    public interface OnImageEdit{
            void sendImageFileUri(Uri imageUri);
            void sendCameraImage(Bitmap photo);
    }
}
