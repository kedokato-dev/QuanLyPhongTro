package com.example.quanlyphongtro.database;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.quanlyphongtro.model.RoomType;

import java.util.List;

@Dao
public interface RoomTypeDAO {

    @Query("DELETE FROM ROOMTYPE")
    void deleteAllData();

    @Query("SELECT * FROM ROOMTYPE")
    List<RoomType> getAllRoomType();

    @Query("SELECT RoomTypeId FROM ROOMTYPE WHERE typeName = :roomType ")
    int SelectIdRoomTypeByRoomType(String roomType);



}
