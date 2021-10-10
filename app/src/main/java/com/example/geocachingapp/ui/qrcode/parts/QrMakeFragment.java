package com.example.geocachingapp.ui.qrcode.parts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.geocachingapp.databinding.FragmentQrMakeBinding;
import com.example.geocachingapp.ui.qrcode.QRCodeViewModel;
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

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class QrMakeFragment extends Fragment {

    private com.example.geocachingapp.ui.qrcode.QRCodeViewModel QRCodeViewModel;
    private FragmentQrMakeBinding binding;

    // variables for imageview, edittext,
    // button, bitmap and qrencoder.
    private ImageView qrCodeIV;
    private TextInputLayout dataEdt;
    private Button generateQrBtn;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        QRCodeViewModel =
                new ViewModelProvider(requireActivity()).get(QRCodeViewModel.class);

        binding = FragmentQrMakeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        QRCodeViewModel.getText().observe(getViewLifecycleOwner(), s -> System.out.println("QR Code " + s));

        // initializing all variables.
        qrCodeIV = binding.idIVQrcode;
        dataEdt =  binding.idEdt;
        generateQrBtn = binding.idBtnGenerateQR;

        // initializing onclick listener for button
        generateQrBtn.setOnClickListener(v -> generateQr());

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
            // below line is for getting the windowmanager service.
            WindowManager manager =
                    ((WindowManager) requireActivity().getSystemService(Context.WINDOW_SERVICE));

            // initializing a variable for default display.
            Display display = manager.getDefaultDisplay();

            // creating a variable for point which is to be displayed in QR Code.
            Point point = new Point();
            display.getSize(point);

            // setting this dimensions inside our qr code encoder to generate our qr code.
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}