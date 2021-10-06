package com.example.ambulance;

public class model {

    String email,uName,phn;

    model(){}


    public model(String uName, String email,String phn) {
        this.uName = uName;
        this.email = email;
        this.phn = phn;
    }

    public String getEmail() {
        return email;
    }

    public String getPhn() {
        return phn;
    }

    public void setPhn(String phn) {
        this.phn = phn;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }
}

