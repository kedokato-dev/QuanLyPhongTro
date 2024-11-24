package com.example.quanlyphongtro.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.quanlyphongtro.R;
import com.example.quanlyphongtro.fragment.BillFragment;
import com.example.quanlyphongtro.fragment.HomeFragment;
import com.example.quanlyphongtro.fragment.MenuFragment;
import com.example.quanlyphongtro.fragment.RoomFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;


public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.bottomNavView);
        frameLayout = findViewById(R.id.frameLayout);


        // bo tròn các góc của thanh navigation
        MaterialShapeDrawable shapeDrawable = (MaterialShapeDrawable) bottomNavigationView.getBackground();
        shapeDrawable.setShapeAppearanceModel(
                shapeDrawable.getShapeAppearanceModel()
                        .toBuilder()
                        .setTopLeftCorner(CornerFamily.ROUNDED, 100f)  // 100f là độ bo góc
                        .setTopRightCorner(CornerFamily.ROUNDED, 100f)  // 100f là độ bo góc
                        .build());

       bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){

           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
               int itemID = menuItem.getItemId();

               if(itemID == R.id.navHome){
                   loadFragment(new HomeFragment(), false);
               }else if(itemID == R.id.navNotification){
                   loadFragment(new BillFragment(), false);
               }else if(itemID == R.id.navSearch){
                   loadFragment(new RoomFragment(), false);
               }else { // nav profile
                   loadFragment(new MenuFragment(), false);
               }
               return true;
           }
       });
        loadFragment(new HomeFragment(), true);
    }

    private void loadFragment(Fragment fragment, boolean isAppInitialized){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(isAppInitialized){
            fragmentTransaction.add(R.id.frameLayout, fragment);
        }else{
            fragmentTransaction.replace(R.id.frameLayout, fragment);
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack(); // Quay lại fragment trước
        } else {
            super.onBackPressed(); // Nếu không còn fragment nào trong back stack, thoát ứng dụng
        }
    }


}