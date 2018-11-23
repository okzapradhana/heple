package com.example.allysaas.heple.model;

public class Komentar {

    public String memberId;
    public String isiKomentar;
    public Long time;

    public String getMemberId() {
        return memberId;
    }

    public Long getTime() {
        return time;
    }

    public String getIsiKomentar() {
        return isiKomentar;
    }

    public Komentar() {
    }

    public Komentar(String memberId, String isiKomentar, Long time) {
        this.memberId = memberId;
        this.isiKomentar = isiKomentar;
        this.time = time;
    }
}
