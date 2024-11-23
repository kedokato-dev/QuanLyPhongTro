package com.example.quanlyphongtro.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.NumberFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyphongtro.R;
import com.example.quanlyphongtro.adapter.CategoryAdapter;
import com.example.quanlyphongtro.adapter.ItemServiceAddBillAdapter;
import com.example.quanlyphongtro.database.QuanLyPhongTroDB;
import com.example.quanlyphongtro.model.Bill;
import com.example.quanlyphongtro.model.BillDetail;
import com.example.quanlyphongtro.model.Category;
import com.example.quanlyphongtro.model.Room;
import com.example.quanlyphongtro.model.Service;
import com.example.quanlyphongtro.model.Tenant;
import com.example.quanlyphongtro.pojo.RoomNumber_RoomTypeName;
import com.example.quanlyphongtro.pojo.ServiceInBillPOJO;
import com.example.quanlyphongtro.pojo.ServicePOJO;
import com.example.quanlyphongtro.pojo.UserPOJO;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AddBillActivity extends AppCompatActivity {
    private Spinner spnRoomNumber, spnFullName, spnStatus, spnService;
    private QuanLyPhongTroDB database;
    private List<Room> roomList;
    private List<ServicePOJO> serviceList;
    private List<UserPOJO> memberList;
    private Button btnAddServices;

    private Button btnAddBill, btnClearBill;

    private EditText edtInvoiceDate, edtQuantityService;
    private TextView tvAmountService, tvRoomPrice, tvToalAmount;

    private String roomNumber, memberName, dateInvoice, status, serviceName;

    private ItemServiceAddBillAdapter adapter;

    private List<ServiceInBillPOJO> listService;
    private RecyclerView rcvService;

    private List<ServiceInBillPOJO> list = new ArrayList<>();
    private List<Service> listServices = new ArrayList<>();
    private List<ServicePOJO> listServiceNameAndPrice;
    private double priceRoom;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_bill);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadView();
        loadToolBar();

        database = QuanLyPhongTroDB.getInstance(AddBillActivity.this);

        CategoryAdapter categoryAdapterRoomNumber = new CategoryAdapter(this, R.layout.item_selected, getListRoomNumber());
        CategoryAdapter categoryAdapterStatus = new CategoryAdapter(this, R.layout.item_selected, getListStatus());
        CategoryAdapter categoryAdapterService = new CategoryAdapter(this, R.layout.item_selected, getListServiceName());
        CategoryAdapter categoryAdapterMemberName = new CategoryAdapter(this, R.layout.item_selected, getListMemberName());

        spnRoomNumber.setAdapter(categoryAdapterRoomNumber);
        spnFullName.setAdapter(categoryAdapterMemberName);
        spnStatus.setAdapter(categoryAdapterStatus);
        spnService.setAdapter(categoryAdapterService);

        adapter = new ItemServiceAddBillAdapter(this);

        listService = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        rcvService.setLayoutManager(linearLayoutManager);

        spnRoomNumber.setSelection(0);
        spnRoomNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                roomNumber = categoryAdapterRoomNumber.getItem(i).getName();

                List<RoomNumber_RoomTypeName> list = database.roomDAO().getRoomNumber_RoomTypeName(roomNumber);
                tvRoomPrice.setText(convertDoubleToVND(list.get(0).getPrice()));

                priceRoom = list.get(0).getPrice();
                tvToalAmount.setText(convertDoubleToVND(list.get(0).getPrice()));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnFullName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                memberName = categoryAdapterMemberName.getItem(i).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                status = categoryAdapterStatus.getItem(i).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                serviceName = categoryAdapterService.getItem(i).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        edtInvoiceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });


        listService = new ArrayList<>();
        listService = database.serviceDAO().getListServiceInBill(roomNumber);

        btnAddServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addServices();
            }
        });

        btnAddBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBill();
            }
        });

        btnClearBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearBill();
            }
        });

        adapter.setOnItemClickListener(new ItemServiceAddBillAdapter.OnItemClickListener() {
            @Override
            public void onClickDelete(int position) {
                deleteDialog(Gravity.CENTER, position);
            }
        });


    }


    private void loadToolBar() {
        Toolbar toolbar = findViewById(R.id.app_bar_add_bill);
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

    private void loadView() {
        spnRoomNumber = findViewById(R.id.spn_roomNumber);
        spnFullName = findViewById(R.id.spn_fullName);
        spnStatus = findViewById(R.id.spn_status);
        spnService = findViewById(R.id.spn_service);

        edtInvoiceDate = findViewById(R.id.edt_invoice_date);
        edtQuantityService = findViewById(R.id.tv_quantityService);
        btnAddServices = findViewById(R.id.btn_add_service);

        tvAmountService = findViewById(R.id.tv_amount_service);
        tvRoomPrice = findViewById(R.id.tv_room_price);
        tvToalAmount = findViewById(R.id.tv_total_amount);

        btnAddBill = findViewById(R.id.btn_add_bill);
        btnClearBill = findViewById(R.id.btn_clear);

        rcvService = findViewById(R.id.rcv_service);
    }

    private List<Category> getListRoomNumber() {
        roomList = database.roomDAO().getNonEmptyRooms();
        List<Category> list = new ArrayList<>();

        if (roomList != null) {
            for (int i = 0; i < roomList.size(); i++) {
                list.add(new Category(roomList.get(i).getRoomNumber()));
            }
        }
        return list;
    }

    private List<Category> getListMemberName() {
        List<Category> categoryList = new ArrayList<>();
        try {
            Room room = database.roomDAO().getInfoRoomByRoomNumber(roomNumber);
            List<UserPOJO> listMember = database.userDAO().getRoomMembers(room.getRoomId());

            if (listMember != null) {
               for(int i = 0; i < listMember.size(); i++){
                   categoryList.add(new Category(listMember.get(i).getFullName()));
               }
            } else {
                categoryList.add(new Category("Trống"));
            }
        } catch (Exception exception) {
            Log.d("room_list", Objects.requireNonNull(exception.getMessage()));
        }
        return categoryList;
    }

    private List<Category> getListServiceName() {
        serviceList = database.serviceDAO().getListService();
        List<Category> list = new ArrayList<>();

        if (serviceList != null) {
            for (int i = 0; i < serviceList.size(); i++) {
                list.add(new Category(serviceList.get(i).getServiceName()));
            }
        }
        return list;
    }

    private List<Category> getListStatus() {
        List<String> list = new ArrayList<>();
        List<Category> categoryList = new ArrayList<>();
        list.add("Chưa thanh toán");
        list.add("Đã thanh toán");
        for (int i = 0; i < list.size(); i++) {
            categoryList.add(new Category(list.get(i)));
        }
        return categoryList;
    }

    private void showDatePickerDialog() {
        // Lấy ngày hiện tại
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddBillActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Định dạng ngày sau khi chọn
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        edtInvoiceDate.setText(selectedDate);
                    }
                }, year, month, day);

        // Hiển thị DatePickerDialog
        datePickerDialog.show();
    }

    private List<ServiceInBillPOJO> getListService() {
        listServiceNameAndPrice = database.serviceDAO().getNameAndPriceService(serviceName);
        String serviceName = listServiceNameAndPrice.get(0).getServiceName();
        int quantity = Integer.parseInt(String.valueOf(edtQuantityService.getText()));
        double priceService = listServiceNameAndPrice.get(0).getPricePerUnit();
        double amount = priceService * quantity;

        list.add(new ServiceInBillPOJO(serviceName, priceService, quantity, amount));


        return list;
    }


    private void addServices() {
        // Kiểm tra nếu người dùng đã nhập đủ thông tin
        if (!edtQuantityService.getText().toString().trim().isEmpty() && serviceName != null) {
            adapter.setData(getListService());

            tvAmountService.setText(convertDoubleToVND(totalAmountServices()));
            tvToalAmount.setText(convertDoubleToVND(loadTotalAmountRoom()));

        } else {
            Toast.makeText(this, "Vui lòng chọn đủ thông tin trước khi thêm !", Toast.LENGTH_SHORT).show();
            return;  // Dừng thực hiện nếu thông tin không đầy đủ
        }

        // Nếu danh sách đã có phần tử
        if (!list.isEmpty()) {
            if (!checkUniqueService()) {
                rcvService.setAdapter(adapter);
            } else {
                Toast.makeText(this, "Dịch vụ này đã được chọn cho phòng này rồi, bạn hãy chọn dịch vụ khác nhé !", Toast.LENGTH_SHORT).show();

                // Kiểm tra trước khi xóa để tránh lỗi
                if (!list.isEmpty()) {
                    list.remove(list.size() - 1);
                    tvAmountService.setText(convertDoubleToVND(totalAmountServices()));
                    tvToalAmount.setText(convertDoubleToVND(loadTotalAmountRoom()));
                }
            }
        }

        tvAmountService.setText(convertDoubleToVND(totalAmountServices()));
        tvToalAmount.setText(convertDoubleToVND(loadTotalAmountRoom()));

        edtQuantityService.setText("");
    }


    private boolean checkUniqueService() {
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                for (int j = i + 1; j < list.size(); j++) {
                    if (list.get(i).getServiceName().equals(list.get(j).getServiceName()))
                        return true;
                }
            }
        }
        return false;
    }

    private double totalAmountServices() {
        double totalService = 0;
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                totalService = totalService + list.get(i).getAmount();
            }
        }
        return totalService;
    }

    private String convertDoubleToVND(double number) {
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        return numberFormat.format(number); // Thêm " VND" để rõ ràng hơn
    }

    private double loadTotalAmountRoom() {
        double totalAmountRoom = 0;
        totalAmountRoom = totalAmountServices() + priceRoom;
        return totalAmountRoom;
    }

    private void loadData() {
        adapter.setData(list);
        rcvService.setAdapter(adapter);
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

    private void deleteDialog(int gravity, int position) {
        Dialog dialog = createDialog(R.layout.layout_dialog_delete, gravity);
        if (dialog == null) {
            return;
        }

        TextView tvDialogName = dialog.findViewById(R.id.tv_delete_name);
        TextView tvDialogSubName = dialog.findViewById(R.id.tv_sub_delete_name);

        Button btnDelete = dialog.findViewById(R.id.btn_delete_dialog);
        ImageView imgClose = dialog.findViewById(R.id.iv_close);

        tvDialogName.setText("Xóa dịch vụ");
        tvDialogSubName.setText(list.get(position).getServiceName());

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(position);
                loadData();

                tvAmountService.setText(convertDoubleToVND(totalAmountServices()));
                tvToalAmount.setText(convertDoubleToVND(loadTotalAmountRoom()));
                dialog.dismiss();
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private String formatDate(String input) throws ParseException {
        String dateAfterFormated = "";
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = inputFormat.parse(input);
        return dateAfterFormated = outputFormat.format(date);

    }

    private void addBill() {
        Room room = database.roomDAO().getInfoRoomByRoomNumber(roomNumber);
        Tenant tenant = database.userDAO().getTenantByNameFullName(memberName);

        String invoiceDate = edtInvoiceDate.getText().toString();

        // Kiểm tra dữ liệu đầu vào
        if (!TextUtils.isEmpty(edtInvoiceDate.getText()) && !list.isEmpty()) {
            // Thêm hóa đơn và lấy billId của hóa đơn mới
            int billId = 0;
            try {
                billId = (int) database.billDAO().insertBill(new Bill(
                        room.getRoomId(),
                        tenant.getTenantId(),
                        formatDate(invoiceDate),
                        123345,
                        status
                ));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            // Thêm các chi tiết hóa đơn
            for (int i = 0; i < list.size(); i++) {
                Service service = database.serviceDAO().getServiceByNameAndPrice(list.get(i).getServiceName(), list.get(i).getPricePerUnit());
                database.billDetailDAO().insertBillDetail(new BillDetail(
                        billId,                // Sử dụng billId từ bill vừa thêm
                        service.getServiceId(),
                        list.get(i).getQuantity(),
                        list.get(i).getAmount()
                ));
            }

            database.billDAO().updateTotalAmount();
            Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("action", "oke");
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else {
            // Thông báo nếu dữ liệu chưa đầy đủ
            Toast.makeText(this, "Vui lòng chọn đủ thông tin trước khi thêm hóa đơn", Toast.LENGTH_SHORT).show();
        }
    }


    private void clearBill() {

    }
}