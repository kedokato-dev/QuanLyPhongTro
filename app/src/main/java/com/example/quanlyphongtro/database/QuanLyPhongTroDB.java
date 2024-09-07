package com.example.quanlyphongtro.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.quanlyphongtro.Model.Room;
import com.example.quanlyphongtro.Model.RoomType;

    @Database(entities = {Room.class, RoomType.class}, version = 1)
    public abstract class QuanLyPhongTroDB extends RoomDatabase {
        private static final String DATABASE_NAME = "QuanLyPhongTro.db";
        private static QuanLyPhongTroDB instance;

    public static synchronized QuanLyPhongTroDB getInstance(Context context){
        if(instance != null){
        instance = androidx.room.Room.databaseBuilder(context.getApplicationContext(), QuanLyPhongTroDB.class, DATABASE_NAME)
                .allowMainThreadQueries().build();
          }
        return instance;
        }
}
