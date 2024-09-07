package com.example.quanlyphongtro.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "RoomType")
public class RoomType {
    @PrimaryKey(autoGenerate = true)
    private int roomTypeId;
    private String typeName;
    private String description;
    private int maxOccupants;

    public RoomType(int roomTypeId, String typeName, String description, int maxOccupants) {
        this.roomTypeId = roomTypeId;
        this.typeName = typeName;
        this.description = description;
        this.maxOccupants = maxOccupants;
    }

    public int getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(int roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxOccupants() {
        return maxOccupants;
    }

    public void setMaxOccupants(int maxOccupants) {
        this.maxOccupants = maxOccupants;
    }
}
