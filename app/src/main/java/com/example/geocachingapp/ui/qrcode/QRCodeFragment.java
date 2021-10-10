package com.example.geocachingapp.ui.qrcode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.geocachingapp.databinding.FragmentQrcodeBinding;
import com.google.android.gms.common.util.Hex;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class QRCodeFragment extends Fragment {

    private QRCodeViewModel QRCodeViewModel;
    private FragmentQrcodeBinding binding;

    // variables for imageview, edittext,
    // button, bitmap and qrencoder.
    private ImageView qrCodeIV;
    private TextInputLayout dataEdt;
    private Button generateQrBtn;

    private ActivityResultLauncher<Intent> someActivityResultLauncher;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        QRCodeViewModel =
                new ViewModelProvider(requireActivity()).get(QRCodeViewModel.class);

        binding = FragmentQrcodeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        QRCodeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                System.out.println("QR Code " + s);
            }
        });

        // initializing all variables.
        qrCodeIV = binding.idIVQrcode;
        dataEdt =  binding.idEdt;
        generateQrBtn = binding.idBtnGenerateQR;

        // initializing onclick listener for button

        if(dataEdt.getEditText() != null) {
            dataEdt.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    // nothing
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    generateQr();
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    // nothing
                }
            });
        }

        generateQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateQr();
            }
        });

        binding.scanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScanActivityForResult();
            }
        });

        // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            if (data != null) {
                                showQrResult(data);
                            }
                        }
                    }
                });

        return root;
    }

    public void generateQr() {
        String id = Objects.requireNonNull(dataEdt.getEditText()).getText().toString();

        // see next section for ´generateVerificationKey´ method
        Map<String, String> qrCodeDataMap = new HashMap<>();
        qrCodeDataMap.put("Name", id);
        try {
            qrCodeDataMap.put("Key", generateVerificationKey(id));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        String textIn = new JSONObject(qrCodeDataMap).toString();

        if (textIn.equals("")) {

            // if the edittext inputs are empty then execute
            // this method showing a toast message.
            Toast.makeText(requireActivity(),
                    "Please enter a name for your geocache.", Toast.LENGTH_SHORT).show();
        } else {
            // below line is for getting
            // the windowmanager service.
            WindowManager manager =
                    ((WindowManager) requireActivity().getSystemService(Context.WINDOW_SERVICE));

            // initializing a variable for default display.
            Display display = manager.getDefaultDisplay();

            // creating a variable for point which
            // is to be displayed in QR Code.
            Point point = new Point();
            display.getSize(point);

            // getting width and
            // height of a point
            int width = point.x;
            int height = point.y;

            // generating dimension from width and height.
            int dimen = Math.min(width, height);
            dimen = dimen * 3 / 4;

            // setting this dimensions inside our qr code
            // encoder to generate our qr code.
            try {
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.encodeBitmap(textIn, BarcodeFormat.QR_CODE, 400, 400);
                qrCodeIV.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String generateVerificationKey(String str) throws NoSuchAlgorithmException,
                                                             InvalidKeySpecException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        KeySpec spec = new PBEKeySpec(str.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = factory.generateSecret(spec).getEncoded();
        return new String(hash);
    }

    public void openScanActivityForResult() {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
        someActivityResultLauncher.launch(intent);
    }

    public void showQrResult(Intent data) {
        String result = data.getStringExtra("SCAN_RESULT");
        System.out.println(result);
        Toast.makeText(requireContext(), result, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}