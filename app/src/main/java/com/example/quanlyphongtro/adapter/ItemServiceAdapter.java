package com.example.quanlyphongtro.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.NumberFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyphongtro.pojo.ServicePOJO;
import com.example.quanlyphongtro.R;

import java.util.List;
import java.util.Locale;

public class ItemServiceAdapter extends RecyclerView.Adapter<ItemServiceAdapter.ItemServicesViewHolder> {

    private Context context;
    private List<ServicePOJO> serviceList;

    private OnDeleteClickListener onDeleteClickListener;
    private OnUpdateClickListener onUpdateClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public interface OnUpdateClickListener {
        void onUpdateClick(int position);
    }


    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    public void setOnUpdateClickListener(OnUpdateClickListener listener) {
        this.onUpdateClickListener = listener;
    }


    public ItemServiceAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<ServicePOJO> list) {
        this.serviceList = list;
        notifyDataSetChanged();
    }


    public void removeItem(int position) {
        if (position >= 0 && position < serviceList.size()) {
            serviceList.remove(position);
            notifyItemRemoved(position);
        }
    }


    @NonNull
    @Override
    public ItemServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new ItemServicesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemServicesViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ServicePOJO service = serviceList.get(position);
        if (service == null) {
            return;
        }

        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = numberFormat.format(service.getPricePerUnit());
        holder.serviceName.setText(service.getServiceName());
        holder.servicePrice.setText(formattedPrice);

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(position);
                }
            }
        });

        holder.imgUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onUpdateClickListener != null) {
                    onUpdateClickListener.onUpdateClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (serviceList != null) {
            return serviceList.size();
        }
        return 0;
    }

    public class ItemServicesViewHolder extends RecyclerView.ViewHolder {

        private TextView serviceName;
        private TextView servicePrice;

        private ImageView imgDelete;
        private ImageView imgUpdate;

        public ItemServicesViewHolder(@NonNull View itemView) {
            super(itemView);

            serviceName = itemView.findViewById(R.id.tv_service_name);
            servicePrice = itemView.findViewById(R.id.tv_service_price);
            imgDelete = itemView.findViewById(R.id.delete_icon);
            imgUpdate = itemView.findViewById(R.id.update_icon);
        }
    }


}
