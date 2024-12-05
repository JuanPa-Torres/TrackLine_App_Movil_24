package com.example.trackline24;

public class Ruta {
    private String createdAt;
    private String idRuta;
    private double latStart;
    private double lonStart;
    private double latFinish;
    private double lonFinish;
    private String nombreRuta;

    public Ruta(String createdAt, String idRuta, double latStart, double lonStart, double latFinish, double lonFinish, String nombreRuta) {
        this.createdAt = createdAt;
        this.idRuta = idRuta;
        this.latStart = latStart;
        this.lonStart = lonStart;
        this.latFinish = latFinish;
        this.lonFinish = lonFinish;
        this.nombreRuta = nombreRuta;
    }

    // Getters y setters
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(String idRuta) {
        this.idRuta = idRuta;
    }

    public double getLatStart() {
        return latStart;
    }

    public void setLatStart(double latStart) {
        this.latStart = latStart;
    }

    public double getLonStart() {
        return lonStart;
    }

    public void setLonStart(double lonStart) {
        this.lonStart = lonStart;
    }

    public double getLatFinish() {
        return latFinish;
    }

    public void setLatFinish(double latFinish) {
        this.latFinish = latFinish;
    }

    public double getLonFinish() {
        return lonFinish;
    }

    public void setLonFinish(double lonFinish) {
        this.lonFinish = lonFinish;
    }

    public String getNombreRuta() {
        return nombreRuta;
    }

    public void setNombreRuta(String nombreRuta) {
        this.nombreRuta = nombreRuta;
    }
}

