package com.example.geocachingapp.ui.qrcode.parts;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.print.PrintHelper;

import com.example.geocachingapp.R;
import com.example.geocachingapp.ui.qrcode.QRCodeViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class PrintDialog extends BottomSheetDialogFragment {

    private com.example.geocachingapp.ui.qrcode.QRCodeViewModel QRCodeViewModel;

    private Button print_button;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.bottom_modal_print_sheet,
                container, false);

        print_button = v.findViewById(R.id.print_button);

        QRCodeViewModel =
                new ViewModelProvider(requireActivity()).get(QRCodeViewModel.class);

        print_button.setOnClickListener(v1 -> {
            Bitmap codeBitmap = QRCodeViewModel.getQrCode().getValue();
            if(codeBitmap == null) {
                Toast.makeText(requireActivity(),
                        "Code not generated", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            PrintHelper photoPrinter = new PrintHelper(requireActivity());
            photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
            photoPrinter.printBitmap("New Geocache QR Code", codeBitmap);
            dismiss();
        });

        return v;
    }
}
