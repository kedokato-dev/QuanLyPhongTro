package com.example.quanlyphongtro.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import java.util.List;

public class RemoveTenantToRoomFragment extends Fragment {
    private Spinner spinnerRooms, spinnerTenants;
    private Button  btnRemoveTenantToRoom;
    private QuanLyPhongTroDB database;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmement_remove_tenant_to_room, container, false);

        spinnerRooms = view.findViewById(R.id.spinnerRooms);
        spinnerTenants = view.findViewById(R.id.spinnerTenants);
        btnRemoveTenantToRoom = view.findViewById(R.id.btnRemoveTenantToRoom);


        loadRooms();
//        loadTenants();

        // Xử lý sự kiện nút thêm
        btnRemoveTenantToRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Room selectedRoom = (Room) spinnerRooms.getSelectedItem();
                Tenant selectedTenant = (Tenant) spinnerTenants.getSelectedItem();
                if(selectedRoom != null && selectedTenant != null){
                    removeTenantToRoom(selectedRoom.getRoomId(), selectedTenant.getTenantId());
                    Toast.makeText(getContext(), "Xoá thành viên: "+selectedTenant.getFullName() +
                                    " ra khỏi phòng " +selectedRoom.getRoomNumber() + " thành công.",
                            Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(getContext(), "Vui lòng chọn đầy đủ thông tin trước khi xoá.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }
    private void loadRooms() {
        database = QuanLyPhongTroDB.getInstance(getContext());
        List<Room> rooms = database.roomDAO().getNonEmptyRooms();

        // Thiết lập adapter cho Spinner
        ArrayAdapter<Room> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, rooms);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRooms.setAdapter(adapter);

        // Sử dụng setOnItemSelectedListener để lắng nghe sự kiện khi người dùng chọn phòng
        spinnerRooms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Lấy phòng được chọn
                Room selectedRoom = (Room) parent.getItemAtPosition(position);

                // Gọi phương thức load danh sách thành viên của phòng
                loadTenantsByRoom(selectedRoom.getRoomId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì khi không có mục nào được chọn
            }
        });
    }


    // Load danh sách người thuê từ database
    private void loadTenantsByRoom(int roomId) {
        // Lấy danh sách thành viên thuộc phòng từ database
        List<Tenant> tenants = database.roomTenantDAO().getTenantInRoom(roomId);

        // Kiểm tra danh sách rỗng
        if (tenants.isEmpty()) {
            Toast.makeText(getContext(), "Phòng này chưa có thành viên nào!", Toast.LENGTH_SHORT).show();
        }

        // Hiển thị danh sách thành viên lên Spinner hoặc giao diện khác
        ArrayAdapter<Tenant> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tenants);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTenants.setAdapter(adapter);
    }

//    public boolean canAddTenantToRoom(int roomId, int tenantId, Room_TenantDAO roomTenantDAO) {
//        // Kiểm tra thành viên đã ở trong phòng hay chưa
//        int isMemberExists = roomTenantDAO.checkMemberExistsInRoom(roomId, tenantId);
//        if (isMemberExists > 0) {
//            Toast.makeText(getContext(), "Thành viên này đã ở trong phòng!", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//        // Lấy số lượng người hiện tại trong phòng
//        int currentTenantCount = roomTenantDAO.getCurrentTenantCount(roomId);
//
//        // Lấy sức chứa tối đa của phòng
//        int roomCapacity = roomTenantDAO.getRoomMaxOccupants(roomId);
//
//        if (currentTenantCount >= roomCapacity) {
//            Toast.makeText(getContext(), "Phòng đã đạt số lượng tối đa ->" +currentTenantCount + "/"  + roomCapacity, Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//        // Nếu không có vấn đề gì thì có thể thêm
//        return true;
//    }

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


    // Xoá người thuê ra khỏi phòng
    private void removeTenantToRoom(int roomId, int tenantId) {
        try {
            Room_TenantDAO roomTenantDAO = database.roomTenantDAO();
            database.roomTenantDAO().deleteTenantFromRoom(roomId, tenantId);

            checkStatusRoom(roomTenantDAO, roomId);

            Intent intent = new Intent();
            intent.putExtra("result_key", "some_result");
            requireActivity().setResult(Activity.RESULT_OK, intent);
            requireActivity().finish();

        }catch (Exception e){
            Toast.makeText(getContext(), "Chưa thể xoá thành viên ra khỏi phòng lúc này.", Toast.LENGTH_SHORT).show();
        }
    }
}
