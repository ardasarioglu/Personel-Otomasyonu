package com.example.demo.nesneler;

public class Toplanti {
    private String saat;
    private String departman;

    public Toplanti(String saat, String departman) {
        this.saat = saat;
        this.departman = departman;
    }

    public String getSaat() {
        return saat;
    }

    public String getDepartman() {
        return departman;
    }
}
