package com.example.quanlyphongtro.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyphongtro.R;
import com.example.quanlyphongtro.adapter.ItemServiceAdapter;
import com.example.quanlyphongtro.database.QuanLyPhongTroDB;
import com.example.quanlyphongtro.model.Service;
import com.example.quanlyphongtro.pojo.ServicePOJO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private RecyclerView rcv_services;
    private FloatingActionButton fab_add;
    private ItemServiceAdapter itemServiceAdapter;
    private QuanLyPhongTroDB database;
    private List<ServicePOJO> list;
    private Service service;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        Toolbar toolbar = rootView.findViewById(R.id.app_bar_home);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Bật tùy chọn menu cho Fragment
        setHasOptionsMenu(true);

        rcv_services = rootView.findViewById(R.id.rcv_services);
        fab_add = rootView.findViewById(R.id.fab_add_service);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcv_services.setLayoutManager(linearLayoutManager);
        itemServiceAdapter = new ItemServiceAdapter(getContext());


        list = new ArrayList<>();
        database = QuanLyPhongTroDB.getInstance(getContext());

        list = database.serviceDAO().getListService();
        itemServiceAdapter.setData(list);
        rcv_services.setAdapter(itemServiceAdapter);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddDialog(Gravity.CENTER);
            }
        });

        deleteService();



        return rootView;
    }



    private void openAddDialog(int gravity){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_service);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // xử lý xác định vị trí của 1 dialog
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if(Gravity.CENTER == gravity){
            dialog.setCancelable(true);
        }else{
            dialog.setCancelable(false);
        }

        EditText serviceName  = dialog.findViewById(R.id.edt_service_name);
        EditText servicePrice  = dialog.findViewById(R.id.edt_service_price);

        Button btn_add = dialog.findViewById(R.id.btn_add);
        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strServiceName = serviceName.getText().toString().trim();
                String strServicePrice = servicePrice.getText().toString().trim();

                if(strServicePrice.isEmpty() || strServiceName.isEmpty()){
                    return;
                }

                Double dServicePrice = Double.parseDouble(strServicePrice);

                service = new Service(strServiceName, dServicePrice);

                database.serviceDAO().insertService(service);

                loadData();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void deleteService() {
        itemServiceAdapter.setOnClickListener(new ItemServiceAdapter.OnClickItemListener() {
            @Override
            public void onItemClick(int position) {
                if (position >= 0 && position < list.size()) {
                    ServicePOJO servicePOJO = list.get(position);

                    // Tìm dịch vụ từ cơ sở dữ liệu
                    Service service = database.serviceDAO().getServiceByNameAndPrice(servicePOJO.getServiceName(), servicePOJO.getPricePerUnit());
                    if (service != null) {
                        // Xóa dịch vụ từ cơ sở dữ liệu
                        database.serviceDAO().delete(service);

                        // Cập nhật dữ liệu trong RecyclerView
                        itemServiceAdapter.removeItem(position);
                    }
                }
            }
        });
    }

    private void loadData() {
        list.clear();
        list.addAll(database.serviceDAO().getListService());
        itemServiceAdapter.setData(list);
    }


}