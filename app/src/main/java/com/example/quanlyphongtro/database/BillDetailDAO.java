package com.example.quanlyphongtro.database;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface BillDetailDAO {
    @Query("DELETE FROM BILLDETAIL")
    void deleteAllData();


}
