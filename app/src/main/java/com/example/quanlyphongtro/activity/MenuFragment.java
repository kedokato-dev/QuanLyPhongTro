package com.example.quanlyphongtro.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quanlyphongtro.Model.MenuItem;
import com.example.quanlyphongtro.R;
import com.example.quanlyphongtro.adapter.ItemMenuAdapter;

import java.util.ArrayList;
import java.util.List;


public class MenuFragment extends Fragment {

    private RecyclerView rcv_item;
    private ItemMenuAdapter itemMenuAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        rcv_item = view.findViewById(R.id.rcv_item);
        itemMenuAdapter = new ItemMenuAdapter(getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcv_item.setLayoutManager(linearLayoutManager);
        itemMenuAdapter.setData(getListMenu());
        rcv_item.setAdapter(itemMenuAdapter);
        return view;
    }

    private List<MenuItem> getListMenu() {
            List<MenuItem> list = new ArrayList<>();
            list.add(new MenuItem(R.drawable.import_file, "Nhập dữ liệu", "Nhập dữ liệu từ file csv"));
            list.add(new MenuItem(R.drawable.export_flie, "Xuất dữ liệu", "Xuất dữ liệu ra file csv"));

            return list;
        }

    }
