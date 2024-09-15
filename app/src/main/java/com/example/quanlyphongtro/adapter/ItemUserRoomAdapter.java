package com.example.quanlyphongtro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyphongtro.Model.UserDetailPOJO;
import com.example.quanlyphongtro.R;

import java.util.List;

public class ItemUserRoomAdapter extends RecyclerView.Adapter<ItemUserRoomAdapter.ItemUserRoomViewHolder> {
    private List<UserDetailPOJO> userListPOJO;
    private Context context;

    public  ItemUserRoomAdapter(Context context){
        this.context = context;
    }

    public void setData(List<UserDetailPOJO> list){
        this.userListPOJO = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ItemUserRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_room, parent, false);
        return new ItemUserRoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemUserRoomViewHolder holder, int position) {
        UserDetailPOJO userDetailPOJO = userListPOJO.get(position);
        if(userDetailPOJO == null){
            return;
        }
        holder.memberName.setText(userDetailPOJO.getFullName());
        holder.memberPhone.setText(userDetailPOJO.getPhone());
    }

    @Override
    public int getItemCount() {
       if(userListPOJO != null){
           return userListPOJO.size();
       }
       return 0;
    }

    public class ItemUserRoomViewHolder extends RecyclerView.ViewHolder{
        private TextView memberName;
        private TextView memberPhone;
        public ItemUserRoomViewHolder(@NonNull View itemView) {
            super(itemView);

            memberName = itemView.findViewById(R.id.member_name);
            memberPhone = itemView.findViewById(R.id.member_phone);
        }
    }
}
