package com.cctpl.societyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cctpl.societyapp.utils.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText mOwnerName;
    EditText mBuildingNumber;
    EditText mRoomNumber;
    EditText mVehicleNumber;
    EditText mVehicleDetails;
    EditText mMobileNumber;
    Button mBtnUploadDetails;
    ProgressBar mLoader;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mOwnerName = findViewById(R.id.ownerName);
        mBuildingNumber = findViewById(R.id.buildingNumber);
        mRoomNumber = findViewById(R.id.roomNumber);
        mVehicleNumber = findViewById(R.id.vehicleNumber);
        mVehicleDetails = findViewById(R.id.vehicleDetails);
        mMobileNumber = findViewById(R.id.mobileNumber);
        mBtnUploadDetails = findViewById(R.id.btnUploadDetails);
        mLoader = findViewById(R.id.loader);
        firebaseFirestore = FirebaseFirestore.getInstance();

        mBtnUploadDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String OWNER_NAME = mOwnerName.getText().toString();
                String BUILDING_NUMBER = mBuildingNumber.getText().toString();
                String ROOM_NUMBER = mRoomNumber.getText().toString();
                String VEHICLE_NUMBER = mVehicleNumber.getText().toString();
                String VEHICLE_DETAILS = mVehicleDetails.getText().toString();
                String MOBILE_NUMBER = mMobileNumber.getText().toString();

                if (TextUtils.isEmpty(OWNER_NAME)){
                    mOwnerName.setError("Owner Name");
                }else if(TextUtils.isEmpty(BUILDING_NUMBER)){
                    mBuildingNumber.setError("Building Number");
                }else if (TextUtils.isEmpty(ROOM_NUMBER)){
                    mRoomNumber.setError("Room Number");
                }else if (TextUtils.isEmpty(VEHICLE_NUMBER)){
                    mVehicleNumber.setError("Vehicle Number");
                }else if (TextUtils.isEmpty(VEHICLE_DETAILS)){
                    mVehicleDetails.setError("Vehicle Details");
                }else if (TextUtils.isEmpty(MOBILE_NUMBER)){
                    mMobileNumber.setError("Moblie Number");
                }else {
                    uploadData(OWNER_NAME,BUILDING_NUMBER,ROOM_NUMBER,VEHICLE_NUMBER,VEHICLE_DETAILS,MOBILE_NUMBER);
                    mBtnUploadDetails.setVisibility(View.GONE);
                    mLoader.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    private void uploadData(String owner_name, String building_number, String room_number, String vehicle_number, String vehicle_details, String mobile_number) {


        HashMap<Object,String > map = new HashMap<>();
        map.put(Constant.OWNER_NAME,owner_name);
        map.put(Constant.BUILDING_NUMBER,building_number);
        map.put(Constant.ROOM_NUMBER,room_number);
        map.put(Constant.VEHICLE_NUMBER,vehicle_number);
        map.put(Constant.VEHICLE_DETAILS,vehicle_details);
        map.put(Constant.MOBILE_NUMBER,"+91" + mobile_number);
        map.put(Constant.TIMESTAMP, String.valueOf(System.currentTimeMillis()));

        SharedPreferences sharedPreferences = getSharedPreferences(Constant.USER_DATA,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constant.MOBILE_NUMBER,"+91"+mobile_number);
        editor.putString(Constant.LOGGED_IN,"true");
        editor.apply();
        editor.commit();

        firebaseFirestore.collection(Constant.USER_DATA).document("+91"+mobile_number)
                .set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mBtnUploadDetails.setVisibility(View.VISIBLE);
                mLoader.setVisibility(View.GONE);
                Toast.makeText(RegisterActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}