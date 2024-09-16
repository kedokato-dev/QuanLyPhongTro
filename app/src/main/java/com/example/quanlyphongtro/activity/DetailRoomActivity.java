package com.example.quanlyphongtro.activity;

import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyphongtro.model.DetailRoomPOJO;
import com.example.quanlyphongtro.model.UserDetailPOJO;
import com.example.quanlyphongtro.R;
import com.example.quanlyphongtro.adapter.ItemUserRoomAdapter;
import com.example.quanlyphongtro.database.QuanLyPhongTroDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DetailRoomActivity extends AppCompatActivity {
    private TextView roomNumber;
    private TextView roomStatus;
    private TextView price;
    private TextView roomType;
    private TextView description;

    private RecyclerView rcv_item_userRoom;
    private List<DetailRoomPOJO> detailRoom;
    private QuanLyPhongTroDB database;
    private String roomNumberValue;
    private ItemUserRoomAdapter itemUserRoomAdapter;
    private List<UserDetailPOJO> userDetailPOJOList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_room);
        loadToolBar();

        loadView();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcv_item_userRoom.setLayoutManager(linearLayoutManager);
        itemUserRoomAdapter = new ItemUserRoomAdapter(this);

         roomNumberValue = getIntent().getStringExtra("roomNumber");

        detailRoom = new ArrayList<>();
        userDetailPOJOList = new ArrayList<>();

        database = QuanLyPhongTroDB.getInstance(this);

        detailRoom = database.roomDAO().getDetailRoomPOJO(roomNumberValue);
        userDetailPOJOList = database.roomDAO().getUserDetailRoom(roomNumberValue);

        itemUserRoomAdapter.setData(userDetailPOJOList);
        rcv_item_userRoom.setAdapter(itemUserRoomAdapter);

        loadDataDetailRoom();

    }

    private void loadDataDetailRoom() {
        if (detailRoom != null && !detailRoom.isEmpty()) {
            // Đảm bảo có ít nhất một phần tử trong danh sách
            if (detailRoom.size() > 0) {
                // Lấy dữ liệu từ phần tử đầu tiên
                DetailRoomPOJO roomData = detailRoom.get(0);

                // Cập nhật giao diện với dữ liệu phòng
                roomNumber.setText(roomData.getRoomNumber());
                roomStatus.setText(roomData.getStatus());

                // Cập nhật màu sắc trạng thái phòng
                if ("Đã đủ người".equals(roomData.getStatus())) {
                    roomStatus.setTextColor(this.getResources().getColor(R.color.red));
                } else if ("Đã có người thuê".equals(roomData.getStatus())) {
                    roomStatus.setTextColor(this.getResources().getColor(R.color.orange));
                } else {
                    roomStatus.setTextColor(this.getResources().getColor(R.color.green));
                }

                roomType.setText(roomData.getTypeName());

                // Định dạng giá phòng
                NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
                String formattedPrice = numberFormat.format(roomData.getPrice());
                price.setText(formattedPrice);

                description.setText(roomData.getDescription());
            } else {
                // Nếu danh sách rỗng, hiển thị thông báo cho người dùng
                Toast.makeText(this, "Không có dữ liệu phòng.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Nếu danh sách null hoặc rỗng, hiển thị thông báo cho người dùng
            Toast.makeText(this, "Phòng này đang trống", Toast.LENGTH_SHORT).show();
            roomNumber.setText(roomNumberValue);
            roomStatus.setText("Còn trống");
            roomStatus.setTextColor(this.getResources().getColor(R.color.green));
        }
    }

    private void loadToolBar(){
        Toolbar toolbar = findViewById(R.id.app_bar_detail_room);
        setSupportActionBar(toolbar);

        // Hiển thị nút back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Xóa title mặc định
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Đổi icon nút back
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left); // Thay icon
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
        roomNumber = findViewById(R.id.room_number);
        roomType = findViewById(R.id.room_type);
        roomStatus = findViewById(R.id.room_status);
        price = findViewById(R.id.room_price);

        description = findViewById(R.id.room_description);
        rcv_item_userRoom = findViewById(R.id.members_recycler_view);
    }
}
