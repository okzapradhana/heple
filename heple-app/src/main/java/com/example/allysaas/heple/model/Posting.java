package com.example.allysaas.heple.model;

import java.io.Serializable;

public class Posting implements Serializable {
    public String memberId;
    public String namaPenjual;
    public String alamat;
    public String lokasiBerjualan;
    public String petaLokasi;
    public String noHp;
    public String statusBerjualan;
    public String deskripsi;
    public long time;
    public String photoUrl;

    public String getMemberId() {
        return memberId;
    }

    public String getNamaPenjual() {
        return namaPenjual;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getLokasiBerjualan() {
        return lokasiBerjualan;
    }

    public void setLokasiBerjualan(String lokasiBerjualan) {
        this.lokasiBerjualan = lokasiBerjualan;
    }

    public String getPetaLokasi() {
        return petaLokasi;
    }

    public void setPetaLokasi(String petaLokasi) {
        this.petaLokasi = petaLokasi;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getStatusBerjualan() {
        return statusBerjualan;
    }

    public void setStatusBerjualan(String statusBerjualan) {
        this.statusBerjualan = statusBerjualan;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Posting(){

    }

    public Posting( String memberId, String namaPenjual, String alamat, String lokasiBerjualan, String petaLokasi, String noHp, String statusBerjualan, String deskripsi, String photoUrl) {
        this.memberId = memberId;
        this.namaPenjual = namaPenjual;
        this.alamat = alamat;
        this.lokasiBerjualan = lokasiBerjualan;
        this.petaLokasi = petaLokasi;
        this.noHp = noHp;
        time = System.currentTimeMillis()/1000;
        this.statusBerjualan = statusBerjualan;
        this.deskripsi = deskripsi;
        this.photoUrl = photoUrl;
    }
}
