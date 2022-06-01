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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constant.USER_DATA,0);
        String MOBILE_NUMBER = sharedPreferences.getString(Constant.MOBILE_NUMBER,null);


        mScanner = view.findViewById(R.id.scanner);
        recyclerView = view.findViewById(R.id.recycleView);
        mHideQr = view.findViewById(R.id.hideQrCode);
        mQrLayout = view.findViewById(R.id.qrCodeLayout);
        firebaseFirestore = FirebaseFirestore.getInstance();


        mScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext() , ScannerView.class));
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

}