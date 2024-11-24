package com.example.quanlyphongtro.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.quanlyphongtro.fragment.AddTenantToRoomFragment;
import com.example.quanlyphongtro.fragment.RemoveTenantToRoomFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {


    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new AddTenantToRoomFragment();

            case 1:
                return new RemoveTenantToRoomFragment();

            default:
                return new AddTenantToRoomFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
