package com.example.quanlyphongtro.database;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface RoomTypeDAO {

    @Query("DELETE FROM ROOMTYPE")
    void deleteAllData();

}
