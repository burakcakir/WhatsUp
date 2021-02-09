package com.example.deneme;

public class Kullanicilar
{

    private String dogumTarihi;
    private String hakkimda;
    private String isim;
    private String resim;

    public Kullanicilar() {

    }

    public Kullanicilar(String dogumTarihi, String hakkimda, String isim, String resim) {
        this.dogumTarihi = dogumTarihi;
        this.hakkimda = hakkimda;
        this.isim = isim;
        this.resim = resim;
    }

    public String getDogumTarihi() {
        return dogumTarihi;
    }

    public void setDogumTarihi(String dogumTarihi) {
        this.dogumTarihi = dogumTarihi;
    }

    public String getHakkimda() {
        return hakkimda;
    }

    public void setHakkimda(String hakkimda) {
        this.hakkimda = hakkimda;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getResim() {
        return resim;
    }

    public void setResim(String resim) {
        this.resim = resim;
    }

    @Override
    public String toString() {
        return "Kullanicilar{" +
                "dogumTarihi='" + dogumTarihi + '\'' +
                ", hakkimda='" + hakkimda + '\'' +
                ", isim='" + isim + '\'' +
                ", resim='" + resim + '\'' +
                '}';
    }
}

