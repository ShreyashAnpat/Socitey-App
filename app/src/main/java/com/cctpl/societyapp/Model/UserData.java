package com.cctpl.societyapp.Model;

public class UserData {

    String OwnerName;
    String BuildingNumber;
    String RoomNumber;
    String OwnerType;
    String VehicleDetails;
    String VehicleNumber;
    String MobileNumber;
    String TimeStamp;
    String Verify;
    String Vehicle;

    public UserData() {
    }

    public UserData(String ownerName, String buildingNumber, String roomNumber, String ownerType, String vehicleDetails, String vehicleNumber, String mobileNumber, String timeStamp, String verify, String vehicle) {
        OwnerName = ownerName;
        BuildingNumber = buildingNumber;
        RoomNumber = roomNumber;
        OwnerType = ownerType;
        VehicleDetails = vehicleDetails;
        VehicleNumber = vehicleNumber;
        MobileNumber = mobileNumber;
        TimeStamp = timeStamp;
        Verify = verify;
        Vehicle = vehicle;
    }

    public String getOwnerName() {
        return OwnerName;
    }

    public void setOwnerName(String ownerName) {
        OwnerName = ownerName;
    }

    public String getBuildingNumber() {
        return BuildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        BuildingNumber = buildingNumber;
    }

    public String getRoomNumber() {
        return RoomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        RoomNumber = roomNumber;
    }

    public String getOwnerType() {
        return OwnerType;
    }

    public void setOwnerType(String ownerType) {
        OwnerType = ownerType;
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

    public String getVerify() {
        return Verify;
    }

    public void setVerify(String verify) {
        Verify = verify;
    }

    public String getVehicle() {
        return Vehicle;
    }

    public void setVehicle(String vehicle) {
        Vehicle = vehicle;
    }
}
