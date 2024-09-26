package com.example.quanlyphongtro.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyphongtro.R;
import com.example.quanlyphongtro.database.QuanLyPhongTroDB;
import com.example.quanlyphongtro.pojo.UserPOJO;

import java.util.List;

public class ItemUserListAdapter extends RecyclerView.Adapter<ItemUserListAdapter.ItemUserViewHoler> implements Filterable {
    private Context context;
    private List<UserPOJO> userPOJOList;
    private List<UserPOJO> userPOJOListOld;
    private OnClickItemUpdateListener onClickItemUpdateListener;
    private OnClickItemDeleteListener onClickItemDeleteListener;

   private QuanLyPhongTroDB database;

    public ItemUserListAdapter(Context context) {
        this.context = context;
        this.database = QuanLyPhongTroDB.getInstance(context);

    }

    public ItemUserListAdapter (List<UserPOJO> userPOJOList){
        this.userPOJOList = userPOJOList;
        this.userPOJOListOld = userPOJOList;
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchQuery = charSequence.toString().trim();
                userPOJOListOld = database.userDAO().getListUser();
                if (searchQuery.isEmpty()) {
                    userPOJOList = userPOJOListOld;
                } else {
                    userPOJOList = database.userDAO().searchMemberByName(searchQuery);
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = userPOJOList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                userPOJOList = (List<UserPOJO>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
