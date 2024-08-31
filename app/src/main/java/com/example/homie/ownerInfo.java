package com.example.homie;

public class ownerInfo {
    String ownerID;
    String name;
    String vehicleNumber;
    String number;

    public ownerInfo(){

    }

    public ownerInfo(String ownerID, String name, String vehicleNumber, String number) {
        this.ownerID = ownerID;
        this.name = name;
        this.vehicleNumber = vehicleNumber;
        this.number = number;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public String getName() {
        return name;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getNumber() {
        return number;
    }
}
