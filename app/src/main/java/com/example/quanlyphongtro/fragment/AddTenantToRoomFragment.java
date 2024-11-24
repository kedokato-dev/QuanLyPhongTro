package com.example.quanlyphongtro.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quanlyphongtro.R;
import com.example.quanlyphongtro.database.QuanLyPhongTroDB;
import com.example.quanlyphongtro.database.Room_TenantDAO;
import com.example.quanlyphongtro.model.Room;
import com.example.quanlyphongtro.model.Tenant;

import java.time.LocalDate;
import java.util.List;

public class AddTenantToRoomFragment extends Fragment {
    private Spinner spinnerRooms, spinnerTenants;
    private Button btnAddTenantToRoom, btnRemoveTenantToRoom;
    private QuanLyPhongTroDB database;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmement_add_tenant_to_room, container, false);

        spinnerRooms = view.findViewById(R.id.spinnerRooms);
        spinnerTenants = view.findViewById(R.id.spinnerTenants);
        btnAddTenantToRoom = view.findViewById(R.id.btnAddTenantToRoom);


        loadRooms();
        loadTenants();

        // Xử lý sự kiện nút thêm
        btnAddTenantToRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Room selectedRoom = (Room) spinnerRooms.getSelectedItem();
                Tenant selectedTenant = (Tenant) spinnerTenants.getSelectedItem();

                if (selectedRoom != null && selectedTenant != null) {
                    addTenantToRoom(selectedRoom.getRoomId(), selectedTenant.getTenantId());
                } else {
                    Toast.makeText(getContext(), "Vui lòng chọn đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }


    private void loadRooms() {
        database = QuanLyPhongTroDB.getInstance(getContext());
        List<Room> rooms = database.roomDAO().getAvailableRooms();

        ArrayAdapter<Room> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, rooms);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRooms.setAdapter(adapter);
    }

    // Load danh sách người thuê từ database
    private void loadTenants() {
        database = QuanLyPhongTroDB.getInstance(getContext());
        List<Tenant> tenants = database.userDAO().getAllUser();

        ArrayAdapter<Tenant> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tenants);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTenants.setAdapter(adapter);
    }

    public boolean canAddTenantToRoom(int roomId, int tenantId, Room_TenantDAO roomTenantDAO) {
        // Kiểm tra thành viên đã ở trong phòng hay chưa
        int isMemberExists = roomTenantDAO.checkMemberExistsInRoom(roomId, tenantId);
        if (isMemberExists > 0) {
            Toast.makeText(getContext(), "Thành viên này đã ở trong phòng!", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Lấy số lượng người hiện tại trong phòng
        int currentTenantCount = roomTenantDAO.getCurrentTenantCount(roomId);

        // Lấy sức chứa tối đa của phòng
        int roomCapacity = roomTenantDAO.getRoomMaxOccupants(roomId);

        if (currentTenantCount >= roomCapacity) {
            Toast.makeText(getContext(), "Phòng đã đạt số lượng tối đa ->" +currentTenantCount + "/"  + roomCapacity, Toast.LENGTH_SHORT).show();
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
            database.roomDAO().updateStatusRoom("Còn slot", roomId);
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
            Toast.makeText(getContext(), "Thêm thành viên vào phòng thành công!", Toast.LENGTH_SHORT).show();
            checkStatusRoom(roomTenantDAO, roomId);
        } else {
            Toast.makeText(getContext(), "Không thể thêm thành viên vào phòng.", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent();
        intent.putExtra("result_key", "some_result");
        requireActivity().setResult(Activity.RESULT_OK, intent);
        requireActivity().finish();

    }


}
