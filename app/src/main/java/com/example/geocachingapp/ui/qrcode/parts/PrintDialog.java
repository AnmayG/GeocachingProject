package com.example.geocachingapp.ui.qrcode.parts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.print.PrintHelper;

import com.example.geocachingapp.R;
import com.example.geocachingapp.ui.qrcode.QRCodeViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class PrintDialog extends BottomSheetDialogFragment {

    private com.example.geocachingapp.ui.qrcode.QRCodeViewModel QRCodeViewModel;
    private static final String TAG = "PrintDialog";

    private TextView description;

    private int printClicks = 0;
    private int sendClicks = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.bottom_modal_print_sheet,
                container, false);

        Button printButton = v.findViewById(R.id.print_button);
        Button sendButton = v.findViewById(R.id.send_button);
        description = v.findViewById(R.id.description);

        QRCodeViewModel =
                new ViewModelProvider(requireActivity()).get(QRCodeViewModel.class);

        printButton.setOnClickListener(v1 -> {
            sendClicks = 0;
            printClicks ++;
            if(printClicks > 1) {
                Bitmap codeBitmap = QRCodeViewModel.getQrCode().getValue();
                if (codeBitmap == null) {
                    Toast.makeText(requireActivity(),
                            "Code not generated", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                PrintHelper photoPrinter = new PrintHelper(requireActivity());
                photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
                photoPrinter.printBitmap("New Geocache QR Code", codeBitmap);
                dismiss();
            } else {
                description.setText(R.string.print_description);
            }
        });

        sendButton.setOnClickListener(v1 -> {
            printClicks = 0;
            sendClicks++;
            if(sendClicks > 1) {
                Bitmap codeBitmap = QRCodeViewModel.getQrCode().getValue();
                if (codeBitmap == null) {
                    Toast.makeText(requireActivity(),
                            "Code not generated", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                String pathofBmp = MediaStore.Images.Media.insertImage(requireActivity().getContentResolver(), codeBitmap,"QR Code", null);
                Uri bmpUri = Uri.parse(pathofBmp);
                Log.d("PrintDialog", bmpUri.toString());
                final Intent emailIntent1 = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent1.putExtra(Intent.EXTRA_STREAM, bmpUri);
                emailIntent1.setType("image/png");
                startActivity(emailIntent1);
                dismiss();
            } else {
                description.setText(R.string.send_description);
            }
        });

        return v;
    }
}
