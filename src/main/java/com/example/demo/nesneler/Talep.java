package com.example.demo.nesneler;

public class Talep {
    private String type;
    private String value;
    private String userId;
    private String isim;
    private String soyisim;

    public Talep(String type, String value, String userId, String isim, String soyisim) {
        this.type = type;
        this.value = value;
        this.userId = userId;
        this.isim = isim;
        this.soyisim = soyisim;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String getUserId() {
        return userId;
    }

    public String getIsim() {
        return isim;
    }

    public String getSoyisim() {
        return soyisim;
    }

    /**
     * ChoiceDialog'ta otomatik olarak "Talep kodu" yerine
     * "3 gün izin" veya "%6 maaş zammı" görünmesi için:
     */
    @Override
    public String toString() {
        if ("zam".equalsIgnoreCase(this.type)) {
            return "%" + this.value + " maaş zammı";
        } else {
            return this.value + " gün izin";
        }
    }
}