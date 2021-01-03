package com.justfoodzorderreceivers.Model;

public class DriverList {

    int id,DriverID,RestaurantID;
    String DriverFirstName,DriverLastName,DriverEmailAddress,DriverPhoto,DriverLiencePhoto,
            DriverCountry,DriverState,DriverCity,DriverPostcode,DriverAddress,DriverCommisionCharge,
            DriverCommission_Type,customerCountry,customerState,customerCity,
            driver_vehicle_types,driver_vehicle_year,driver_vehicle_brand,driver_vehicle_model,
            DriverLicence,DriverMobileNo,created_date,status,DriverStatus;

    public DriverList(int id, int driverID, int restaurantID, String driverFirstName, String driverLastName, String driverEmailAddress, String driverPhoto, String driverLiencePhoto, String driverCountry, String driverState, String driverCity, String driverPostcode, String driverAddress, String driverCommisionCharge, String driverCommission_Type, String customerCountry, String customerState, String customerCity, String driver_vehicle_types, String driver_vehicle_year, String driver_vehicle_brand, String driver_vehicle_model, String driverLicence, String driverMobileNo, String created_date, String status, String driverStatus) {
        this.id = id;
        DriverID = driverID;
        RestaurantID = restaurantID;
        DriverFirstName = driverFirstName;
        DriverLastName = driverLastName;
        DriverEmailAddress = driverEmailAddress;
        DriverPhoto = driverPhoto;
        DriverLiencePhoto = driverLiencePhoto;
        DriverCountry = driverCountry;
        DriverState = driverState;
        DriverCity = driverCity;
        DriverPostcode = driverPostcode;
        DriverAddress = driverAddress;
        DriverCommisionCharge = driverCommisionCharge;
        DriverCommission_Type = driverCommission_Type;
        this.customerCountry = customerCountry;
        this.customerState = customerState;
        this.customerCity = customerCity;
        this.driver_vehicle_types = driver_vehicle_types;
        this.driver_vehicle_year = driver_vehicle_year;
        this.driver_vehicle_brand = driver_vehicle_brand;
        this.driver_vehicle_model = driver_vehicle_model;
        DriverLicence = driverLicence;
        DriverMobileNo = driverMobileNo;
        this.created_date = created_date;
        this.status = status;
        DriverStatus = driverStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDriverID() {
        return DriverID;
    }

    public void setDriverID(int driverID) {
        DriverID = driverID;
    }

    public int getRestaurantID() {
        return RestaurantID;
    }

    public void setRestaurantID(int restaurantID) {
        RestaurantID = restaurantID;
    }

    public String getDriverFirstName() {
        return DriverFirstName;
    }

    public void setDriverFirstName(String driverFirstName) {
        DriverFirstName = driverFirstName;
    }

    public String getDriverLastName() {
        return DriverLastName;
    }

    public void setDriverLastName(String driverLastName) {
        DriverLastName = driverLastName;
    }

    public String getDriverEmailAddress() {
        return DriverEmailAddress;
    }

    public void setDriverEmailAddress(String driverEmailAddress) {
        DriverEmailAddress = driverEmailAddress;
    }

    public String getDriverPhoto() {
        return DriverPhoto;
    }

    public void setDriverPhoto(String driverPhoto) {
        DriverPhoto = driverPhoto;
    }

    public String getDriverLiencePhoto() {
        return DriverLiencePhoto;
    }

    public void setDriverLiencePhoto(String driverLiencePhoto) {
        DriverLiencePhoto = driverLiencePhoto;
    }

    public String getDriverCountry() {
        return DriverCountry;
    }

    public void setDriverCountry(String driverCountry) {
        DriverCountry = driverCountry;
    }

    public String getDriverState() {
        return DriverState;
    }

    public void setDriverState(String driverState) {
        DriverState = driverState;
    }

    public String getDriverCity() {
        return DriverCity;
    }

    public void setDriverCity(String driverCity) {
        DriverCity = driverCity;
    }

    public String getDriverPostcode() {
        return DriverPostcode;
    }

    public void setDriverPostcode(String driverPostcode) {
        DriverPostcode = driverPostcode;
    }

    public String getDriverAddress() {
        return DriverAddress;
    }

    public void setDriverAddress(String driverAddress) {
        DriverAddress = driverAddress;
    }

    public String getDriverCommisionCharge() {
        return DriverCommisionCharge;
    }

    public void setDriverCommisionCharge(String driverCommisionCharge) {
        DriverCommisionCharge = driverCommisionCharge;
    }

    public String getDriverCommission_Type() {
        return DriverCommission_Type;
    }

    public void setDriverCommission_Type(String driverCommission_Type) {
        DriverCommission_Type = driverCommission_Type;
    }

    public String getCustomerCountry() {
        return customerCountry;
    }

    public void setCustomerCountry(String customerCountry) {
        this.customerCountry = customerCountry;
    }

    public String getCustomerState() {
        return customerState;
    }

    public void setCustomerState(String customerState) {
        this.customerState = customerState;
    }

    public String getCustomerCity() {
        return customerCity;
    }

    public void setCustomerCity(String customerCity) {
        this.customerCity = customerCity;
    }

    public String getDriver_vehicle_types() {
        return driver_vehicle_types;
    }

    public void setDriver_vehicle_types(String driver_vehicle_types) {
        this.driver_vehicle_types = driver_vehicle_types;
    }

    public String getDriver_vehicle_year() {
        return driver_vehicle_year;
    }

    public void setDriver_vehicle_year(String driver_vehicle_year) {
        this.driver_vehicle_year = driver_vehicle_year;
    }

    public String getDriver_vehicle_brand() {
        return driver_vehicle_brand;
    }

    public void setDriver_vehicle_brand(String driver_vehicle_brand) {
        this.driver_vehicle_brand = driver_vehicle_brand;
    }

    public String getDriver_vehicle_model() {
        return driver_vehicle_model;
    }

    public void setDriver_vehicle_model(String driver_vehicle_model) {
        this.driver_vehicle_model = driver_vehicle_model;
    }

    public String getDriverLicence() {
        return DriverLicence;
    }

    public void setDriverLicence(String driverLicence) {
        DriverLicence = driverLicence;
    }

    public String getDriverMobileNo() {
        return DriverMobileNo;
    }

    public void setDriverMobileNo(String driverMobileNo) {
        DriverMobileNo = driverMobileNo;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDriverStatus() {
        return DriverStatus;
    }

    public void setDriverStatus(String driverStatus) {
        DriverStatus = driverStatus;
    }


}
