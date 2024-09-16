package com.example.quanlyphongtro.adapter;

import android.content.Context;
import android.icu.text.NumberFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyphongtro.model.ServicePOJO;
import com.example.quanlyphongtro.R;

import java.util.List;
import java.util.Locale;

public class ItemServiceAdapter extends RecyclerView.Adapter<ItemServiceAdapter.ItemServicesViewHolder>{

   private Context context;
   private List<ServicePOJO> serviceList;



    public ItemServiceAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<ServicePOJO> list){
        this.serviceList = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ItemServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.services_item, parent, false);
        return new ItemServicesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemServicesViewHolder holder, int position) {
        ServicePOJO service = serviceList.get(position);
        if(service == null){
            return;
        }

        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = numberFormat.format(service.getPricePerUnit());
        holder.serviceName.setText(service.getServiceName());
        holder.servicePrice.setText(formattedPrice);
    }

    @Override
    public int getItemCount() {
        if(serviceList != null){
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
