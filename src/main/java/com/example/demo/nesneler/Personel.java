package com.example.demo.nesneler;

public class Personel extends Calisan {
    private String departman;
    private String pozisyon;
    private double maas;
    private int izin_gunu;

    public Personel(int id, String isim, String soyisim, String eposta, String sifre, long telefon, String departman, String pozisyon, double maas, int izin_gunu) {
        super(id, isim, soyisim, eposta, sifre, telefon);
        this.departman = departman;
        this.pozisyon = pozisyon;
        this.maas = maas;
        this.izin_gunu = izin_gunu;
    }

    public String getDepartman() {
        return departman;
    }

    public void setDepartman(String departman) {
        this.departman = departman;
    }

    public String getPozisyon() {
        return pozisyon;
    }

    public void setPozisyon(String pozisyon) {
        this.pozisyon = pozisyon;
    }

    public double getMaas() {
        return maas;
    }

    public void setMaas(double maas) {
        this.maas = maas;
    }

    public int getIzin_gunu() {
        return izin_gunu;
    }

    public void setIzin_gunu(int izin_gunu) {
        this.izin_gunu = izin_gunu;
    }
}
