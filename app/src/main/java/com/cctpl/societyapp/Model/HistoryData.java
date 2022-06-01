package com.cctpl.societyapp.Model;

public class HistoryData {

    String OwnerName;
    String VehicleDetails;
    String VehicleNumber;
    String MobileNumber;
    String TimeStamp;
    String STATUS;
    String Vehicle;

    public HistoryData() {
    }

    public HistoryData(String ownerName, String vehicleDetails, String vehicleNumber,
                       String mobileNumber, String timeStamp, String status, String vehicle) {
        OwnerName = ownerName;
        VehicleDetails = vehicleDetails;
        VehicleNumber = vehicleNumber;
        MobileNumber = mobileNumber;
        TimeStamp = timeStamp;
        STATUS = status;
        Vehicle = vehicle;
    }

    public String getOwnerName() {
        return OwnerName;
    }

    public void setOwnerName(String ownerName) {
        OwnerName = ownerName;
    }

    public String getVehicleDetails() {
        return VehicleDetails;
    }

    public void setVehicleDetails(String vehicleDetails) {
        VehicleDetails = vehicleDetails;
    }

    public String getVehicleNumber() {
        return VehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        VehicleNumber = vehicleNumber;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getVehicle() {
        return Vehicle;
    }

    public void setVehicle(String vehicle) {
        Vehicle = vehicle;
    }
}
