package com.example.quanlyphongtro.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Bill", foreignKeys = {
        @ForeignKey(entity = Room.class, parentColumns = "roomId", childColumns = "roomId"),
        @ForeignKey(entity = Tenant.class, parentColumns = "tenantId", childColumns = "tenantId")
})
public class Bill {
    @PrimaryKey(autoGenerate = true)
    private int billId;
    private int roomId;
    private int tenantId;
    private String issueDate;
    private double totalAmount;
    private String status;

    @Ignore
    public Bill(int billId, int roomId, int tenantId, String issueDate, double totalAmount, String status) {
        this.billId = billId;
        this.roomId = roomId;
        this.tenantId = tenantId;
        this.issueDate = issueDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public Bill( int roomId, int tenantId, String issueDate, double totalAmount, String status) {
        this.roomId = roomId;
        this.tenantId = tenantId;
        this.issueDate = issueDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }



    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
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

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
