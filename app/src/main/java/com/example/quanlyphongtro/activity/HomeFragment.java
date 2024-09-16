package com.example.quanlyphongtro.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyphongtro.model.ServicePOJO;
import com.example.quanlyphongtro.R;
import com.example.quanlyphongtro.adapter.ItemServiceAdapter;
import com.example.quanlyphongtro.database.QuanLyPhongTroDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private RecyclerView rcv_services;
    FloatingActionButton fab_add;
    private ItemServiceAdapter itemServiceAdapter;
    private List<ServicePOJO> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Toolbar toolbar = view.findViewById(R.id.app_bar_home);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Bật tùy chọn menu cho Fragment
        setHasOptionsMenu(true);

        rcv_services = view.findViewById(R.id.rcv_services);
        fab_add = view.findViewById(R.id.fab_add);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcv_services.setLayoutManager(linearLayoutManager);
        itemServiceAdapter = new ItemServiceAdapter(getContext());


        list = new ArrayList<>();
        list = QuanLyPhongTroDB.getInstance(getContext()).serviceDAO().getListService();
        itemServiceAdapter.setData(list);
        rcv_services.setAdapter(itemServiceAdapter);
        return view;


    }
}