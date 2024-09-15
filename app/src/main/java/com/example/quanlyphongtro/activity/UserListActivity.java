package com.example.quanlyphongtro.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyphongtro.Model.UserPOJO;
import com.example.quanlyphongtro.R;
import com.example.quanlyphongtro.adapter.ItemUserListAdapter;
import com.example.quanlyphongtro.database.QuanLyPhongTroDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {
    private  RecyclerView rcv_user_list;
    private List<UserPOJO> userPOJOList;
    private QuanLyPhongTroDB database;
    private ItemUserListAdapter itemUserListAdapter;
    private FloatingActionButton fab_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        loadToolBar();

        rcv_user_list = findViewById(R.id.rcv_user_list);
        fab_add = findViewById(R.id.fab_add);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcv_user_list.setLayoutManager(linearLayoutManager);
        itemUserListAdapter = new ItemUserListAdapter(this);


        userPOJOList = new ArrayList<>();
        database = (QuanLyPhongTroDB) QuanLyPhongTroDB.getInstance(this);

        userPOJOList = database.userDAO().getListUser();
        itemUserListAdapter.setData(userPOJOList);
        rcv_user_list.setAdapter(itemUserListAdapter);

        rcv_user_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0){
                    fab_add.hide();
                }else{
                    fab_add.show();
                }
            }
        });


    }

    private void loadToolBar(){
        Toolbar toolbar = findViewById(R.id.app_bar_user_list);
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
}