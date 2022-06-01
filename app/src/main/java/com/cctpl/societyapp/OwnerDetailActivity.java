package com.cctpl.societyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.cctpl.societyapp.utils.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class OwnerDetailActivity extends AppCompatActivity {

    String MOBILE_NUMBER;
    EditText mOwnerName;
    EditText mVehicleName;
    EditText mVehicleNumber;
    EditText mMobileNumber;
    EditText mBuildingNumber;
    EditText mRoomNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_detail);

        MOBILE_NUMBER = getIntent().getStringExtra(Constant.MOBILE_NUMBER);
        mOwnerName = findViewById(R.id.ownerName);
        mVehicleName = findViewById(R.id.vehicleDetails);
        mVehicleNumber = findViewById(R.id.vehicleNumber);
        mMobileNumber = findViewById(R.id.mobileNumber);
        mBuildingNumber = findViewById(R.id.buildingNumber);
        mRoomNumber = findViewById(R.id.roomNumber);

        if (!TextUtils.isEmpty(MOBILE_NUMBER)){
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseFirestore.collection(Constant.USER_DATA).document(MOBILE_NUMBER)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                mOwnerName.setText(task.getResult().get(Constant.OWNER_NAME).toString());
                                mVehicleName.setText(task.getResult().get(Constant.VEHICLE_DETAILS).toString());
                                mVehicleNumber.setText(task.getResult().get(Constant.VEHICLE_NUMBER).toString());
                                mMobileNumber.setText(task.getResult().get(Constant.MOBILE_NUMBER).toString());
                                mBuildingNumber.setText(task.getResult().get(Constant.BUILDING_NUMBER).toString());
                                mRoomNumber.setText(task.getResult().get(Constant.ROOM_NUMBER).toString());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(OwnerDetailActivity.this, "Error : " + e.getMessage() , Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }
}