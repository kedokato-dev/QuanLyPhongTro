package com.example.quanlyphongtro.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Tenant")
public class Tenant {
    @PrimaryKey(autoGenerate = true)
    private int tenantId;
    private String fullName;
    private String phone;
    private String Email;
    private String identityCard;

    public Tenant(){

    }
    public Tenant(int tenantId, String fullName, String phone, String email, String identityCard) {
        this.tenantId = tenantId;
        this.fullName = fullName;
        this.phone = phone;
        Email = email;
        this.identityCard = identityCard;
    }

    public Tenant( String fullName, String phone, String email, String identityCard) {
        this.fullName = fullName;
        this.phone = phone;
        Email = email;
        this.identityCard = identityCard;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }
}
