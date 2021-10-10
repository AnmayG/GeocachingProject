package com.example.geocachingapp.ui.qrcode.parts;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.geocachingapp.databinding.FragmentQrMakeBinding;
import com.example.geocachingapp.ui.qrcode.QRCodeViewModel;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.apache.commons.codec.binary.Hex;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class QrMakeFragment extends Fragment {

    private com.example.geocachingapp.ui.qrcode.QRCodeViewModel QRCodeViewModel;
    private FragmentQrMakeBinding binding;

    // variables for imageview, edittext,
    // button, bitmap and qrencoder.
    private ImageView qrCodeImageView;
    private TextInputLayout nameInputLayout;
    private Button createQrButton;
    private Button printButton;

    private Bitmap codeBitmap;

    public static QrMakeFragment newInstance() {
        return new QrMakeFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        QRCodeViewModel =
                new ViewModelProvider(requireActivity()).get(QRCodeViewModel.class);

        QRCodeViewModel.getText().observe(requireActivity(), s -> System.out.println("QR Code " + s));

        binding = FragmentQrMakeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // initializing all variables.
        qrCodeImageView = binding.idIVQrcode;
        nameInputLayout =  binding.idEdt;
        nameInputLayout.setFocusableInTouchMode(true);
        createQrButton = binding.idBtnGenerateQR;

        // initializing onclick listener for button
        createQrButton.setOnClickListener(v -> generateQr());

        QRCodeViewModel.setQrCode(null);
        qrCodeImageView.setImageBitmap(null);

        printButton = binding.printButton;

        if(nameInputLayout.getEditText() != null) {
            nameInputLayout.getEditText().setOnKeyListener((v, keyCode, event) -> {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    generateQr();
                    return true;
                }
                return false;
            });
        }

        printButton.setOnClickListener(view -> {
            PrintDialog bottomSheet = new PrintDialog();
            bottomSheet.show(requireActivity().getSupportFragmentManager(), "ModalBottomSheet");
        });

        return root;
    }

    public void hideKeyboard() {
        nameInputLayout.requestFocus();

        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(nameInputLayout.getWindowToken(), 0);
    }

    public void generateQr() {
        hideKeyboard();

        String id = Objects.requireNonNull(nameInputLayout.getEditText()).getText().toString();

        // see next section for ´generateVerificationKey´ method
        Map<String, String> qrCodeDataMap = new HashMap<>();
        qrCodeDataMap.put("Name", id.isEmpty() ? "No name provided" : id);
        try {
            qrCodeDataMap.put("Key", generateVerificationKey(id + getSaltString()));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        String textIn = new JSONObject(qrCodeDataMap).toString();
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            codeBitmap = barcodeEncoder.encodeBitmap(textIn, BarcodeFormat.QR_CODE, 400, 400);
            qrCodeImageView.setImageBitmap(codeBitmap);
            QRCodeViewModel.setQrCode(codeBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // https://stackoverflow.com/questions/20536566/creating-a-random-string-with-a-z-and-0-9-in-java/20536597
    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();

    }

    public String generateVerificationKey(String str) throws NoSuchAlgorithmException,
                                                             InvalidKeySpecException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        KeySpec spec = new PBEKeySpec(str.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = factory.generateSecret(spec).getEncoded();
        return new String(Hex.encodeHex(hash));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}