package com.example.quanlyphongtro.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyphongtro.Model.RoomWithTenantInfo;
import com.example.quanlyphongtro.R;

import java.util.List;

public class ItemRoomAdapter extends RecyclerView.Adapter<ItemRoomAdapter.ItemRoomViewHolder>{
    private Context context;
    private List<RoomWithTenantInfo> roomList;

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public ItemRoomAdapter(Context context) {
        this.context = context;
    }


    public void setData(List<RoomWithTenantInfo> list){
        this.roomList = list;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ItemRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_item, parent, false);
        return new ItemRoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemRoomViewHolder holder, @SuppressLint("RecyclerView") int position) {
        RoomWithTenantInfo room = roomList.get(position);
        if(room == null){
            return;
        }
       holder.roomName.setText(room.getRoomNumber());
       holder.roomStatus.setText(room.getStatus());
       holder.userNumber.setText(String.valueOf(room.getUserNumber()));

        if ("Đã đủ người".equals(room.getStatus())) {
            holder.roomStatus.setTextColor(context.getResources().getColor(R.color.red));
        } else if("Đã có người thuê".equals(room.getStatus())) {
            holder.roomStatus.setTextColor(context.getResources().getColor(R.color.orange));
        }else{
            holder.roomStatus.setTextColor(context.getResources().getColor(R.color.green));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        if(roomList != null){
            return roomList.size();
        }
        return 0;
    }


    public class ItemRoomViewHolder extends RecyclerView.ViewHolder {
        private TextView roomName;
        private TextView roomStatus;
        private TextView userNumber;

        public ItemRoomViewHolder(@NonNull View itemView) {
            super(itemView);
            roomName = itemView.findViewById(R.id.tv_room_name);
            roomStatus = itemView.findViewById(R.id.tv_room_status);
            userNumber = itemView.findViewById(R.id.tv_user_number);
        }
    }
}
