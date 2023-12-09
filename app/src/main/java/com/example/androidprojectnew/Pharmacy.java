package com.example.androidprojectnew;

public class Pharmacy {
    private String name;
    private double latitude;
    private double longitude;

    public Pharmacy() {
        // Constructeur par défaut requis pour Firebase
    }

    public Pharmacy(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "Pharmacy{" +
                "name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
// Constructeurs, getters et setters
}