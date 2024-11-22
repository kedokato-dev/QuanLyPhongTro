package com.example.quanlyphongtro.activity;

import static android.view.View.*;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.quanlyphongtro.R;
import com.example.quanlyphongtro.adapter.ItemBillAdapter;
import com.example.quanlyphongtro.database.BillDAO;
import com.example.quanlyphongtro.database.QuanLyPhongTroDB;
import com.example.quanlyphongtro.pojo.ItemBillPOJO;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class StatisticsActivity extends AppCompatActivity {

    private TextView tvMonthYear, tvTotalRevenue;
    private RecyclerView rcv_item_bill;
    private ItemBillAdapter itemBillAdapter;
    private Button btnSelectMonth;
    private List<ItemBillPOJO> itemBillPOJOList;

    private String selectedMonthYear; // Lưu tháng/năm được chọn
    private QuanLyPhongTroDB database;
    private BillDAO billDAO;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_statistics);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Thiết lập các View khác
        loadView();
        loadToolBar();

        itemBillPOJOList = new ArrayList<>();
        database = QuanLyPhongTroDB.getInstance(this);

        // Khởi tạo RecyclerView
        rcv_item_bill = findViewById(R.id.rcv_item_bill);

        // Kiểm tra xem RecyclerView có null không
        if (rcv_item_bill == null) {
            Log.e("StatisticsActivity", "RecyclerView not found");
            return; // Hoặc xử lý lỗi ở đây
        }

        // Thiết lập LayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcv_item_bill.setLayoutManager(linearLayoutManager);

        // Khởi tạo adapter và gán dữ liệu
        itemBillAdapter = new ItemBillAdapter(this);
        itemBillPOJOList = database.billDAO().getListItemBill(); // Lấy dữ liệu từ database

        // Kiểm tra dữ liệu
        if (itemBillPOJOList == null || itemBillPOJOList.isEmpty()) {
            Log.w("StatisticsActivity", "No bills data available.");
        }

//        itemBillAdapter.setData(itemBillPOJOList);
//        rcv_item_bill.setAdapter(itemBillAdapter); // Gán adapter cho RecyclerView

        // Hiển thị hóa đơn tháng hiện tại
        loadData(itemBillPOJOList);

        // Xử lý chọn tháng
        btnSelectMonth.setOnClickListener(v -> openMonthPicker());

    }

    private void loadView(){
        tvMonthYear = findViewById(R.id.tvMonthYear);
        tvTotalRevenue = findViewById(R.id.tvTotalRevenue);
        rcv_item_bill = findViewById(R.id.rcv_item_bill);
        btnSelectMonth = findViewById(R.id.btnSelectMonth);

    }

    private void loadToolBar(){
        Toolbar toolbar = findViewById(R.id.app_bar_statistics);
        setSupportActionBar(toolbar);

        // Hiển thị nút back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Xóa title mặc định
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Đổi icon nút back
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left); // Thay icon
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Kết thúc Activity và quay lại Activity trước đó
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openMonthPicker() {
        // Lấy ngày hiện tại
        final java.util.Calendar calendar = java.util.Calendar.getInstance();
        int year = calendar.get(java.util.Calendar.YEAR);
        int month = calendar.get(java.util.Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                StatisticsActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Định dạng ngày sau khi chọn
                        String selectedDate =  (month + 1) + "/" + year;
                        tvMonthYear.setText(selectedDate);
                        String month_bill = String.valueOf(month+1);
                        if(Integer.parseInt(month_bill) < 10){
                            month_bill = '0'+month_bill;
                        }


//                        Toast.makeText(StatisticsActivity.this, year+"-"+month_bill, Toast.LENGTH_SHORT).show();
                        loadBills(year+"-"+month_bill);
                    }
                }, year, month, day);

        // Hiển thị DatePickerDialog
        datePickerDialog.show();
    }


    @SuppressLint("SetTextI18n")
    private void loadBills(String monthYear) {

            List<ItemBillPOJO> bills = database.billDAO().getBillsByMonth(monthYear);
            Double totalRevenue = database.billDAO().getTotalRevenueByMonth(monthYear);
                // Cập nhật RecyclerView
              loadData(bills);

                // Hiển thị tổng doanh thu
                tvTotalRevenue.setText("Tổng doanh thu: " + (totalRevenue != null ? convertDoubleToVND(totalRevenue) : 0) + " VNĐ");


    }

    private void loadData(List<ItemBillPOJO> list){
        itemBillAdapter.setData(list);
        rcv_item_bill.setAdapter(itemBillAdapter);
    }

    private String convertDoubleToVND(double number) {
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        return numberFormat.format(number); // Thêm " VND" để rõ ràng hơn
    }




}