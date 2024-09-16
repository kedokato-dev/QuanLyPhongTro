package com.example.quanlyphongtro.model;

public class DetailRoomPOJO {
    private String roomNumber;
    private String status;
    private double price;
    private String typeName;
    private String description;

    public  DetailRoomPOJO(){

    }

    public DetailRoomPOJO(String roomNumber, String status, double price, String typeName, String description) {
        this.roomNumber = roomNumber;
        this.status = status;
        this.price = price;
        this.typeName = typeName;
        this.description = description;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
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
}
