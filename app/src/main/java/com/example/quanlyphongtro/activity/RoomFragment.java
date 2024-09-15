package com.example.quanlyphongtro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyphongtro.Model.RoomWithTenantInfo;
import com.example.quanlyphongtro.R;
import com.example.quanlyphongtro.adapter.ItemRoomAdapter;
import com.example.quanlyphongtro.database.QuanLyPhongTroDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class RoomFragment extends Fragment {
    private RecyclerView rcv;
    private ItemRoomAdapter itemRoomAdapter;
    private QuanLyPhongTroDB database;
    private FloatingActionButton fab;
    private List<RoomWithTenantInfo> infoList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room, container, false);

//        Toolbar toolbar = view.findViewById(R.id.app_bar_room);
        fab = view.findViewById(R.id.fab_add);

        // Khởi tạo database
        database = QuanLyPhongTroDB.getInstance(getContext());
        infoList = new ArrayList<>();

        // Khởi tạo RecyclerView và adapter
        rcv = view.findViewById(R.id.rcv_item);
        itemRoomAdapter = new ItemRoomAdapter(getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcv.setLayoutManager(linearLayoutManager);

        infoList = database.roomDAO().getRoomWithTenantInfo();
        itemRoomAdapter.setData(infoList);
        rcv.setAdapter(itemRoomAdapter);




        itemRoomAdapter.setOnItemClickListener(new ItemRoomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Lấy roomNumber từ item clicked
                String roomNumber = infoList.get(position).getRoomNumber();

                // Tạo Intent để truyền dữ liệu
                Intent intent = new Intent(getContext(), DetailRoomActivity.class);
                intent.putExtra("roomNumber", roomNumber);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Đảm bảo hiển thị FAB khi RoomFragment được hiển thị lại
        if (fab != null) {
            fab.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Hiển thị lại FAB khi quay lại fragment
        if (fab != null) {
            fab.show();
        }
    }
}

