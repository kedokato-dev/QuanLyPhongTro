package com.example.quanlyphongtro.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.quanlyphongtro.model.Tenant;
import com.example.quanlyphongtro.pojo.UserPOJO;

import java.util.List;

@Dao
public interface UserDAO
{
    @Query("SELECT fullName, phone, email, identityCard\n" +
            "FROM Tenant")
    List<UserPOJO> getListUser();


    @Query("SELECT * FROM TENANT")
    List<Tenant> getAllUser();

    @Query("DELETE FROM TENANT")
    void deleteAllData();

    @Delete
    void deleteMember(Tenant tenant);

    @Insert
    void insertMember(Tenant tenant);

    @Update
    void updateMember(Tenant tenant);

    @Query("SELECT * FROM TENANT WHERE identityCard = :cccd")
    Tenant getMemberByCCCD(String cccd);
}
