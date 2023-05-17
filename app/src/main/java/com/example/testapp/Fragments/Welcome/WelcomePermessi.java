package com.example.testapp.Fragments.Welcome;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testapp.R;

import java.util.ArrayList;
import java.util.List;

public class WelcomePermessi extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome_permessi, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPermissionsNotGranted();
    }

    public List<String> getPermissionsNotGranted() {
        List<String> permissionsNonGranted=null;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                PackageInfo packageInfo = getContext().getPackageManager().getPackageInfo(getActivity().getPackageName(), PackageManager.GET_PERMISSIONS);
                for (int i = 0; i < packageInfo.requestedPermissions.length; i++) {
                    String requestedPermission=packageInfo.requestedPermissions[i];
                    Log.d("PERMISSION", requestedPermission);
                    int permissionCheck = ContextCompat.checkSelfPermission(getContext(), requestedPermission);
                    if(permissionCheck == PackageManager.PERMISSION_DENIED) {
                        if(permissionsNonGranted==null)
                            permissionsNonGranted= new ArrayList<>();
                        permissionsNonGranted.add(requestedPermission);
                    }
                }
            }
            catch(PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return permissionsNonGranted;
    }
}