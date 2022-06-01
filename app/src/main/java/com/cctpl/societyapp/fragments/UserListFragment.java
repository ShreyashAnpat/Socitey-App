package com.cctpl.societyapp.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cctpl.societyapp.Adapter.HistoryAdapter;
import com.cctpl.societyapp.Adapter.UserListAdapter;
import com.cctpl.societyapp.Model.HistoryData;
import com.cctpl.societyapp.Model.UserData;
import com.cctpl.societyapp.R;
import com.cctpl.societyapp.utils.Constant;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserListFragment extends Fragment {


    List<UserData> userData;
    UserListAdapter userListAdapter;
    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        recyclerView = view.findViewById(R.id.recycleView);
        loadUserList();
        return view;
    }

    private void loadUserList() {
        userData = new ArrayList<>();
        userListAdapter = new UserListAdapter(userData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView,new RecyclerView.State(), userListAdapter.getItemCount());
        recyclerView.setAdapter(userListAdapter);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection(Constant.USER_DATA).orderBy("TimeStamp", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                UserData mUserData = doc.getDocument().toObject(UserData.class);
                                userData.add(mUserData);
                                userListAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
}