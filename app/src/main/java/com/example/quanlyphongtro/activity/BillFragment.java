package com.example.quanlyphongtro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyphongtro.model.ItemBillPOJO;
import com.example.quanlyphongtro.R;
import com.example.quanlyphongtro.adapter.ItemBillAdapter;
import com.example.quanlyphongtro.database.QuanLyPhongTroDB;

import java.util.ArrayList;
import java.util.List;


public class BillFragment extends Fragment {
    private RecyclerView rcv_item_bill;
    private List<ItemBillPOJO> itemBillPOJOList;
    private QuanLyPhongTroDB database;
    private ItemBillAdapter itemBillAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_bill, container, false);

        rcv_item_bill = view.findViewById(R.id.rcv_item_bill);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcv_item_bill.setLayoutManager(linearLayoutManager);

        itemBillPOJOList = new ArrayList<>();
        itemBillAdapter = new ItemBillAdapter(getContext());

        database = QuanLyPhongTroDB.getInstance(getContext());
        itemBillPOJOList = database.billDAO().getListItemBill();
        itemBillAdapter.setData(itemBillPOJOList);

        rcv_item_bill.setAdapter(itemBillAdapter);

        itemBillAdapter.setOnItemClickListener(new ItemBillAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String roomNumber = itemBillPOJOList.get(position).getRoomNumber();
                String status = itemBillPOJOList.get(position).getStatus();
                Double totalAmount = itemBillPOJOList.get(position).getTotalAmount();

                // Tạo Intent để truyền dữ liệu
                Intent intent = new Intent(getContext(), DetailBillActivity.class);
                intent.putExtra("roomNumber", roomNumber);
                intent.putExtra("totalAmount", totalAmount);
                intent.putExtra("status", status);
                startActivity(intent);
            }
        });


        return view;
    }
}