package com.example.quanlyphongtro.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyphongtro.pojo.RoomWithTenantInfo;
import com.example.quanlyphongtro.R;

import java.util.List;

public class ItemRoomAdapter extends RecyclerView.Adapter<ItemRoomAdapter.ItemRoomViewHolder>{
    private Context context;
    private List<RoomWithTenantInfo> roomList;

    private SetOnItemClickListener setOnItemClickListener;

    private OnDeleteClickListener onDeleteClickListener;
    private OnUpdateClickListener onUpdateClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public interface OnUpdateClickListener {
        void onUpdateClick(int position);
    }

    public interface SetOnItemClickListener{
        void onItemClick(int position);
    }


    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = (OnDeleteClickListener) listener;
    }

    public void setOnUpdateClickListener(OnUpdateClickListener listener) {
        this.onUpdateClickListener = (OnUpdateClickListener) listener;
    }


    public void setOnClickListener(SetOnItemClickListener listener){
        this.setOnItemClickListener = listener;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
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
                if (setOnItemClickListener != null) {
                    setOnItemClickListener.onItemClick(position);
                }
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onDeleteClickListener != null){
                    onDeleteClickListener.onDeleteClick(position);
                }
            }
        });

        holder.imgUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onUpdateClickListener != null){
                    onUpdateClickListener.onUpdateClick(position);
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
        private ImageView imgUpdate, imgDelete;

        public ItemRoomViewHolder(@NonNull View itemView) {
            super(itemView);
            roomName = itemView.findViewById(R.id.tv_room_name);
            roomStatus = itemView.findViewById(R.id.tv_room_status);
            userNumber = itemView.findViewById(R.id.tv_user_number);

            imgDelete = itemView.findViewById(R.id.delete_icon);
            imgUpdate = itemView.findViewById(R.id.update_icon);
        }
    }
}
