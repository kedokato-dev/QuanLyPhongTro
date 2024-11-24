package com.example.quanlyphongtro.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyphongtro.R;
import com.example.quanlyphongtro.activity.AddTenantToRoomActivity;
import com.example.quanlyphongtro.activity.ExportDataActivity;
import com.example.quanlyphongtro.activity.ImportDataActivity;
import com.example.quanlyphongtro.activity.StatisticsActivity;
import com.example.quanlyphongtro.activity.UserListActivity;
import com.example.quanlyphongtro.adapter.ItemMenuAdapter;
import com.example.quanlyphongtro.pojo.MenuItem;

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
                if (position == 2) {
                    Intent intent = new Intent(getContext(), UserListActivity.class);
                    startActivity(intent);
                } else if (position == 0) {
                    Intent intent = new Intent(getContext(), ImportDataActivity.class);
                    startActivity(intent);
                } else if (position == 1) {
                    Intent intent = new Intent(getContext(), ExportDataActivity.class);
                    startActivity(intent);
                } else if (position == 4) {
                    openAboutDialog(Gravity.CENTER);
                } else if (position == 3){
//                    Intent intent = new Intent(getContext(), AddTenantToRoomActivity.class);
//                    startActivity(intent);
                    Intent intent = new Intent(getActivity(), AddTenantToRoomActivity.class);
                    startActivityForResult(intent, 1);

                } else if(position == 5){
                    Intent intent = new Intent(getContext(), StatisticsActivity.class);
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
        list.add(new MenuItem(R.drawable.people_arrows_solid, "Điều chỉnh thành viên phòng", "Thêm, gỡ thành viên ra khỏi phòng"));
        list.add(new MenuItem(R.drawable.circle_info_solid, "Giới thiệu về ứng dụng", "Quản lý phòng trọ"));
        list.add(new MenuItem(R.drawable.chart_simple_solid, "Thống kê doanh thu", "Xem doanh thu theo tháng"));
        return list;
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

    private void openAboutDialog(int gravity){
        Dialog dialog = createDialog(R.layout.layout_dialog_about,gravity);
        if (dialog == null){
            return;
        }


        ImageView iv_close = dialog.findViewById(R.id.iv_close);
        TextView tv_hdsdLink = dialog.findViewById(R.id.tv_hdsd_link);
        dialog.show();

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tv_hdsdLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=eaHq3BCH2qU"));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            String result = data.getStringExtra("result_key");
            // Xử lý kết quả
        }
    }


}
