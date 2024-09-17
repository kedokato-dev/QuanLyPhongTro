package com.example.quanlyphongtro.adapter;

import android.content.Context;
import android.icu.text.NumberFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyphongtro.R;
import com.example.quanlyphongtro.pojo.ServiceInBillPOJO;

import java.util.List;
import java.util.Locale;

public class ItemServiceBillAdapter extends RecyclerView.Adapter<ItemServiceBillAdapter.ItemServiceBillViewHolder> {
    private List<ServiceInBillPOJO> listService;
    private Context context;

    public ItemServiceBillAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<ServiceInBillPOJO> list){
        this.listService = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemServiceBillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service_bill, parent, false);
        return new ItemServiceBillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemServiceBillViewHolder holder, int position) {
        ServiceInBillPOJO service = listService.get(position);
        if(service == null){
            return;
        }

        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPricePerUnit = numberFormat.format(service.getPricePerUnit());
        String formattedAmount = numberFormat.format(service.getAmount());

        holder.serviceName.setText(service.getServiceName());
        holder.pricePerUnit.setText(formattedPricePerUnit);
        holder.quantity.setText(String.valueOf(service.getQuantity()));
        holder.amount.setText(formattedAmount);

        if("Điện".equals(service.getServiceName())){
            holder.donViTinh.setText("(số điện)");
        }else if("Nước".equals(service.getServiceName())){
            holder.donViTinh.setText("(số nước)");
        }else {
            holder.donViTinh.setText("(phòng)");
        }

    }

    @Override
    public int getItemCount() {
        if(listService != null){
            return  listService.size();
        }
        return 0;
    }

    public class ItemServiceBillViewHolder extends RecyclerView.ViewHolder{
        private TextView serviceName;
        private TextView pricePerUnit;
        private TextView quantity;
        private TextView amount;
        private TextView donViTinh;
        public ItemServiceBillViewHolder(@NonNull View itemView) {
            super(itemView);

            serviceName = itemView.findViewById(R.id.tv_service_name);
            pricePerUnit = itemView.findViewById(R.id.tv_price_per_unit);
            quantity = itemView.findViewById(R.id.tv_quantity);
            amount = itemView.findViewById(R.id.tv_amount);
            donViTinh = itemView.findViewById(R.id.tv_don_vi_tinh);

        }
    }
}
