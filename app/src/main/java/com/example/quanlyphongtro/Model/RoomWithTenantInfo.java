package com.example.quanlyphongtro.Model;

public class RoomWithTenantInfo {
    private String roomNumber;
    private String status;
    private int userNumber;

    public RoomWithTenantInfo(String roomNumber, String status, int userNumber) {
        this.roomNumber = roomNumber;
        this.status = status;
        this.userNumber = userNumber;
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

    public int getUserNumber() {
        return userNumber;
    }

    public void setNumberUser(int numberUser) {
        this.userNumber = numberUser;
    }
}
