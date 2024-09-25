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

import com.example.quanlyphongtro.pojo.UserPOJO;
import com.example.quanlyphongtro.R;

import java.util.List;

public class ItemUserListAdapter extends RecyclerView.Adapter<ItemUserListAdapter.ItemUserViewHoler> {
    private Context context;
    private List<UserPOJO> userPOJOList;
    private OnClickItemUpdateListener onClickItemUpdateListener;
    private OnClickItemDeleteListener onClickItemDeleteListener;

    public ItemUserListAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<UserPOJO> list){
        this.userPOJOList = list;
        notifyDataSetChanged();
    }

    public void setOnClickItemUpdateListener(OnClickItemUpdateListener onClickItemUpdateListener) {
        this.onClickItemUpdateListener = onClickItemUpdateListener;
    }

    public void setOnClickItemDeleteListener (OnClickItemDeleteListener onClickItemDeleteListener) {
        this.onClickItemDeleteListener = onClickItemDeleteListener;
    }

    public interface OnClickItemUpdateListener{
        void onClickUpdateItem(int position);
    }

    public interface OnClickItemDeleteListener{
        void onClickDeleteItem(int position);
    }

    @NonNull
    @Override
    public ItemUserViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list, parent,false);
        return new ItemUserViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemUserViewHoler holder, @SuppressLint("RecyclerView") int position) {
        UserPOJO userPOJO = userPOJOList.get(position);
        if(userPOJO == null){
            return;
        }

        holder.fullName.setText(userPOJO.getFullName());
        holder.phone.setText(userPOJO.getPhone());
        holder.email.setText(userPOJO.getEmail());
        holder.identityCard.setText(userPOJO.getIdentityCard());

        holder.imgUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onClickItemUpdateListener != null){
                    onClickItemUpdateListener.onClickUpdateItem(position);
                }
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onClickItemDeleteListener != null){
                    onClickItemDeleteListener.onClickDeleteItem(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if(userPOJOList != null){
            return  userPOJOList.size();
        }
        return 0;
    }

    public class ItemUserViewHoler extends RecyclerView.ViewHolder {
        private TextView fullName;
        private TextView phone;
        private TextView email;
        private TextView identityCard;
        private ImageView imgUpdate, imgDelete;
        public ItemUserViewHoler(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.tv_full_name);
            phone = itemView.findViewById(R.id.tv_phone);
            email = itemView.findViewById(R.id.tv_email);
            identityCard = itemView.findViewById(R.id.tv_identity_card);

            imgUpdate = itemView.findViewById(R.id.update_icon);
            imgDelete = itemView.findViewById(R.id.delete_icon);
        }
    }
}
