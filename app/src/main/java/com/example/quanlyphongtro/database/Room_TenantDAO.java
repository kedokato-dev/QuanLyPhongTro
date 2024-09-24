package com.example.quanlyphongtro.database;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface Room_TenantDAO {

    @Query("DELETE FROM ROOM_TENANT")
    void deleteAllData();

}
