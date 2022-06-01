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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import com.cctpl.societyapp.Model.HistoryData;
import com.cctpl.societyapp.OwnerDetailActivity;
import com.cctpl.societyapp.R;
import com.cctpl.societyapp.utils.Constant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{

    List<HistoryData> historyData;
    Context context;

    public HistoryAdapter(List<HistoryData> historyData) {
        this.historyData = historyData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_view,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String STATUS = historyData.get(position).getSTATUS();

        if (historyData.get(position).getSTATUS().equalsIgnoreCase("IN")){
            holder.mStatus.setText(STATUS);
            holder.mStatus.setTextColor(Color.parseColor("#FF00C853"));
        }else {
            holder.mStatus.setText(STATUS);
            holder.mStatus.setTextColor(Color.parseColor("#FFDD2C00"));
        }

        if (historyData.get(position).getVehicle()!=null){
            if (historyData.get(position).getVehicle().equalsIgnoreCase("2 Wheeler")){
                holder.mStatusImg.setImageResource(R.drawable.bike);
            }else if (historyData.get(position).getVehicle().equalsIgnoreCase("3 Wheeler")){
                holder.mStatusImg.setImageResource(R.drawable.rickshaw);
            }else if (historyData.get(position).getVehicle().equalsIgnoreCase("4 Wheeler")){
                holder.mStatusImg.setImageResource(R.drawable.car);
            }
        }

//        TextView marquee = view.findViewById(R.id.marquee);
//        marquee.setSelected(true);
        holder.mCarName.setText(historyData.get(position).getVehicleDetails());
        holder.mCarName.setSelected(true);
        holder.mCarNumber.setText(historyData.get(position).getVehicleNumber());

        Date d = new Date(Long.parseLong(historyData.get(position).getTimeStamp()));

        DateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
        String currentDate = dateFormat.format(d.getTime());
        DateFormat dateFormat1 = new SimpleDateFormat("MMM dd");
        String Date = dateFormat1.format(d.getTime());

        holder.mTime.setText(Date + "\n" + currentDate);

        holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) context;
                Intent intent = new Intent(context, OwnerDetailActivity.class);
                intent.putExtra(Constant.MOBILE_NUMBER,historyData.get(position).getMobileNumber());
                activity.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return historyData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mCarName;
        TextView mCarNumber;
        TextView mTime;
        ImageView mStatusImg;
        TextView mStatus;
        RelativeLayout mRelativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mCarName = itemView.findViewById(R.id.carName);
            mCarNumber = itemView.findViewById(R.id.carNumber);
            mTime = itemView.findViewById(R.id.time);
            mStatusImg = itemView.findViewById(R.id.statusImg);
            mStatus = itemView.findViewById(R.id.status);
            mRelativeLayout = itemView.findViewById(R.id.relativeLayout);

        }
    }
}
