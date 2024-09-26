package com.example.quanlyphongtro.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyphongtro.R;
import com.example.quanlyphongtro.adapter.ItemBillAdapter;
import com.example.quanlyphongtro.database.QuanLyPhongTroDB;
import com.example.quanlyphongtro.pojo.ItemBillPOJO;

import java.util.ArrayList;
import java.util.List;


public class BillFragment extends Fragment {
    private RecyclerView rcv_item_bill;
    private List<ItemBillPOJO> itemBillPOJOList;
    private QuanLyPhongTroDB database;
    private ItemBillAdapter itemBillAdapter;
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true); // Để thông báo rằng Fragment này có Options Menu
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_filter, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            openFilterDialog(Gravity.CENTER);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill, container, false);

        toolbar = view.findViewById(R.id.app_bar_bill_list);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        // Tắt tittle mặc định cho toolbar
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        rcv_item_bill = view.findViewById(R.id.rcv_item_bill);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcv_item_bill.setLayoutManager(linearLayoutManager);


        itemBillAdapter = new ItemBillAdapter(getContext());

        database = QuanLyPhongTroDB.getInstance(getContext());
//        itemBillPOJOList = database.billDAO().getListItemBill();
//        itemBillAdapter.setData(itemBillPOJOList);
        loadData();
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

    private void openFilterDialog(int gravity) {
        Dialog dialog = createDialog(R.layout.layout_dialog_filter, gravity);
        if (dialog == null) {
            return;
        }

        RadioButton rbPaid = dialog.findViewById(R.id.rb_paid);
        RadioButton rbUnpaid = dialog.findViewById(R.id.rb_unpaid);
        RadioButton rbAll = dialog.findViewById(R.id.rb_all);

        // lấy ra sharePreferences
        sharedPreferences = getContext().getSharedPreferences("FilterPreferences", getContext().MODE_PRIVATE);
        String filterOption = sharedPreferences.getString("filterOption", "all"); // Giá trị mặc định là "all"

        // Thiết lập RadioButton dựa trên giá trị lưu trữ
        if (filterOption.equals("paid")) {
            rbPaid.setChecked(true);
        } else if (filterOption.equals("unpaid")) {
            rbUnpaid.setChecked(true);
        } else {
            rbAll.setChecked(true);
        }

        Button btnConfirm = dialog.findViewById(R.id.btn_confirm);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit(); // Để chỉnh sửa SharedPreferences

                if (rbAll.isChecked()) {
                    itemBillPOJOList.clear();
                    itemBillPOJOList = database.billDAO().getListItemBill();
                    itemBillAdapter.setData(itemBillPOJOList);
                    editor.putString("filterOption", "all"); // Lưu lựa chọn "all"
                }

                if (rbPaid.isChecked()) {
                    itemBillPOJOList.clear();
                    itemBillPOJOList = database.billDAO().getListItemBillPaid();
                    itemBillAdapter.setData(itemBillPOJOList);
                    editor.putString("filterOption", "paid"); // Lưu lựa chọn "paid"
                }

                if (rbUnpaid.isChecked()) {
                    itemBillPOJOList.clear();
                    itemBillPOJOList = database.billDAO().getListItemBillUnpaid();
                    itemBillAdapter.setData(itemBillPOJOList);
                    editor.putString("filterOption", "unpaid"); // Lưu lựa chọn "unpaid"
                }

                editor.apply(); // Lưu thay đổi
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void loadData() {
        itemBillPOJOList = new ArrayList<>();
        // Lấy lựa chọn đã lưu trong SharedPreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("FilterPreferences", getContext().MODE_PRIVATE);
        String filterOption = sharedPreferences.getString("filterOption", "all"); // Giá trị mặc định là "all"

        // Kiểm tra lựa chọn đã lưu và hiển thị dữ liệu phù hợp
        if (filterOption.equals("paid")) {
            itemBillPOJOList = database.billDAO().getListItemBillPaid();
            itemBillAdapter.setData(itemBillPOJOList);
        } else if (filterOption.equals("unpaid")) {
            itemBillPOJOList = database.billDAO().getListItemBillUnpaid();
            itemBillAdapter.setData(itemBillPOJOList);
        } else if (filterOption.equals("all")) {
            itemBillPOJOList = database.billDAO().getListItemBill();
            itemBillAdapter.setData(itemBillPOJOList);
        } else {
            itemBillPOJOList = database.billDAO().getListItemBill();
            itemBillAdapter.setData(itemBillPOJOList);
        }
    }
}