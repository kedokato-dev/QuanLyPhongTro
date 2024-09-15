package com.example.quanlyphongtro.Model;

public class UserPOJO {
    private String fullName;
    private String phone;
    private String Email;
    private String identityCard;

    public UserPOJO(String fullName, String phone, String email, String identityCard) {
        this.fullName = fullName;
        this.phone = phone;
        this.Email = email;
        this.identityCard = identityCard;
    }

    public UserPOJO() {
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
        this.Email = email;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }
}
