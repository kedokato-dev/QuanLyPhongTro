package com.example.quanlyphongtro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyphongtro.pojo.MenuItem;
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

        Toolbar toolbar = view.findViewById(R.id.app_bar_menu);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Bật tùy chọn menu cho Fragment
        setHasOptionsMenu(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcv_item.setLayoutManager(linearLayoutManager);
        itemMenuAdapter.setData(getListMenu());
        rcv_item.setAdapter(itemMenuAdapter);

        itemMenuAdapter.setOnItemClickListener(new ItemMenuAdapter.OnItemClickGetUserListListener() {
            @Override
            public void onItemClick(int position) {
                if(position == 2){
                    Intent intent = new Intent(getContext(), UserListActivity.class);
                    startActivity(intent);
                }else if(position == 0){
                    Intent intent = new Intent(getContext(), ImportDataActivity.class);
                    startActivity(intent);
                }else if(position == 1){
                    Intent intent = new Intent(getContext(), ExportDataActivity.class);
                    startActivity(intent);
                }

            }
        });

        return view;
    }

    private List<MenuItem> getListMenu() {
            List<MenuItem> list = new ArrayList<>();
            list.add(new MenuItem(R.drawable.ic_import_file, "Nhập dữ liệu", "Nhập dữ liệu từ file csv"));
            list.add(new MenuItem(R.drawable.ic_export_flie, "Xuất dữ liệu", "Xuất dữ liệu ra file csv"));
            list.add(new MenuItem(R.drawable.ic_users, "Thành viên", "Quản lý thành viên trong khu trọ"));
            list.add(new MenuItem(R.drawable.ic_dark, "Dark mode", "Chế độ tối của ứng dụng"));
            return list;
        }

    }
