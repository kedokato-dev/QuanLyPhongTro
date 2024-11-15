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

import com.example.quanlyphongtro.R;
import com.example.quanlyphongtro.pojo.ServiceInBillPOJO;

import java.util.List;
import java.util.Locale;

public class ItemServiceAddBillAdapter extends RecyclerView.Adapter<ItemServiceAddBillAdapter.ItemServiceAddBillViewHolder> {
    private Context context;
    private List<ServiceInBillPOJO> listServices;

    private OnItemClickListener onItemClickListener;

    public ItemServiceAddBillAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<ServiceInBillPOJO> list){
       this.listServices = list;
       notifyDataSetChanged();
    }

    public void setOnItemClickListener (OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener{
        void onClickDelete(int position);
    }

    @NonNull
    @Override
    public ItemServiceAddBillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service_bill_curd, parent, false);
        return new ItemServiceAddBillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemServiceAddBillViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ServiceInBillPOJO service = listServices.get(position);
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

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener != null){
                    onItemClickListener.onClickDelete(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if(listServices != null){
            return listServices.size();
        }
        return 0;
    }


    public class ItemServiceAddBillViewHolder extends RecyclerView.ViewHolder{
        private TextView serviceName;
        private TextView pricePerUnit;
        private TextView quantity;
        private TextView amount;
        private TextView donViTinh;
        private ImageView img;

        public ItemServiceAddBillViewHolder(@NonNull View itemView) {
            super(itemView);

            serviceName = itemView.findViewById(R.id.tv_service_name);
            pricePerUnit = itemView.findViewById(R.id.tv_price_per_unit);
            quantity = itemView.findViewById(R.id.tv_quantity);
            amount = itemView.findViewById(R.id.tv_amount);
            donViTinh = itemView.findViewById(R.id.tv_don_vi_tinh);

             img = itemView.findViewById(R.id.img_delete);

        }
    }
}
