package com.example.quanlyphongtro.model;

public class MenuItem {
    private int resourceId;
    private String nameMenu;
    private String subNameMenu;

    public MenuItem(int resourceId, String nameMenu, String subNameMenu) {
        this.resourceId = resourceId;
        this.nameMenu = nameMenu;
        this.subNameMenu = subNameMenu;
    }

    public MenuItem(){

    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getNameMenu() {
        return nameMenu;
    }

    public void setNameMenu(String nameMenu) {
        this.nameMenu = nameMenu;
    }

    public String getSubNameMenu() {
        return subNameMenu;
    }

    public void setSubNameMenu(String subNameMenu) {
        this.subNameMenu = subNameMenu;
    }
}
