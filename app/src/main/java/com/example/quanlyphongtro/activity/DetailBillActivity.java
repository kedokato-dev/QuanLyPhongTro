package com.example.quanlyphongtro.activity;

import android.icu.text.NumberFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyphongtro.R;
import com.example.quanlyphongtro.adapter.ItemServiceBillAdapter;
import com.example.quanlyphongtro.database.QuanLyPhongTroDB;
import com.example.quanlyphongtro.pojo.ItemBillPOJO;
import com.example.quanlyphongtro.pojo.RoomNumber_RoomTypeName;
import com.example.quanlyphongtro.pojo.ServiceInBillPOJO;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetailBillActivity extends AppCompatActivity {
    private String roomNumerValue;
    private Double getTotalAmount;
    private String getStatus;
    private RecyclerView rcv_item_service_in_bill;
    private QuanLyPhongTroDB database;
    private ItemServiceBillAdapter itemServiceBillAdapter;
    private List<ServiceInBillPOJO> listService;
    private List<ItemBillPOJO> inforRoomBill;
    private List<RoomNumber_RoomTypeName> roomNumber_roomTypeNames;

    private TextView roomNumber;
    private TextView roomTypeName;
    private TextView memberName;
    private TextView issueDate;
    private TextView roomPrice;
    private TextView totalAmountService;
    private TextView totalAmount;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_detail_bill);

        roomNumerValue = getIntent().getStringExtra("roomNumber");
        getTotalAmount = getIntent().getDoubleExtra("totalAmount", 999999);
        getStatus = getIntent().getStringExtra("status");
        loadToolBar();

        rcv_item_service_in_bill = findViewById(R.id.rcv_item_service);
        listService = new ArrayList<>();
        inforRoomBill = new ArrayList<>();
        itemServiceBillAdapter = new ItemServiceBillAdapter(this);
        database = QuanLyPhongTroDB.getInstance(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        rcv_item_service_in_bill.setLayoutManager(linearLayoutManager);

        listService = database.serviceDAO().getListServiceInBill(roomNumerValue);
        inforRoomBill = database.billDAO().inforRoomBill(roomNumerValue);
        roomNumber_roomTypeNames = database.roomDAO().getRoomNumber_RoomTypeName(roomNumerValue);

        itemServiceBillAdapter.setData(listService);
        rcv_item_service_in_bill.setAdapter(itemServiceBillAdapter);

        loadView();
        loadDetailBill();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void loadToolBar() {
        Toolbar toolbar = findViewById(R.id.app_bar_detail_bill);
        setSupportActionBar(toolbar);

        // Hiển thị nút back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Xóa title mặc định
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Đổi icon nút back
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left); // Thay icon
    }

    // Xử lý sự kiện khi người dùng nhấn nút back trên toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Kết thúc Activity và quay lại Activity trước đó
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadView() {
        roomNumber = findViewById(R.id.tv_room_number_bill_detail);
        roomTypeName = findViewById(R.id.tv_room_type_bill_detail);
        memberName = findViewById(R.id.tv_member_name_detail_bill);
        issueDate = findViewById(R.id.tv_issue_date_bill_detail);
        roomPrice = findViewById(R.id.tv_room_price_bill_detail);
        totalAmountService = findViewById(R.id.tv_total_service_amonut);
        totalAmount = findViewById(R.id.tv_total_amount);
        status = findViewById(R.id.tv_status_bill_detail);


    }

    private void loadDetailBill() {
        roomNumber.setText(inforRoomBill.get(0).getRoomNumber());
        roomTypeName.setText(roomNumber_roomTypeNames.get(0).getTypeName());


        issueDate.setText(inforRoomBill.get(0).getIssueDate());
        String strIssueDate = issueDate.getText().toString();
        String date = "";
        try {
            date = formatDate(strIssueDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedRoomPrice = numberFormat.format(roomNumber_roomTypeNames.get(0).getPrice());
        String formattedTotalServiceAmount = numberFormat.format(database.billDAO().totalServiceAmount(roomNumerValue));
        String formattedTotalAmount = numberFormat.format(getTotalAmount);
        roomPrice.setText(formattedRoomPrice);
        totalAmountService.setText(formattedTotalServiceAmount);
        totalAmount.setText(formattedTotalAmount);
        status.setText(getStatus);


        int id = database.billDAO().getBillByIssueDateAndTotal(date, getTotalAmount);

        String strMemberName = database.billDAO().getNamePaidInBillByIdAndIssueDate(id, date);

        memberName.setText(strMemberName);

        if ("Chưa thanh toán".equals(getStatus)) {
            status.setTextColor(this.getResources().getColor(R.color.red));
        } else if ("Đã thanh toán".equals(getStatus)) {
            status.setTextColor(this.getResources().getColor(R.color.green));
        }
    }

    private String formatDate(String input) throws ParseException {
        String dateAfterFormated = "";
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = inputFormat.parse(input);
        return dateAfterFormated = outputFormat.format(date);

    }
}