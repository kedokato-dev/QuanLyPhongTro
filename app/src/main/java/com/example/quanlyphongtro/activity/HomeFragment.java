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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

        openDialogDelete(Gravity.CENTER);
        openDialogUpdate(Gravity.CENTER);

        rcv_services.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    fab_add.hide();
                } else {
                    fab_add.show();
                }
            }
        });
        return rootView;
    }


    private Dialog createDialog(int layoutResId, int gravity) {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutResId);

        Window window = dialog.getWindow();
        if (window == null) {
            return null;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        dialog.setCancelable(Gravity.CENTER == gravity);

        return dialog;
    }


    private void openAddDialog(int gravity) {
        Dialog dialog = createDialog(R.layout.layout_dialog_service, gravity);
        if (dialog == null) return;

        EditText serviceName = dialog.findViewById(R.id.edt_service_name);
        EditText servicePrice = dialog.findViewById(R.id.edt_service_price);

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

                if (strServicePrice.isEmpty() || strServiceName.isEmpty()) {
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

    private void openDialogDelete(int gravity) {
        itemServiceAdapter.setOnDeleteClickListener(new ItemServiceAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick (int position) {
                Dialog dialog = createDialog(R.layout.layout_dialog_delete, gravity);
                if (dialog == null) return;

                TextView tvDeleteName = dialog.findViewById(R.id.tv_delete_name);
                TextView tvSubDeleteName = dialog.findViewById(R.id.tv_sub_delete_name);

                Button btn_delete = dialog.findViewById(R.id.btn_delete_dialog);
                Button btn_cancel = dialog.findViewById(R.id.btn_cancel);

                tvDeleteName.setText("Chú ý");

                tvSubDeleteName.setText(list.get(position).getServiceName());

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (position >= 0 && position < list.size()) {
                            ServicePOJO servicePOJO = list.get(position);

                            // Tìm dịch vụ từ cơ sở dữ liệu
                            Service service = database.serviceDAO().getServiceByNameAndPrice(servicePOJO.getServiceName(), servicePOJO.getPricePerUnit());
                            if (service != null) {
                                // Xóa dịch vụ từ cơ sở dữ liệu
                                database.serviceDAO().delete(service);

                                Toast.makeText(getContext(), "Đã xóa thành công dịch vụ " + service.getServiceName(), Toast.LENGTH_SHORT).show();

                                // Cập nhật dữ liệu trong RecyclerView
                                itemServiceAdapter.removeItem(position);
                            }
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void openDialogUpdate(int gravity) {
        itemServiceAdapter.setOnUpdateClickListener(new ItemServiceAdapter.OnUpdateClickListener() {
            @Override
            public void onUpdateClick(int position) {
                Dialog dialog = createDialog(R.layout.layout_dialog_service, gravity);
                if (dialog == null) return;

                TextView tvDialogName = dialog.findViewById(R.id.tv_dialog_name);
                TextView tvSubDialogName = dialog.findViewById(R.id.tv_sub_name_dialog);

                EditText serviceName = dialog.findViewById(R.id.edt_service_name);
                EditText servicePrice = dialog.findViewById(R.id.edt_service_price);

                Button btn_update = dialog.findViewById(R.id.btn_add);
                Button btn_cancel = dialog.findViewById(R.id.btn_cancel);

                tvDialogName.setText("Chỉnh sửa thông tin dịch vụ");
                tvSubDialogName.setText("Hãy nhập thông tin dịch vụ bạn muốn sửa");

                String strServiceName = list.get(position).getServiceName();
                String strServicePrice = String.valueOf(list.get(position).getPricePerUnit());


                serviceName.setText(strServiceName);
                servicePrice.setText(strServicePrice);

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                // lấy ra trường dữ liệu muốn cập nhât
                service = database.serviceDAO().getServiceByNameAndPrice(strServiceName, Double.parseDouble(strServicePrice));

                // lấy ra giá trị id của trường dữ liệu muốn cập nhật
                int id = 0;
                id = service.getServiceId();

                int finalId = id;
                btn_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String strServiceName = serviceName.getText().toString().trim();
                        String strServicePrice = servicePrice.getText().toString().trim();

                        if (strServicePrice.isEmpty() || strServiceName.isEmpty()) {
                            return;
                        }

                        Double dServicePrice = Double.parseDouble(strServicePrice);
                        service = new Service(finalId, strServiceName, dServicePrice);



                        database.serviceDAO().update(service);

                        loadData();
                        Toast.makeText(getContext(), "Đã thêm thành công dịch vụ " + strServiceName, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }


    private void loadData() {
        list.clear();
        list.addAll(database.serviceDAO().getListService());
        itemServiceAdapter.setData(list);
    }

}