package com.example.quanlyphongtro.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlyphongtro.R;
import com.example.quanlyphongtro.database.QuanLyPhongTroDB;
import com.example.quanlyphongtro.database.Room_TenantDAO;
import com.example.quanlyphongtro.model.Room;
import com.example.quanlyphongtro.model.Room_Tenant;
import com.example.quanlyphongtro.model.Tenant;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

public class AddTenantToRoomActivity extends AppCompatActivity {
    private Spinner spinnerRooms, spinnerTenants;
    private Button btnAddTenantToRoom;
    private QuanLyPhongTroDB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_tenant_to_room);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadToolBar();
        loadView();

        // Lấy danh sách phòng và người thuê từ database
        loadRooms();
        loadTenants();

        // Xử lý sự kiện nút thêm
        btnAddTenantToRoom.setOnClickListener(view -> {
            Room selectedRoom = (Room) spinnerRooms.getSelectedItem();
            Tenant selectedTenant = (Tenant) spinnerTenants.getSelectedItem();

            if (selectedRoom != null && selectedTenant != null) {
                addTenantToRoom(selectedRoom.getRoomId(), selectedTenant.getTenantId());
            } else {
                Toast.makeText(this, "Vui lòng chọn đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadToolBar() {
        Toolbar toolbar = findViewById(R.id.app_bar_add_user_to_bill);
        setSupportActionBar(toolbar);

        // Hiển thị nút back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Xóa title mặc định
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Đổi icon nút back
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left); // Thay icon
    }

    private void loadView() {
        // Ánh xạ giao diện
        spinnerRooms = findViewById(R.id.spinnerRooms);
        spinnerTenants = findViewById(R.id.spinnerTenants);
        btnAddTenantToRoom = findViewById(R.id.btnAddTenantToRoom);
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


    private void loadRooms() {
        database = QuanLyPhongTroDB.getInstance(this);
        List<Room> rooms = database.roomDAO().getAvailableRooms();

        ArrayAdapter<Room> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, rooms);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRooms.setAdapter(adapter);
    }

    // Load danh sách người thuê từ database
    private void loadTenants() {
        database = QuanLyPhongTroDB.getInstance(this);
        List<Tenant> tenants = database.userDAO().getAllUser();

        ArrayAdapter<Tenant> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenants);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTenants.setAdapter(adapter);
    }

    public boolean canAddTenantToRoom(int roomId, int tenantId, Room_TenantDAO roomTenantDAO) {
        // Kiểm tra thành viên đã ở trong phòng hay chưa
        int isMemberExists = roomTenantDAO.checkMemberExistsInRoom(roomId, tenantId);
        if (isMemberExists > 0) {
            Toast.makeText(this, "Thành viên này đã ở trong phòng!", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Lấy số lượng người hiện tại trong phòng
        int currentTenantCount = roomTenantDAO.getCurrentTenantCount(roomId);

        // Lấy sức chứa tối đa của phòng
        int roomCapacity = roomTenantDAO.getRoomMaxOccupants(roomId);

        if (currentTenantCount >= roomCapacity) {
            Toast.makeText(this, "Phòng đã đạt số lượng tối đa ->" +currentTenantCount + "/"  + roomCapacity, Toast.LENGTH_SHORT).show();
            return false;
        }

        // Nếu không có vấn đề gì thì có thể thêm
        return true;
    }

    public void checkStatusRoom(Room_TenantDAO roomTenantDAO, int roomId){
        // Lấy số lượng người hiện tại trong phòng
        int currentTenantCount = roomTenantDAO.getCurrentTenantCount(roomId);
        // Lấy sức chứa tối đa của phòng
        int roomCapacity = roomTenantDAO.getRoomMaxOccupants(roomId);

        if(currentTenantCount == 0 ){
            database.roomDAO().updateStatusRoom("Còn trống", roomId);
        }else if (currentTenantCount > 0 && currentTenantCount < roomCapacity){
            database.roomDAO().updateStatusRoom("Đã có người thuê", roomId);
        } else {
            database.roomDAO().updateStatusRoom("Đã đủ người", roomId);
        }

    }


    // Thêm người thuê vào phòng
    private void addTenantToRoom(int roomId, int tenantId) {
        LocalDate currentDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
        }

        Room_TenantDAO roomTenantDAO = database.roomTenantDAO();
        
        if (canAddTenantToRoom(roomId, tenantId, roomTenantDAO)) {
            assert currentDate != null;
            roomTenantDAO.insertRoomTenant(roomId, tenantId, currentDate.toString(), "NULL");
            Toast.makeText(this, "Thêm thành viên vào phòng thành công!", Toast.LENGTH_SHORT).show();
            checkStatusRoom(roomTenantDAO, roomId);
        } else {
            Toast.makeText(this, "Không thể thêm thành viên vào phòng.", Toast.LENGTH_SHORT).show();
        }
        finish(); // Đóng activity sau khi thêm xong
    }
}