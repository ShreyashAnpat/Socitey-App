package com.cctpl.societyapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.cctpl.societyapp.Model.HistoryData;
import com.cctpl.societyapp.Model.UserData;
import com.cctpl.societyapp.OwnerDetailActivity;
import com.cctpl.societyapp.R;
import com.cctpl.societyapp.utils.Constant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder>{

    List<UserData> userData;
    Context context;

    public UserListAdapter(List<UserData> userData) {
        this.userData = userData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.mFullName.setText(userData.get(position).getOwnerName().toString());
        holder.mFullName.setSelected(true);
        holder.mBuildingNumber.setText(userData.get(position).getRoomNumber() + ", " + userData.get(position).getBuildingNumber());
        holder.mMobileNumber.setText(userData.get(position).getMobileNumber());

        holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) context;
                Intent intent = new Intent(context, OwnerDetailActivity.class);
                intent.putExtra(Constant.MOBILE_NUMBER,userData.get(position).getMobileNumber());
                intent.putExtra(Constant.OWNER_NAME,userData.get(position).getOwnerName());
                activity.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return userData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mFullName;
        TextView mBuildingNumber;
        ImageView mViewQR;
        ImageView mProfileImg;
        TextView mMobileNumber;
        RelativeLayout mRelativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mFullName = itemView.findViewById(R.id.fullName);
            mBuildingNumber = itemView.findViewById(R.id.buildingNumber);
            mViewQR = itemView.findViewById(R.id.btnViewQR);
            mProfileImg = itemView.findViewById(R.id.profileImg);
            mMobileNumber = itemView.findViewById(R.id.mobileNumber);
            mRelativeLayout = itemView.findViewById(R.id.relativeLayout);

        }
    }
}
