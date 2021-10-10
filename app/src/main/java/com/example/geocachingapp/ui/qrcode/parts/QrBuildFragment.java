package com.example.geocachingapp.ui.qrcode.parts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geocachingapp.R;
import com.example.geocachingapp.databinding.FragmentQrBuildBinding;
import com.example.geocachingapp.databinding.FragmentQrScanBinding;
import com.example.geocachingapp.ui.qrcode.QRCodeViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QrBuildFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QrBuildFragment extends Fragment {

    private FragmentQrBuildBinding binding;
    private com.example.geocachingapp.ui.qrcode.QRCodeViewModel QRCodeViewModel;

    private TextView notScannedView;

    private JSONObject readData;
    private String id;

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

        QRCodeViewModel =
                new ViewModelProvider(requireActivity()).get(com.example.geocachingapp.ui.qrcode.QRCodeViewModel.class);

        QRCodeViewModel.getReadData().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                readQrData(s);
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        readQrData(QRCodeViewModel.getReadData().getValue());
    }

    public void readQrData(String s) {
        Log.d("QR Build Fragment", s + "");
        if(s == null) {
            notScannedView.setText(R.string.not_scanned_text);
        } else {
            try {
                readData = new JSONObject(s);
                id = readData.getString("Key");
                notScannedView.setText(id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}