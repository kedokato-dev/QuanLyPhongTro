package com.example.quanlyphongtro.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.quanlyphongtro.Model.Service;
import com.example.quanlyphongtro.Model.ServicePOJO;

import java.util.List;

@Dao
public interface ServiceDAO {
    @Insert
    void insertService(Service service);
    @Query("SELECT serviceName, pricePerUnit FROM SERVICE")
    List<ServicePOJO> getListService();

}
