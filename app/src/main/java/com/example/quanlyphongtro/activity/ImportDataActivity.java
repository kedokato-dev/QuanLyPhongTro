package com.example.quanlyphongtro.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlyphongtro.R;
import com.example.quanlyphongtro.database.QuanLyPhongTroDB;
import com.example.quanlyphongtro.model.Room;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class ImportDataActivity extends AppCompatActivity {
    private Button btnImport;
    private Button btnFolderOpen;
    private TextView tvPathFile;
    private Uri pathFile;
    private QuanLyPhongTroDB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_import_data);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadView();
        loadToolBar();

        database = QuanLyPhongTroDB.getInstance(this);

        btnFolderOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFolder();
            }
        });

        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Xóa dữ liệu trong tất cả các bảng trước khi nhập dữ liệu mới
                deleteAllData();

                // Nhập dữ liệu từ file
                if (pathFile != null) {
                    importDatabase(pathFile);
                } else {
                    Toast.makeText(ImportDataActivity.this, "Vui lòng chọn file để nhập dữ liệu!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadView(){
        btnImport = findViewById(R.id.btn_import);
        btnFolderOpen = findViewById(R.id.btn_folder_open_import);
        tvPathFile = findViewById(R.id.tv_path_file_import);
    }

    private void loadToolBar(){
        Toolbar toolbar = findViewById(R.id.app_bar_import_db);
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

    private void openFolder() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/octet-stream"); // Hoặc sử dụng loại MIME cho tệp cơ sở dữ liệu nếu có
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                String mimeType = getContentResolver().getType(uri);
                String fileName = uri.getLastPathSegment();
                if (mimeType != null && mimeType.equals("application/octet-stream") || fileName != null && fileName.endsWith(".db")) {
                    pathFile = uri;
                    tvPathFile.setText(uri.toString());
                } else {
                    Toast.makeText(this, "Tệp không phải là cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void deleteAllData() {
        // Xóa dữ liệu trong các bảng với thứ tự phù hợp để tránh lỗi khóa ngoại
        database.billDetailDAO().deleteAllData();
        database.serviceDAO().deleteAllData();
        database.billDAO().deleteAllData();
        database.roomTenantDAO().deleteAllData();
        database.roomDAO().deleteAllData();
        database.roomTypeDAO().deleteAllData();
        database.userDAO().deleteAllData();
        Toast.makeText(this, "Dữ liệu cũ đã được xóa!", Toast.LENGTH_SHORT).show();
    }

    private void importDatabase(Uri uri) {
        File dbFile = getDatabasePath("QuanLyPhongTro.db");
        File shmFile = new File(getDatabasePath("QuanLyPhongTro.db").getAbsolutePath() + "-shm");
        File walFile = new File(getDatabasePath("QuanLyPhongTro.db").getAbsolutePath() + "-wal");

        // Xóa các tệp phụ
        if (shmFile.exists()) {
            shmFile.delete();
        }
        if (walFile.exists()) {
            walFile.delete();
        }

        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             OutputStream outputStream = new FileOutputStream(dbFile)) {

            if (inputStream != null) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();
                Toast.makeText(this, "Nhập dữ liệu thành công!", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Nhập dữ liệu thất bại!", Toast.LENGTH_SHORT).show();
        }
    }




    private void verifyData() {
        // Ví dụ: kiểm tra bảng Room
        List<Room> rooms = database.roomDAO().getAllRoom();
        if (rooms.isEmpty()) {
            Toast.makeText(this, "Không có dữ liệu trong bảng Room!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Có dữ liệu trong bảng Room!", Toast.LENGTH_SHORT).show();
        }
    }

}
