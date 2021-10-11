package com.example.geocachingapp.ui.qrcode.parts;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.VIBRATE;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.geocachingapp.databinding.FragmentQrScanBinding;
import com.example.geocachingapp.ui.qrcode.QRCodeViewModel;

import eu.livotov.labs.android.camview.ScannerLiveView;
import eu.livotov.labs.android.camview.scanner.decoder.zxing.ZXDecoder;

/**
 * A simple {@link Fragment} subclass.
 * This scans the QR code and attaches a picture to the database.
 */
public class QrScanFragment extends Fragment {

    private FragmentQrScanBinding binding;
    private com.example.geocachingapp.ui.qrcode.QRCodeViewModel QRCodeViewModel;

    private ScannerLiveView camera;

    public static QrScanFragment newInstance() {
        return new QrScanFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
                // Inflate the layout for this fragment
        binding = FragmentQrScanBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (!checkPermission()) {
            requestPermission();
        }

        QRCodeViewModel =
                new ViewModelProvider(requireActivity()).get(QRCodeViewModel.class);

        camera = binding.camview;

        camera.setScannerViewEventListener(new ScannerLiveView.ScannerViewEventListener() {
            @Override
            public void onScannerStarted(ScannerLiveView scanner) {}

            @Override
            public void onScannerStopped(ScannerLiveView scanner) {}

            @Override
            public void onScannerError(Throwable err) { Toast.makeText(requireActivity(), "Scanner Error: " + err.getMessage(), Toast.LENGTH_SHORT).show(); }

            @Override
            public void onCodeScanned(String data) { showQrResult(data); }
        });

        return root;
    }

    public void showQrResult(String result) {
        Toast.makeText(requireContext(), "Code scanned", Toast.LENGTH_SHORT).show();
        QRCodeViewModel.setReadData(result);
    }

    @Override
    public void onResume() {
        super.onResume();
        ZXDecoder decoder = new ZXDecoder();
        // 0.5 is the area where we have
        // to place red marker for scanning.
        decoder.setScanAreaPercent(0.8);
        // below method will set decoder to camera.
        camera.setDecoder(decoder);
        camera.startScanner();
    }

    @Override
    public void onPause() {
        // on app pause the
        // camera will stop scanning.
        camera.stopScanner();
        super.onPause();
    }

    // check permissions
    private boolean checkPermission() {
        // here we are checking two permission that is vibrate
        // and camera which is granted by user and not.
        // if permission is granted then we are returning
        // true otherwise false.
        int camera_permission = ContextCompat.checkSelfPermission(requireContext(), CAMERA);
        int vibrate_permission = ContextCompat.checkSelfPermission(requireContext(), VIBRATE);
        return camera_permission == PackageManager.PERMISSION_GRANTED && vibrate_permission == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermission() {
        // this method is to request
        // the runtime permission.
        int PERMISSION_REQUEST_CODE = 200;
        ActivityCompat.requestPermissions(requireActivity(), new String[]{CAMERA, VIBRATE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        // this method is called when user
        // allows the permission to use camera.
        if (grantResults.length > 0) {
            boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean vibrateAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            if (cameraAccepted && vibrateAccepted) {
                Toast.makeText(requireActivity(), "Permission granted..", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireActivity(), "Permission Denined \n You cannot use app without providing permission", Toast.LENGTH_SHORT).show();
            }
        }
    }
}