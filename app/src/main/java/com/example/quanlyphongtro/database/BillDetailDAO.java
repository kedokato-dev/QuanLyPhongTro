package com.example.quanlyphongtro.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.quanlyphongtro.model.BillDetail;

@Dao
public interface BillDetailDAO {
    @Query("DELETE FROM BILLDETAIL")
    void deleteAllData();

    @Insert()
    void insertBillDetail(BillDetail billDetail);


}
