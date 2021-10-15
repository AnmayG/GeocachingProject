package com.example.geocachingapp.ui.qrcode.parts;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geocachingapp.AppViewModel;
import com.example.geocachingapp.R;
import com.example.geocachingapp.database.QRCode;
import com.example.geocachingapp.databinding.FragmentQrBuildBinding;
import com.example.geocachingapp.ui.customViews.CircleImageView;
import com.example.geocachingapp.ui.search.SearchViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;
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
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import eu.livotov.labs.android.camview.ScannerLiveView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QrBuildFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QrBuildFragment extends Fragment {

    private static final String TAG = "QrBuildFragment";
    private FragmentQrBuildBinding binding;
    private static final String ARG_SEARCH = "search";
    private boolean mSearch;

    private com.example.geocachingapp.ui.qrcode.QRCodeViewModel QRCodeViewModel;
    private SearchViewModel mSearchViewModel;
    private AppViewModel mAppViewModel;

    private ScannerLiveView camera;

    private TextView notScannedView;
    private ConstraintLayout constraintLayout;
    private CircleImageView profileBackground;
    private ImageView profileImageView;
    private TextView addressView;
    private TextView coordinatesView;
    private TextInputLayout nameInput;
    private TextInputLayout descInput;
    private RecyclerView grid;
    private FloatingActionButton cameraButton;
    private FloatingActionButton saveButton;

    private JSONObject readData;
    private boolean setProfile = true;
    private Bitmap profilePic;
    private final ArrayList<Bitmap> pics = new ArrayList<>();
    private boolean cameFromCamera = false;

    private final ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    cameFromCamera = true;
                    addToGallery();
                }
            });

    public QrBuildFragment() {
        // Required empty public constructor
    }

    public static QrBuildFragment newInstance(boolean search) {
        QrBuildFragment fragment = new QrBuildFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_SEARCH, search);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentQrBuildBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        notScannedView = binding.notScannedView;
        constraintLayout = binding.formConstraintLayout;
        profileBackground = binding.profileBackground;
        profileBackground.setOnClickListener(view -> {
            saveToDatabase();
            takePicture(true);
        });
        profileImageView = binding.profileImageView;
        addressView = binding.addressView;
        coordinatesView = binding.coordinatesView;
        nameInput = binding.nameInput;
        descInput = binding.descInput;

        if (nameInput.getEditText() != null) {
            nameInput.getEditText().setOnKeyListener((v, keyCode, event) -> {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    descInput.requestFocus();
                    return true;
                }
                return false;
            });
        }

        if (descInput.getEditText() != null) {
            descInput.getEditText().setOnKeyListener((v, keyCode, event) -> {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
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
        cameraButton.setOnClickListener(view -> {
            saveToDatabase();
            takePicture(false);
        });
        saveButton = binding.saveButton;

        mAppViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        saveButton.setOnClickListener(view -> {
            saveButton.requestFocus();
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(saveButton.getWindowToken(), 0);
            saveButton.clearFocus();
            saveToDatabase();
        });

        if(mSearch) {
            mSearchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
            mSearchViewModel.getReadData().observe(requireActivity(), this::readQrData);
        } else {
            QRCodeViewModel =
                    new ViewModelProvider(requireActivity()).get(com.example.geocachingapp.ui.qrcode.QRCodeViewModel.class);
            QRCodeViewModel.getReadData().observe(requireActivity(), this::readQrData);
        }

        grid = binding.grid;
        GalleryAdapter myAdapter = new GalleryAdapter(requireContext(), pics);
        grid.setAdapter(myAdapter);
        grid.setLayoutManager(new GridLayoutManager(requireContext(), 3));

        myAdapter.notifyItemChanged(pics.size()-1);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mSearch) {
            readQrData(mSearchViewModel.getReadData().getValue());
        } else {
            readQrData(QRCodeViewModel.getReadData().getValue());
        }
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

        byte[] profileArray = null;
        if(profilePic != null) {
            Bitmap bmp = profilePic.copy(profilePic.getConfig(), true);
            Log.d(TAG, bmp + " " + profilePic);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            profileArray = stream.toByteArray();
            bmp.recycle();
        }

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(requireContext(), Locale.getDefault());
        String addressThing = "";

        try {
            addresses = geocoder.getFromLocation(lastLoc.getLatitude(), lastLoc.getLongitude(), 1);
            addressThing = addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mAppViewModel.insert(new QRCode(id, name, desc, profileArray,
                lastLoc.getLatitude(), lastLoc.getLongitude(), pics, addressThing));
    }

    public void readQrData(String s) {
        Log.d(TAG, s + " readData");
        if (s == null) {
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

                LiveData<QRCode> codeLiveData = mAppViewModel.getCode(id);
                Log.d(TAG, "Start search " + id + " " + codeLiveData);
                if(codeLiveData != null) {
                    codeLiveData.observe(requireActivity(), code -> {
                        if(code != null) {
                            Log.d(TAG, code.toString());
                            if (nameInput.getEditText() != null)
                                nameInput.getEditText().setText(code.getName());
                            if (descInput.getEditText() != null)
                                descInput.getEditText().setText(code.getDescription());
                            if (code.getPicture() != null) {
                                profilePic = BitmapFactory.decodeByteArray(code.getPicture(), 0, code.getPicture().length);
                                profileBackground.setImageBitmap(profilePic);
                                profileImageView.setVisibility(View.GONE);
                            }
                            if (code.getPictureStorage() != null && !cameFromCamera) {
                                pics.clear();
                                pics.addAll(code.getPictureStorage());
                                Log.d(TAG, pics.toString());
                            }
                        } else {
                            mAppViewModel.insert(new QRCode(id, name, "", null, 0, 0, null, ""));
                            if (nameInput.getEditText() != null) nameInput.getEditText().setText(name);
                        }
                    });
                } else {
                    mAppViewModel.insert(new QRCode(id, name, "", null, 0, 0, null, ""));
                    if (nameInput.getEditText() != null) nameInput.getEditText().setText(name);
                }

                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(requireContext(), Locale.getDefault());
                String addressThing = "";
                if(lastLoc == null) {
                    lastLoc = new Location("null");
                    lastLoc.setLatitude(0);
                    lastLoc.setLongitude(0);
                } else {
                    try {
                        addresses = geocoder.getFromLocation(lastLoc.getLatitude(), lastLoc.getLongitude(), 1);
                        addressThing = addresses.get(0).getAddressLine(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                coordinatesView.setText(String.format("Coordinates: %s, %s",
                        lastLoc.getLatitude(), lastLoc.getLongitude()));
                addressView.setText(String.format("Address: %s", !addressThing.equals("") ? addressThing : "None"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void takePicture(boolean setProfile) {
        if (!requireActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
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
            Bitmap b = decodeLastImageFile(profileBackground);
            profileBackground.setImageBitmap(b);
            profilePic = b;
            profileImageView.setVisibility(View.GONE);
        } else {
            decodeLastImageFile(null);
        }
        Objects.requireNonNull(grid.getAdapter()).notifyItemInserted(pics.size() - 1);
        Log.d(TAG, pics.toString());
    }

    public Bitmap decodeLastImageFile(ImageView imageView) {
        // Get the dimensions of the View
        int targetW = imageView != null ? imageView.getWidth() : 100;
        int targetH = imageView != null ? imageView.getHeight() : 100;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW / targetW, photoH / targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        pics.add(bitmap);
        return bitmap;
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

    // Begin location code

    private FusedLocationProviderClient fusedLocationClient;
    private Location lastLoc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            mSearch = getArguments().getBoolean(ARG_SEARCH);
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        lastLoc = location;
                    }
                });

        Log.d(TAG, "getCurrentLocation started");

        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
            @Override
            public boolean isCancellationRequested() {
                return false;
            }

            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                return this;
            }
        }).addOnSuccessListener(location -> {
            Log.d(TAG, "location created");
            if (location != null) {
                lastLoc = location;
            }
        });
    }

}