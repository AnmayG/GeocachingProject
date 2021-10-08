package com.example.geocachingapp.ui.qrcode;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.geocachingapp.R;
import com.example.geocachingapp.databinding.FragmentQrcodeBinding;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.WriterException;

import java.util.Objects;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRCodeFragment extends Fragment {

    private QRCodeViewModel QRCodeViewModel;
    private FragmentQrcodeBinding binding;

    // variables for imageview, edittext,
    // button, bitmap and qrencoder.
    private ImageView qrCodeIV;
    private TextInputLayout dataEdt;
    private Button generateQrBtn;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        QRCodeViewModel =
                new ViewModelProvider(this).get(QRCodeViewModel.class);

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

        // initializing onclick listener for button.
        generateQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.requireNonNull(dataEdt.getEditText()).getText().toString().equals("")) {

                    // if the edittext inputs are empty then execute
                    // this method showing a toast message.
                    Toast.makeText(requireActivity(),
                            "Enter some text to generate QR Code", Toast.LENGTH_SHORT).show();
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
                    qrgEncoder = new QRGEncoder(Objects.requireNonNull(
                            dataEdt.getEditText()).getText().toString(),
                            null, QRGContents.Type.TEXT, dimen);
                    try {
                        // getting our qrcode in the form of bitmap.
                        bitmap = qrgEncoder.encodeAsBitmap();
                        // the bitmap is set inside our image
                        // view using .setimagebitmap method.
                        qrCodeIV.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        // this method is called for
                        // exception handling.
                        Log.e("qrgencoder", e.toString());
                    }
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}