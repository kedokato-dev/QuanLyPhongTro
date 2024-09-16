package com.example.quanlyphongtro.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.NumberFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyphongtro.model.ItemBillPOJO;
import com.example.quanlyphongtro.R;

import java.util.List;
import java.util.Locale;

public class ItemBillAdapter extends RecyclerView.Adapter<ItemBillAdapter.ItemBillViewholder>
{
    private Context context;
    private List<ItemBillPOJO> itemBillPOJOList;

    private OnItemClickListener onItemClickListener;

    public void setData(List<ItemBillPOJO> list){
        this.itemBillPOJOList = list;
        notifyDataSetChanged();
    }

    public ItemBillAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemClickListener (OnItemClickListener listener){
        this.onItemClickListener = listener;
    }

    // sử dụng call back để tạo sự kiện click ở bên fragment
    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    @NonNull
    @Override
    public ItemBillViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);
        return new ItemBillViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemBillViewholder holder, @SuppressLint("RecyclerView") int position) {
        ItemBillPOJO itemBillPOJO = itemBillPOJOList.get(position);
        if(itemBillPOJO == null){
            return;
        }

        holder.roomNumber.setText(itemBillPOJO.getRoomNumber());
        holder.issueDate.setText(itemBillPOJO.getIssueDate());

        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedTotalAmount = numberFormat.format(itemBillPOJO.getTotalAmount());

        holder.totalAmount.setText(formattedTotalAmount);
        holder.status.setText(itemBillPOJO.getStatus());

        if("Chưa thanh toán".equals(itemBillPOJO.getStatus())){
            holder.status.setTextColor(context.getResources().getColor(R.color.red));
        }else{
            holder.status.setTextColor(context.getResources().getColor(R.color.green));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(itemBillPOJOList != null){
            return itemBillPOJOList.size();
        }
        return 0;
    }

    public class ItemBillViewholder extends RecyclerView.ViewHolder{
        private TextView roomNumber;
        private TextView issueDate;
        private TextView totalAmount;
        private TextView status;
        public ItemBillViewholder(@NonNull View itemView) {
            super(itemView);

            roomNumber = itemView.findViewById(R.id.tv_room_number);
            issueDate = itemView.findViewById(R.id.tv_issue_date);
            totalAmount = itemView.findViewById(R.id.tv_total_price);
            status = itemView.findViewById(R.id.tv_status_bill);
        }
    }
}
