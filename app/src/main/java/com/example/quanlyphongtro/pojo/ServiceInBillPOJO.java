package com.example.quanlyphongtro.pojo;

public class ServiceInBillPOJO {
    private String serviceName;
    private double pricePerUnit;
    private int quantity;
    private double amount;

    public ServiceInBillPOJO(String serviceName, double pricePerUnit, int quantity, double amount) {
        this.serviceName = serviceName;
        this.pricePerUnit = pricePerUnit;
        this.quantity = quantity;
        this.amount = amount;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
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
