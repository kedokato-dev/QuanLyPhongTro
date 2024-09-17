package com.example.quanlyphongtro.pojo;

public class ItemBillPOJO {
    private String roomNumber;
    private String issueDate;
    private double totalAmount;
    private String status;

    public ItemBillPOJO(String roomNumber, String issueDate, double totalAmount, String status) {
        this.roomNumber = roomNumber;
        this.issueDate = issueDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public ItemBillPOJO() {

    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
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
