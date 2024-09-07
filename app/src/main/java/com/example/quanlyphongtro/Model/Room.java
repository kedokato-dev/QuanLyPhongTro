package com.example.quanlyphongtro.Model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Room",foreignKeys = @ForeignKey(entity = RoomType.class, parentColumns = "roomTypeId", childColumns = "roomTypeId"))
public class Room {
    @PrimaryKey(autoGenerate = true)
    private int roomId;
    private String roomNumber;
    private int roomTypeId;
    private String status;
    private double price;

    public Room(int roomId, String roomNumber, int roomTypeId, String status, double price) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.roomTypeId = roomTypeId;
        this.status = status;
        this.price = price;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(int roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
