package com.example.quanlyphongtro.activity;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyphongtro.R;
import com.example.quanlyphongtro.adapter.ItemUserListAdapter;
import com.example.quanlyphongtro.database.QuanLyPhongTroDB;
import com.example.quanlyphongtro.model.Tenant;
import com.example.quanlyphongtro.pojo.UserPOJO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {
    private  RecyclerView rcv_user_list;
    private List<UserPOJO> userPOJOList;
    private QuanLyPhongTroDB database;
    private ItemUserListAdapter itemUserListAdapter;
    private FloatingActionButton fab_add;
    private SearchView searchView;
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

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogAddTenant(Gravity.CENTER);
            }
        });

        openDialogUpdateTenant(Gravity.CENTER);
        openDialogDeleteTenant(Gravity.CENTER);




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
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        // Lấy SearchView từ menu
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        // Cài đặt SearchableInfo
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setQueryHint("Nhập tên muốn tìm ?");

        // Thay đổi màu icon search
        ImageView searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_button);
        searchIcon.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);

        // đổi màu cho icon clear (X)
        ImageView closeIcon = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        closeIcon.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);

        // Thay đổi màu của icon search trong thanh nhập liệu
        ImageView searchEditIcon = searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
        searchEditIcon.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);

        // Thay đổi màu của text (nếu cần)
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.white));

        // Ví dụ: Thay đổi màu nền của khung nhập liệu
        View searchEditFrame = searchView.findViewById(androidx.appcompat.R.id.search_edit_frame);
        searchEditFrame.setBackgroundColor(getResources().getColor(R.color.transparent));

        // Cài đặt listener cho SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                itemUserListAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                itemUserListAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }


    private Dialog createDialog(int layoutResId, int gravity) {
        Dialog dialog = new Dialog(this);
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

    private void openDialogAddTenant(int gravity){

        Dialog dialog = createDialog(R.layout.layout_dialog_user,gravity);
        if (dialog == null){
            return;
        }

        TextView tvDialogName = dialog.findViewById(R.id.tv_dialog_name);
        TextView tvSubDialogName = dialog.findViewById(R.id.tv_sub_name_dialog);

        EditText edtMemberName = dialog.findViewById(R.id.edt_member_name);
        EditText edtMemberPhone = dialog.findViewById(R.id.edt_member_phone);
        EditText edtMemberEmail = dialog.findViewById(R.id.edt_member_email);
        EditText edtMemberCCCD = dialog.findViewById(R.id.edt_member_cccd);

        Button btnAdd = dialog.findViewById(R.id.btn_add);
        ImageView ivClose = dialog.findViewById(R.id.iv_close);



        tvDialogName.setText("Thêm thành viên mới");
        tvSubDialogName.setText("Thêm thành viên mới vào hệ thống");

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strMemberName = edtMemberName.getText().toString().trim();
                String strMemberPhone = edtMemberPhone.getText().toString().trim();
                String strMemberEmail = edtMemberEmail.getText().toString().trim();
                String strMemberCCCD = edtMemberCCCD.getText().toString().trim();

                if (!strMemberName.isEmpty() && !strMemberPhone.isEmpty()
                        && !strMemberEmail.isEmpty() && !strMemberCCCD.isEmpty()){
                    Tenant tenant = new Tenant(strMemberName,  strMemberPhone, strMemberEmail, strMemberCCCD);
                    database.userDAO().insertMember(tenant);
                    loadData();
                }else {
                    Toast.makeText(UserListActivity.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }

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

    private void loadData(){
        userPOJOList.clear();
        userPOJOList = database.userDAO().getListUser();
        itemUserListAdapter.setData(userPOJOList);
    }


    private void openDialogUpdateTenant(int gravity){

        itemUserListAdapter.setOnClickItemUpdateListener(new ItemUserListAdapter.OnClickItemUpdateListener() {
            @Override
            public void onClickUpdateItem(int position) {
                Dialog dialog = createDialog(R.layout.layout_dialog_user,gravity);
                if (dialog == null){
                    return;
                }

                TextView tvDialogName = dialog.findViewById(R.id.tv_dialog_name);
                TextView tvSubDialogName = dialog.findViewById(R.id.tv_sub_name_dialog);

                EditText edtMemberName = dialog.findViewById(R.id.edt_member_name);
                EditText edtMemberPhone = dialog.findViewById(R.id.edt_member_phone);
                EditText edtMemberEmail = dialog.findViewById(R.id.edt_member_email);
                EditText edtMemberCCCD = dialog.findViewById(R.id.edt_member_cccd);

                Button btnAdd = dialog.findViewById(R.id.btn_add);
                ImageView ivClose = dialog.findViewById(R.id.iv_close);



                tvDialogName.setText("Cập nhật thành viên");
                tvSubDialogName.setText("Điều chỉnh thông tin thành viên");
                
                edtMemberName.setText(userPOJOList.get(position).getFullName());    
                edtMemberPhone.setText(userPOJOList.get(position).getPhone());    
                edtMemberEmail.setText(userPOJOList.get(position).getEmail());    
                edtMemberCCCD.setText(userPOJOList.get(position).getIdentityCard());

                String CCCD_Old = userPOJOList.get(position).getIdentityCard();
                Tenant tenant =  database.userDAO().getMemberByCCCD(CCCD_Old);
                int idMember = tenant.getTenantId();

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String strMemberName = edtMemberName.getText().toString().trim();
                        String strMemberPhone = edtMemberPhone.getText().toString().trim();
                        String strMemberEmail = edtMemberEmail.getText().toString().trim();
                        String strMemberCCCD = edtMemberCCCD.getText().toString().trim();

                        if (!strMemberName.isEmpty() && !strMemberPhone.isEmpty()
                                && !strMemberEmail.isEmpty() && !strMemberCCCD.isEmpty()){
                            database.userDAO().updateMember(new Tenant(idMember, strMemberName, strMemberPhone, strMemberEmail, strMemberCCCD));
                            Toast.makeText(UserListActivity.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                            loadData();
                        }else {
                            Toast.makeText(UserListActivity.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                        }



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

    private void openDialogDeleteTenant(int gravity){

        itemUserListAdapter.setOnClickItemDeleteListener(new ItemUserListAdapter.OnClickItemDeleteListener() {
            @Override
            public void onClickDeleteItem(int position) {
                Dialog dialog = createDialog(R.layout.layout_dialog_delete,gravity);
                if (dialog == null){
                    return;
                }

                TextView tvDialogDelete = dialog.findViewById(R.id.tv_delete_name);
                TextView tvSubDialogName = dialog.findViewById(R.id.tv_sub_delete_name);


                Button btnDelete = dialog.findViewById(R.id.btn_delete_dialog);
                ImageView ivClose = dialog.findViewById(R.id.iv_close);


                tvDialogDelete.setText("Xóa thành viên");
                tvSubDialogName.setText(userPOJOList.get(position).getFullName());


                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    String cccd =  userPOJOList.get(position).getIdentityCard();
                    Tenant tenant = database.userDAO().getMemberByCCCD(cccd);

                    database.userDAO().deleteMember(tenant);
                    Toast.makeText(UserListActivity.this, "Xóa thành công thành viên "+tenant.getFullName(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        loadData();
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