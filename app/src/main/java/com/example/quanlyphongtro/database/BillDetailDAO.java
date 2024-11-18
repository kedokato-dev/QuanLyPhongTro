package com.example.quanlyphongtro.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.quanlyphongtro.model.BillDetail;

@Dao
public interface BillDetailDAO {
    @Query("DELETE FROM BILLDETAIL")
    void deleteAllData();

    @Insert()
    void insertBillDetail(BillDetail billDetail);

    // VIẾT HÀM CẬP NHẬT THÔNG TIN CỦA BILLDETAIL
    @Update
    void updateBillDetail(BillDetail billDetail);

    @Query("SELECT * FROM BILLDETAIL WHERE BILLDETAIL.billDetailId =:id")
    BillDetail getBillDetailById(int id);

    @Delete
    void deleteBillDetail(BillDetail billDetail);


}
