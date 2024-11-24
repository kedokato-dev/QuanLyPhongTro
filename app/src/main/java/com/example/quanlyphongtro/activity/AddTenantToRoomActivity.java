package com.example.quanlyphongtro.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.quanlyphongtro.R;
import com.example.quanlyphongtro.adapter.MyViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class AddTenantToRoomActivity extends AppCompatActivity {
    private TabLayout mTablayout;
    private ViewPager2 viewPager2;
    private MyViewPagerAdapter myViewPagerAdapter;

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

        myViewPagerAdapter = new MyViewPagerAdapter(this);
        viewPager2.setAdapter(myViewPagerAdapter);

        new TabLayoutMediator(mTablayout, viewPager2, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Thêm TV vào phòng");
                    break;

                case 1:
                    tab.setText("Xoá TV khỏi phòng");
                    break;
            }
        }).attach();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Kết thúc Activity và quay lại Activity trước đó
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadView(){
        mTablayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager);
    }


}