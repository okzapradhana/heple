package com.example.allysaas.heple.model;

import java.util.HashMap;
import java.util.Map;

public class Post {
    public String postId;
    public String authorId;
    public String nama;
    public String authorMail;
    public String alamat;
    public String lokasiBerjualan;
    public String petaLokasi;
    public String noHp;
    public String statusBerjualan;
    public String deskripsi;
    public Map<String, Object> komentar = new HashMap<>();
    public int like;
    public Long time;
    public String photoUrl;

    public Map<String, Object> getKomentar() {
        return komentar;
    }

    public void setKomentar(Map<String, Object> komentar) {
        this.komentar = komentar;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getAuthorMail() {
        return authorMail;
    }

    public void setAuthorMail(String authorMail) {
        this.authorMail = authorMail;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
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

    public Post(){

    }

    public Post(int like){
        this.like = like;
    }

    //Constructor for Set Value to Firebase//
    public Post(String postId, String authorId, String authorMail, String nama, String alamat, String lokasiBerjualan, String petaLokasi, String noHp, String statusBerjualan, String deskripsi, String photoUrl) {
        this.postId = postId;
        this.authorId = authorId;
        this.authorMail = authorMail;
        this.nama = nama;
        this.alamat = alamat;
        this.lokasiBerjualan = lokasiBerjualan;
        this.petaLokasi = petaLokasi;
        this.noHp = noHp;
        time = System.currentTimeMillis()/1000;
        this.statusBerjualan = statusBerjualan;
        this.deskripsi = deskripsi;
        this.photoUrl = photoUrl;
    }

    //Constructor for Adapter//
    public Post(String postId, String authorId, String authorMail, String nama, String alamat, String lokasiBerjualan, String petaLokasi, String noHp, Long time, String statusBerjualan, String deskripsi, String photoUrl, int like) {
        this.postId = postId;
        this.authorId = authorId;
        this.authorMail = authorMail;
        this.nama = nama;
        this.alamat = alamat;
        this.lokasiBerjualan = lokasiBerjualan;
        this.petaLokasi = petaLokasi;
        this.noHp = noHp;
        this.time = time;
        this.statusBerjualan = statusBerjualan;
        this.deskripsi = deskripsi;
        this.photoUrl = photoUrl;
        this.like = like;
    }

    public Post(String authorId, String photoUrl, Long time, String deskripsi){
        this.authorId = authorId;
        this.photoUrl = photoUrl;
        this.time = time;
        this.deskripsi = deskripsi;
    }


}
