package com.cctpl.societyapp;

import static android.graphics.Color.TRANSPARENT;
import static android.graphics.Color.WHITE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cctpl.societyapp.utils.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class OwnerDetailActivity extends AppCompatActivity {

    String MOBILE_NUMBER;
    EditText mOwnerName;
    EditText mVehicleName;
    EditText mVehicleNumber;
    EditText mMobileNumber;
    EditText mBuildingNumber;
    EditText mRoomNumber;
    ImageView mQRCode;
    ProgressBar mLoader;

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
        mQRCode = findViewById(R.id.qrCode);
        mLoader = findViewById(R.id.loader);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection(Constant.USER_DATA).document(MOBILE_NUMBER)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    String VEHICLE_NUMBER = task.getResult().get(Constant.VEHICLE_NUMBER).toString();
                    String OWNER_NAME = task.getResult().get(Constant.OWNER_NAME).toString();
                    TextView text = findViewById(R.id.text);
                    text.setText(OWNER_NAME);
                    try {
                        Bitmap bitmap = encodeAsBitmap(VEHICLE_NUMBER);
                        mQRCode.setImageBitmap(bitmap);
                        mLoader.setVisibility(View.GONE);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OwnerDetailActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        if (!TextUtils.isEmpty(MOBILE_NUMBER)){
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

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, 356, 356, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? WHITE : TRANSPARENT;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 356, 0, 0, w, h);
        return bitmap;
    }
}