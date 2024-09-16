package com.example.quanlyphongtro.database;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.quanlyphongtro.model.UserPOJO;

import java.util.List;

@Dao
public interface UserDAO
{
    @Query("SELECT fullName, phone, email, identityCard\n" +
            "FROM Tenant")
    List<UserPOJO> getListUser();
}
