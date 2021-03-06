package com.example.Classes.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class Pandomats {
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("lastDeviceData")
    @Expose
    private HashMap<String, Data> lastDeviceData;
    @SerializedName("image")
    @Expose
    private String image;



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public HashMap<String, Data> getLastDeviceData() {
        return lastDeviceData;
    }

    public void setLastDeviceData(HashMap<String, Data> lastDeviceData) {
        this.lastDeviceData = lastDeviceData;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}