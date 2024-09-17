package com.example.quanlyphongtro.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Service")
public class Service {
    @PrimaryKey(autoGenerate = true)
    private int serviceId;
    private String serviceName;
    private double pricePerUnit;

    public Service(int serviceId, String serviceName, double pricePerUnit) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.pricePerUnit = pricePerUnit;
    }

    public Service(String serviceName, double pricePerUnit) {
        this.serviceName = serviceName;
        this.pricePerUnit = pricePerUnit;
    }

    public Service() {

    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
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
}
