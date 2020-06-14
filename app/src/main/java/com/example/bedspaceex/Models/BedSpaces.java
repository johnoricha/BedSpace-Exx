package com.example.bedspaceex.Models;

import java.io.Serializable;

public class BedSpaces implements Serializable {

    private String ownerName;
    private String roomNumber;
    private String hall;
    private String phoneNumber;
    private String price;
    private String id;
    private String imageUrl;

    public BedSpaces(){

    }

//    public BedSpaces(String id) {
//        this.id = id;
//    }

    public BedSpaces(String ownerName, String roomNumber, String hall, String phoneNumber, String price, String id) {
        this.ownerName = ownerName;
        this.roomNumber = roomNumber;
        this.hall = hall;
        this.phoneNumber = phoneNumber;
        this.price = price;
        this.id = id;
    }


//    public BedSpaces(String ownerName, String roomNumber, String hall, String phoneNumber, String price) {
//        this.ownerName = ownerName;
//        this.roomNumber = roomNumber;
//        this.hall = hall;
//        this.phoneNumber = phoneNumber;
//        this.price = price;
//        this.id = id;
//    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getHall() {
        return hall;
    }

    public void setHall(String hall) {
        this.hall = hall;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
