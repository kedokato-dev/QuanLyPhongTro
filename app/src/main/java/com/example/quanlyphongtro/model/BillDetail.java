package com.example.quanlyphongtro.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "BillDetail", foreignKeys ={
        @ForeignKey(entity = Bill.class, parentColumns = "billId", childColumns = "billId"),
        @ForeignKey(entity = Service.class, parentColumns = "serviceId", childColumns = "serviceId")
})
public class BillDetail {
    @PrimaryKey(autoGenerate = true)
    private int billDetailId;
    private int billId;
    private int serviceId;
    private int quantity;
    private double amount;

    public BillDetail(int billDetailId, int billId, int serviceId, int quantity, double amount) {
        this.billDetailId = billDetailId;
        this.billId = billId;
        this.serviceId = serviceId;
        this.quantity = quantity;
        this.amount = amount;
    }

    @Ignore
    public BillDetail( int billId, int serviceId, int quantity, double amount) {
        this.billId = billId;
        this.serviceId = serviceId;
        this.quantity = quantity;
        this.amount = amount;
    }

    public int getBillDetailId() {
        return billDetailId;
    }

    public void setBillDetailId(int billDetailId) {
        this.billDetailId = billDetailId;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
