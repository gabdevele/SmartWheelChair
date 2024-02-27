package com.example.testapp.fragments.welcome;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.testapp.R;
import com.example.testapp.Utilities;

public class WelcomePermessi extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.concedi).setOnClickListener(l -> {
            // Controlla che i permessi siano stati concessi
            if (!hasBluetoothPermission()) {
//                Richiesta permessi per Android 12 e superiori
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT);
                 else // Richiesta permessi per Android 11 e inferiori - il codice esiste ma non è stato testato per 11, su 10 dovrebbe funzionare
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN}, 1);
            } // Se i permessi sono stati concessi, passa al prossimo fragment
            else next();
        });
    }

    private void next() {
        getParentFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.by_right, R.anim.to_left)
                .replace(R.id.fragments, new WelcomePeso())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome_permessi, container, false);
    }

    private boolean hasBluetoothPermission() {
        if (android.os.Build.VERSION.SDK_INT >= 31)
            return Utilities.checkPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT);
        else
            return Utilities.checkPermission(getContext(), Manifest.permission.BLUETOOTH) && Utilities.checkPermission(getContext(), Manifest.permission.BLUETOOTH_ADMIN);
    }

// Richiesta permessi per Android 11 e inferiori
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED)
                next();
            else
                Toast.makeText(getContext(), "Per favore, accettare i permessi", Toast.LENGTH_SHORT).show();
        }
    }

    // Richiesta permessi per Android 12 e superiori
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            r -> {
                if (r) next();
                else
                    Toast.makeText(getContext(), "Per favore, accettare i permessi", Toast.LENGTH_SHORT);
            }
    );
}