package com.cctpl.societyapp.fragments;

import static android.graphics.Color.TRANSPARENT;
import static android.graphics.Color.WHITE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cctpl.societyapp.Adapter.HistoryAdapter;
import com.cctpl.societyapp.MainActivity;
import com.cctpl.societyapp.Model.HistoryData;
import com.cctpl.societyapp.R;
import com.cctpl.societyapp.ScannerView;
import com.cctpl.societyapp.utils.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    ImageView mQRCode;
    ImageView mScanner;
    FirebaseFirestore firebaseFirestore;
    List<HistoryData> historyData;
    HistoryAdapter historyAdapter;
    RecyclerView recyclerView;
    TextView mHideQr;
    RelativeLayout mQrLayout;
    ProgressBar mLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constant.USER_DATA,0);
        String MOBILE_NUMBER = sharedPreferences.getString(Constant.MOBILE_NUMBER,null);

        mQRCode = view.findViewById(R.id.qrCode);
        mScanner = view.findViewById(R.id.scanner);
        recyclerView = view.findViewById(R.id.recycleView);
        mHideQr = view.findViewById(R.id.hideQrCode);
        mQrLayout = view.findViewById(R.id.qrCodeLayout);
        mLoader = view.findViewById(R.id.loader);
        firebaseFirestore = FirebaseFirestore.getInstance();

        mHideQr.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (mHideQr.getText().toString().equalsIgnoreCase("Hide QR Code")){
                    mQrLayout.setVisibility(View.GONE);
                    mHideQr.setText("Show QR Code");
                }else {
                    mQrLayout.setVisibility(View.VISIBLE);
                    mHideQr.setText("Hide QR Code");
                }
            }
        });

        mScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext() , ScannerView.class));
            }
        });

        firebaseFirestore.collection(Constant.USER_DATA).document(MOBILE_NUMBER)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            String VEHICLE_NUMBER = task.getResult().get(Constant.VEHICLE_NUMBER).toString();
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
                        Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        loadHistory();

        return view;
    }

    private void loadHistory() {
        historyData = new ArrayList<>();
        historyAdapter = new HistoryAdapter(historyData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView,new RecyclerView.State(), historyAdapter.getItemCount());
        recyclerView.setAdapter(historyAdapter);

        firebaseFirestore.collection(Constant.ENTRY).orderBy("TimeStamp", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                HistoryData mHistoryData = doc.getDocument().toObject(HistoryData.class);
                                historyData.add(mHistoryData);
                                historyAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
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