package com.fx_designer.map.models;

/**
 * Created by Chamath Abeysinghe on 9/9/2016.
 */
public class Position {
    private double lat;
    private double longitude;
    private String address;
    private String telephoneNumber;
    private String name;
    private String category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Position(double lat, double longitude, String name,String address, String telephoneNumber,String category) {
        this.lat = lat;
        this.longitude = longitude;
        this.address = address;
        this.telephoneNumber = telephoneNumber;
        this.name=name;
        this.category=category;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
