package com.example.quanlyphongtro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyphongtro.Model.MenuItem;
import com.example.quanlyphongtro.R;

import java.util.List;

public class ItemMenuAdapter extends RecyclerView.Adapter<ItemMenuAdapter.ItemViewHolder> {

    private Context mContext;
    private List<MenuItem> mlistMenu;

    public ItemMenuAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<MenuItem> list){
        this.mlistMenu = list;
        notifyDataSetChanged(); // load và tải dữ liệu lên recycle view
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        MenuItem menu = (MenuItem) mlistMenu.get(position);
        if(menu == null){
            return;
        }
        holder.imgItem.setImageResource(menu.getResourceId());
        holder.tv_menu.setText(menu.getNameMenu());
        holder.tv_sub_menu.setText(menu.getSubNameMenu());
    }

    @Override
    public int getItemCount() {
        if(mlistMenu != null){
            return mlistMenu.size();
        }
        return 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgItem;
        private TextView tv_menu;
        private TextView tv_sub_menu;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            imgItem = itemView.findViewById(R.id.img_menu);
            tv_menu = itemView.findViewById(R.id.tv_menu);
            tv_sub_menu = itemView.findViewById(R.id.tv_sub_menu);
        }
    }
}
