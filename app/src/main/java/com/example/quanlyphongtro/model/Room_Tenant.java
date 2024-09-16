package com.example.quanlyphongtro.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "Room_Tenant", primaryKeys = {"roomId", "tenantId"}, foreignKeys = {
    @ForeignKey(entity = Room.class, parentColumns = "roomId", childColumns = "roomId"),
    @ForeignKey(entity = Tenant.class, parentColumns = "tenantId", childColumns = "tenantId")
    })
public class Room_Tenant {
    private int roomId;
    private int tenantId;
    private String startDate;
    private String endDate;

    public Room_Tenant(int roomId, int tenantId, String startDate, String endDate) {
        this.roomId = roomId;
        this.tenantId = tenantId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Room_Tenant() {

    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
