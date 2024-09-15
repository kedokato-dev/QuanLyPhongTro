package com.example.quanlyphongtro.Model;

public class ServicePOJO {
    private String serviceName;
    private double pricePerUnit;

    public ServicePOJO(String serviceName, double pricePerUnit) {
        this.serviceName = serviceName;
        this.pricePerUnit = pricePerUnit;
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
