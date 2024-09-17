package com.example.quanlyphongtro.pojo;

public class RoomNumber_RoomTypeName {
    private String typeName;
    private double price;

    public RoomNumber_RoomTypeName(String typeName, double price) {
        this.typeName = typeName;
        this.price = price;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
