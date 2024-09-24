package com.example.quanlyphongtro.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.quanlyphongtro.R;
import com.example.quanlyphongtro.database.QuanLyPhongTroDB;
import com.example.quanlyphongtro.model.Bill;
import com.example.quanlyphongtro.model.Room;
import com.example.quanlyphongtro.model.Service;
import com.example.quanlyphongtro.model.Tenant;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ExportDataActivity extends AppCompatActivity {
    private QuanLyPhongTroDB database;
    private List<Room> listRoom = new ArrayList<>();
    private List<Tenant> listMember = new ArrayList<>();
    private List<Service> serviceList = new ArrayList<>();
    private List<Bill> listBill = new ArrayList<>();
    private CheckBox cbRoom;
    private CheckBox cbMember;
    private CheckBox cbService;
    private CheckBox cbRoomType;
    private CheckBox cbBill;
    private CheckBox cbBillDetail;
    private CheckBox cbRoomTenant;
    private CheckBox cbAll;
    private TextView tvPathFile;
    private Button btnFolderOpen;
    private Button btnExport;
    private  Uri pathFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_export_data);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo database và lấy danh sách Room
        database = QuanLyPhongTroDB.getInstance(this);



        loadToolBar();
        loadView();

        btnFolderOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFolder();
            }
        });

        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               CheckAndWriteFileCsv();
            }
        });
    }



    private void openFolder() {
        // Intent để lưu file
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("*/*"); // Loại file, ví dụ: "text/plain" cho file .txt
        intent.putExtra(Intent.EXTRA_TITLE, "myfile.csv"); // Tên mặc định của file
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Mở trình chọn file
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Lấy Uri của nơi lưu file
            Uri uri = data.getData();
            if (uri != null) {
                pathFile = uri;
                tvPathFile.setText(uri.toString());
            }
        }
    }

    private void CheckAndWriteFileCsv() {
        if(cbRoom.isChecked()){
            listRoom = database.roomDAO().getAllRoom();
            exportDataToCSV(listRoom);
        }else if(cbMember.isChecked()){
            listMember = database.userDAO().getAllUser();
            exportDataToCSV(listMember);
        }else if(cbAll.isChecked()){
            exportDatabase();
        }
    }



    private <T> void exportDataToCSV(List<T> dataList) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            if (pathFile != null) {
                try (OutputStream outputStream = getContentResolver().openOutputStream(pathFile)) {
                    if (outputStream != null) {
                        writeCSV(outputStream,  dataList);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // Xử lý cho các phiên bản Android cũ hơn nếu cần
        }
    }

    private <T> void writeCSV(OutputStream outputStream, List<T> dataList) {
        try (OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
            // Ghi header CSV tùy thuộc vào loại dữ liệu
            if (!dataList.isEmpty()) {
                T firstItem = dataList.get(0);
                if (firstItem instanceof Room) {
                    writer.append("Mã phòng, Số phòng, Mã loại phòng, Trạng thái, Giá\n");
                    for (Room room : (List<Room>) dataList) {
                        writer.append(String.valueOf(room.getRoomId()));
                        writer.append(",");
                        writer.append(room.getRoomNumber());
                        writer.append(",");
                        writer.append(String.valueOf(room.getRoomTypeId()));
                        writer.append(",");
                        writer.append(room.getStatus());
                        writer.append(",");
                        writer.append(String.valueOf(room.getPrice()));
                        writer.append("\n");
                    }
                } else if (firstItem instanceof Tenant) { // Thay đổi điều kiện theo model của bạn
                    writer.append("Mã khách hàng, Tên khách hàng, Số điện thoại, Email, Số cccd\n");
                    for (Tenant tenant : (List<Tenant>) dataList) {
                        writer.append(String.valueOf(tenant.getTenantId()));
                        writer.append(",");
                        writer.append(tenant.getFullName());
                        writer.append(",");
                        writer.append(tenant.getPhone());
                        writer.append(",");
                        writer.append(tenant.getEmail());
                        writer.append(",");
                        writer.append(tenant.getIdentityCard());
                        writer.append("\n");
                    }
                }
            }
            writer.flush();
            Toast.makeText(this, "Ghi file thành công", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadView(){
        cbRoom = findViewById(R.id.cb_room);
        cbRoomType = findViewById(R.id.cb_room_type);
        cbService = findViewById(R.id.cb_service);
        cbBill = findViewById(R.id.cb_bill);
        cbBillDetail = findViewById(R.id.cb_bill_detail);
        cbMember = findViewById(R.id.cb_member);
        cbAll = findViewById(R.id.cb_all);
        cbRoomTenant = findViewById(R.id.cb_room_tenant);

        btnFolderOpen = findViewById(R.id.btn_folder_open);
        btnExport = findViewById(R.id.btn_export);
        tvPathFile = findViewById(R.id.tv_path_file);
    }

    private void loadToolBar(){
        Toolbar toolbar = findViewById(R.id.app_bar_export_db);
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

    private void exportDatabase() {
        if (pathFile != null) {
            // Trước khi xuất, dọn dẹp WAL bằng cách checkpoint dữ liệu
            SupportSQLiteDatabase db = database.getOpenHelper().getWritableDatabase();
            db.disableWriteAheadLogging();

            // Đường dẫn tới Room Database
            File dbFile = getDatabasePath("QuanLyPhongTro.db");  // Tên file database của bạn

            try {
                // Mở InputStream từ database và OutputStream tới nơi đã chọn
                InputStream inputStream = new FileInputStream(dbFile);
                OutputStream outputStream = getContentResolver().openOutputStream(pathFile);

                if (outputStream != null) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }

                    inputStream.close();
                    outputStream.close();

                    Toast.makeText(this, "Xuất database thành công!", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Xuất database thất bại!", Toast.LENGTH_SHORT).show();
            } finally {
                db.enableWriteAheadLogging();
            }
        } else {
            Toast.makeText(this, "Vui lòng chọn vị trí lưu file!", Toast.LENGTH_SHORT).show();
        }
    }


}
