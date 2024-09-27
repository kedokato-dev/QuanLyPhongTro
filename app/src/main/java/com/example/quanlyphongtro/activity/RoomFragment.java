package com.example.quanlyphongtro.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyphongtro.R;
import com.example.quanlyphongtro.adapter.ItemRoomAdapter;
import com.example.quanlyphongtro.database.QuanLyPhongTroDB;
import com.example.quanlyphongtro.model.Room;
import com.example.quanlyphongtro.model.RoomType;
import com.example.quanlyphongtro.pojo.RoomWithTenantInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class RoomFragment extends Fragment {
    private RecyclerView rcv;
    private ItemRoomAdapter itemRoomAdapter;
    private QuanLyPhongTroDB database;
    private FloatingActionButton fab;
    private List<RoomWithTenantInfo> infoList;
    private List<RoomType> roomTypeList;
    private List<Room> roomList = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room, container, false);

//        Toolbar toolbar = view.findViewById(R.id.app_bar_room);
        fab = view.findViewById(R.id.fab_add_room);

        // Khởi tạo database
        database = QuanLyPhongTroDB.getInstance(getContext());
        infoList = new ArrayList<>();
        roomTypeList = new ArrayList<>();

        // Khởi tạo RecyclerView và adapter
        rcv = view.findViewById(R.id.rcv_item);
        itemRoomAdapter = new ItemRoomAdapter(getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcv.setLayoutManager(linearLayoutManager);

        infoList = database.roomDAO().getRoomWithTenantInfo();
        itemRoomAdapter.setData(infoList);
        rcv.setAdapter(itemRoomAdapter);

        rcv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0){
                    fab.hide();
                }else{
                    fab.show();
                }
            }
        });


        itemRoomAdapter.setOnClickListener(new ItemRoomAdapter.SetOnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Lấy roomNumber từ item clicked
                String roomNumber = infoList.get(position).getRoomNumber();

                // Tạo Intent để truyền dữ liệu
                Intent intent = new Intent(getContext(), DetailRoomActivity.class);
                intent.putExtra("roomNumber", roomNumber);
                startActivity(intent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddRoom(Gravity.CENTER);
            }
        });


        deleteRoom(Gravity.CENTER);
        updateRoom(Gravity.CENTER);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Đảm bảo hiển thị FAB khi RoomFragment được hiển thị lại
        if (fab != null) {
            fab.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Hiển thị lại FAB khi quay lại fragment
        if (fab != null) {
            fab.show();
        }
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

    private void loadData() {
        infoList.clear();
        infoList = database.roomDAO().getRoomWithTenantInfo();
        itemRoomAdapter.setData(infoList);
    }

    private void openAddRoom(int gravity) {
        Dialog dialog = createDialog(R.layout.layout_dialog_room, gravity);
        if (dialog == null) return;

        TextView tvDialogName = dialog.findViewById(R.id.tv_room_dialog_name);
        TextView tvSubDialogName = dialog.findViewById(R.id.tv_room_sub_name_dialog);

        EditText edtRoomNumber = dialog.findViewById(R.id.edt_room_number);
        EditText edtRoomPrice = dialog.findViewById(R.id.edt_room_price);

        Button btnAdd = dialog.findViewById(R.id.btn_add);
        ImageView ivClose = dialog.findViewById(R.id.iv_close);

        Spinner spinner = dialog.findViewById(R.id.sp_select_room_type);


        roomTypeList = database.roomTypeDAO().getAllRoomType();

        tvSubDialogName.setText("Nhập thông tin phòng mới vào hệ thống");
        tvDialogName.setText("Thêm phòng!");

        // Lấy dữ liệu loại phòng đưa vào arr loại phòng
        List<String> roomTypeNames = new ArrayList<>();
        for (RoomType roomType : roomTypeList) {
            roomTypeNames.add(roomType.getTypeName());
        }

        // Tạo ArrayAdapter cho Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, roomTypeNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Gán adapter vào Spinner
        spinner.setAdapter(adapter);


        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strRoomNumber = edtRoomNumber.getText().toString().trim();
                String strRoomPrice = edtRoomPrice.getText().toString().trim();

                if (!strRoomNumber.isEmpty() && !strRoomPrice.isEmpty()) {
                    try {
                        Double dRoomPrice = Double.parseDouble(strRoomPrice);


                        String roomTypeSelected = spinner.getSelectedItem().toString();
                        int idType = database.roomTypeDAO().SelectIdRoomTypeByRoomType(roomTypeSelected);

                        Room room = new Room(strRoomNumber, idType, "Chưa có người thuê", dRoomPrice);
                        database.roomDAO().insertRoom(room);

                        Toast.makeText(getContext(), "Thêm phòng " + strRoomNumber + " thành công", Toast.LENGTH_SHORT).show();
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Giá phòng không hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Hãy nhập đủ thông tin trước khi thêm", Toast.LENGTH_SHORT).show();
                }
                loadData();
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private void deleteRoom(int gravity) {
        itemRoomAdapter.setOnDeleteClickListener(new ItemRoomAdapter.OnDeleteClickListener() {

            @Override
            public void onDeleteClick(int position) {
                Dialog dialog = createDialog(R.layout.layout_dialog_delete, gravity);

                if (dialog == null) {
                    return;
                }

                TextView tvDeleteName = dialog.findViewById(R.id.tv_delete_name);
                TextView tvSubDeleteName = dialog.findViewById(R.id.tv_sub_delete_name);

                ImageView ivClose = dialog.findViewById(R.id.iv_close);
                Button btnDelete = dialog.findViewById(R.id.btn_delete_dialog);

                tvDeleteName.setText("Xóa phòng");
                tvSubDeleteName.setText("phòng " + infoList.get(position).getRoomNumber());

                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (position >= 0 && position < infoList.size()) {
                            RoomWithTenantInfo roomWithTenantInfo = infoList.get(position);

                            Room room = database.roomDAO().getInfoRoomByRoomNumber(roomWithTenantInfo.getRoomNumber());

                            if (room != null) {
                                database.roomDAO().deleteRoom(room);
                                Toast.makeText(getContext(), "Xóa thành công phòng " + room.getRoomNumber(), Toast.LENGTH_SHORT).show();
                                loadData();
                            }
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void updateRoom(int gravity) {
        itemRoomAdapter.setOnUpdateClickListener(new ItemRoomAdapter.OnUpdateClickListener() {
            @Override
            public void onUpdateClick(int position) {
                Dialog dialog = createDialog(R.layout.layout_dialog_room, gravity);
                if (dialog == null) {
                    return;
                }

                TextView tvDialogName = dialog.findViewById(R.id.tv_room_dialog_name);
                TextView tvSubDialogName = dialog.findViewById(R.id.tv_room_sub_name_dialog);

                EditText edtRoomNumber = dialog.findViewById(R.id.edt_room_number);
                EditText edtRoomPrice = dialog.findViewById(R.id.edt_room_price);

                Button btnAdd = dialog.findViewById(R.id.btn_add);
                ImageView ivClose  = dialog.findViewById(R.id.iv_close);

                Spinner spinner = dialog.findViewById(R.id.sp_select_room_type);


                tvDialogName.setText("Chỉnh sửa thông tin phòng!");
                tvSubDialogName.setText("Hãy nhập thông tin phòng bạn muốn sửa");

                roomTypeList = database.roomTypeDAO().getAllRoomType();


                List<String> roomTypeNames = new ArrayList<>();
                for (RoomType roomType : roomTypeList) {
                    roomTypeNames.add(roomType.getTypeName());
                }

                // Tạo ArrayAdapter cho Spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_item, roomTypeNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(adapter);


                edtRoomNumber.setText(infoList.get(position).getRoomNumber());

                Room room = database.roomDAO().getInfoRoomByRoomNumber(edtRoomNumber.getText().toString());
                Double dRoomPrice = room.getPrice();
                edtRoomPrice.setText(String.valueOf(dRoomPrice));

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String strRoomNumber = edtRoomNumber.getText().toString().trim();
                        String strRoomPrice = edtRoomPrice.getText().toString().trim();

                        if (!strRoomNumber.isEmpty() && !strRoomPrice.isEmpty()) {
                            try {
                                Double dRoomPrice = Double.parseDouble(strRoomPrice);


                                String roomTypeSelected = spinner.getSelectedItem().toString();
                                int idType = database.roomTypeDAO().SelectIdRoomTypeByRoomType(roomTypeSelected);

                                Room room = database.roomDAO().getInfoRoomByRoomNumber(infoList.get(position).getRoomNumber());

                                database.roomDAO().updateRoom(new Room(room.getRoomId(), strRoomNumber, idType, room.getStatus(), dRoomPrice));

                                Toast.makeText(getContext(), "Sửa phòng " + strRoomNumber + " thành công", Toast.LENGTH_SHORT).show();
                            } catch (NumberFormatException e) {
                                Toast.makeText(getContext(), "Giá phòng không hợp lệ", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Hãy nhập đủ thông tin trước khi sửa", Toast.LENGTH_SHORT).show();
                        }
                        loadData();
                        dialog.dismiss();
                    }
                });

                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }


}

