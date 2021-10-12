package com.example.geocachingapp.ui.qrcode.parts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.geocachingapp.AppViewModel;
import com.example.geocachingapp.R;
import com.example.geocachingapp.database.QRCode;
import com.example.geocachingapp.databinding.FragmentQrBuildBinding;
import com.example.geocachingapp.ui.customViews.CircleImageView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QrBuildFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QrBuildFragment extends Fragment {

    private FragmentQrBuildBinding binding;
    private com.example.geocachingapp.ui.qrcode.QRCodeViewModel QRCodeViewModel;
    private AppViewModel appViewModel;
    private static String TAG = "QrBuildFragment";

    private TextView notScannedView;

    private ConstraintLayout constraintLayout;
    private CircleImageView profileBackground;
    private ImageView profileImageView;
    private TextInputLayout nameInput;
    private TextInputLayout descInput;
    private FloatingActionButton cameraButton;
    private FloatingActionButton saveButton;

    private JSONObject readData;
    private boolean setProfile = true;
    private Bitmap profilePic;
    private final ArrayList<Bitmap> pics = new ArrayList<>();

    private final ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    addToGallery();
                }
            });

    public QrBuildFragment() {
        // Required empty public constructor
    }

    public static QrBuildFragment newInstance() {
        return new QrBuildFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentQrBuildBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        notScannedView = binding.notScannedView;
        constraintLayout = binding.formConstraintLayout;
        profileBackground = binding.profileBackground;
        profileBackground.setOnClickListener(view -> takePicture(true));
        profileImageView = binding.profileImageView;
        nameInput = binding.nameInput;
        descInput = binding.descInput;

        if(nameInput.getEditText() != null) {
            nameInput.getEditText().setOnKeyListener((v, keyCode, event) -> {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    descInput.requestFocus();
                    return true;
                }
                return false;
            });
        }

        if(descInput.getEditText() != null) {
            descInput.getEditText().setOnKeyListener((v, keyCode, event) -> {
                if((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    descInput.requestFocus();
                    InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(descInput.getWindowToken(), 0);
                    descInput.clearFocus();
                    return true;
                }
                return false;
            });
        }
        cameraButton = binding.cameraButton;
        cameraButton.setOnClickListener(view -> takePicture(false));
        saveButton = binding.saveButton;

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        saveButton.setOnClickListener(view -> saveToDatabase());

        QRCodeViewModel =
                new ViewModelProvider(requireActivity()).get(com.example.geocachingapp.ui.qrcode.QRCodeViewModel.class);

        QRCodeViewModel.getReadData().observe(requireActivity(), this::readQrData);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        readQrData(QRCodeViewModel.getReadData().getValue());
    }

    public void saveToDatabase() {
        String id = "";
        try {
            id = readData.getString("Key");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String name = Objects.requireNonNull(nameInput.getEditText()).getText().toString();
        String desc = Objects.requireNonNull(descInput.getEditText()).getText().toString();

        Bitmap bmp = profilePic;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] profileArray = stream.toByteArray();
        bmp.recycle();

        // TODO: Get location
        appViewModel.insert(new QRCode(id, name, desc, profileArray, 0, 0, pics));
    }

    public void readQrData(String s) {
        Log.d(TAG, s + "");
        if(s == null) {
            constraintLayout.setVisibility(View.GONE);
            notScannedView.setVisibility(View.VISIBLE);
            notScannedView.setText(R.string.not_scanned_text);
        } else {
            try {
                constraintLayout.setVisibility(View.VISIBLE);
                notScannedView.setVisibility(View.GONE);
                readData = new JSONObject(s);
                String id = readData.getString("Key");
                notScannedView.setText(id);
                String name = readData.getString("Name");
                appViewModel.insert(new QRCode(id, name, "", null, 0, 0, null));
                if(nameInput.getEditText() != null) nameInput.getEditText().setText(name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void takePicture(boolean setProfile) {
        if(!requireActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            Toast.makeText(requireContext(), "Camera not available", Toast.LENGTH_SHORT).show();
            return;
        }
        this.setProfile = setProfile;
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(requireContext(), "Problem creating file, please try again", Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(requireContext(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                cameraActivityResultLauncher.launch(takePictureIntent);
            } else {
                Toast.makeText(requireContext(), "Problem creating file, please try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addToGallery() {
        if (setProfile) {
            showInView(profileBackground);
            profileImageView.setVisibility(View.GONE);
        } else {
            // add to recycler
        }
    }

    public void showInView(ImageView imageView) {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        if(profilePic == null) profilePic = bitmap;
        pics.add(bitmap);

        imageView.setImageBitmap(bitmap);
    }

    private String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}