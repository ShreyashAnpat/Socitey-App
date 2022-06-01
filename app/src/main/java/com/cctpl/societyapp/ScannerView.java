package com.cctpl.societyapp;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cctpl.societyapp.utils.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.HashMap;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class ScannerView extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView scannerView;
    String STATUS;
    String OWNER_NAME;
    String VEHICLE_NAME;
    String VEHICLE_NUMBER;
    String MOBILE_NUMBER;
    String BUILDING_NUMBER;
    String ROOM_NUMBER;
    String OWNER_TYPE;
    String VEHICLE_TYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.cancelPermissionRequest();
                    }
                }).check();
    }


    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        scannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        if (rawResult.getText().length() > 0) {
            // calling send activity instead of send screen activity

            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.entry_dialog);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            TextView mData = dialog.findViewById(R.id.data);
            RadioButton mIN = dialog.findViewById(R.id.in);
            RadioButton mOUT = dialog.findViewById(R.id.out);
            Button mBtnEntry = dialog.findViewById(R.id.btnEntry);
            ProgressBar mLoader = dialog.findViewById(R.id.loader);

            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseFirestore.collection(Constant.USER_DATA).whereEqualTo(Constant.VEHICLE_NUMBER, rawResult.getText().toString())
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                 OWNER_NAME = task.getResult().getDocuments().get(0).getString(Constant.OWNER_NAME);
                                 VEHICLE_NAME = task.getResult().getDocuments().get(0).getString(Constant.VEHICLE_DETAILS);
                                 VEHICLE_NUMBER = task.getResult().getDocuments().get(0).getString(Constant.VEHICLE_NUMBER);
                                 MOBILE_NUMBER = task.getResult().getDocuments().get(0).getString(Constant.MOBILE_NUMBER);
                                 BUILDING_NUMBER = task.getResult().getDocuments().get(0).getString(Constant.BUILDING_NUMBER);
                                 ROOM_NUMBER = task.getResult().getDocuments().get(0).getString(Constant.ROOM_NUMBER);
                                 OWNER_TYPE = task.getResult().getDocuments().get(0).getString(Constant.OWNER_TYPE);
                                 VEHICLE_TYPE = task.getResult().getDocuments().get(0).getString(Constant.VEHICLE);

                                mData.setText("Owner Name       :  " + OWNER_NAME + "\n\n" +
                                              "Address          :  " + BUILDING_NUMBER + " " + ROOM_NUMBER +"\n\n" +
                                              "Owner Type       :  " + OWNER_TYPE + "\n\n" +
                                              "Vehicle Type     :  " + VEHICLE_TYPE + "\n\n" +
                                              "Vehicle Details  :  " + VEHICLE_NAME + "\n\n" +
                                              "Vehicle Number   :  " + VEHICLE_NUMBER + "\n\n" +
                                              "Mobile Number    :  " + MOBILE_NUMBER);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ScannerView.this, "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


            mIN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    STATUS = "IN";
                }
            });

            mOUT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    STATUS = "OUT";
                }
            });

            mBtnEntry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (TextUtils.isEmpty(STATUS)){
                        Toast.makeText(ScannerView.this, "Select IN/OUT Option", Toast.LENGTH_SHORT).show();
                    }else {

                        mLoader.setVisibility(View.VISIBLE);
                        mBtnEntry.setVisibility(View.GONE);
                        HashMap<Object,String> map = new HashMap<>();
                        map.put(Constant.OWNER_NAME,OWNER_NAME);
                        map.put(Constant.VEHICLE_DETAILS,VEHICLE_NAME);
                        map.put(Constant.VEHICLE_NUMBER,VEHICLE_NUMBER);
                        map.put(Constant.MOBILE_NUMBER,MOBILE_NUMBER);
                        map.put(Constant.TIMESTAMP,String.valueOf(System.currentTimeMillis()));
                        map.put(Constant.STATUS,STATUS);
                        map.put(Constant.BUILDING_NUMBER,BUILDING_NUMBER);
                        map.put(Constant.ROOM_NUMBER,ROOM_NUMBER);
                        map.put(Constant.OWNER_TYPE,OWNER_TYPE);
                        map.put(Constant.VEHICLE,VEHICLE_TYPE);

                        firebaseFirestore.collection(Constant.ENTRY).document(String.valueOf(System.currentTimeMillis()))
                                .set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(ScannerView.this, "Entry successfully...", Toast.LENGTH_SHORT).show();
                                            mLoader.setVisibility(View.GONE);
                                            mBtnEntry.setVisibility(View.VISIBLE);
                                            onBackPressed();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        mLoader.setVisibility(View.GONE);
                                        mBtnEntry.setVisibility(View.VISIBLE);
                                        Toast.makeText(ScannerView.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
//                    onBackPressed();
                }
            });

        } else {
            scannerView.resumeCameraPreview(this);

        }
    }
}