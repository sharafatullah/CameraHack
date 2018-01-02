package com.automaticallytakepicture.SendingMail;

/**
 * Created by Sunil on 4/24/2017.
 */

public class Config {
    String EMAIL;
    String PASSWORD;

    public Config(String EMAIL,String PASSWORD){

        this.EMAIL=EMAIL;
        this.PASSWORD=PASSWORD;
    }
    public Config(){

    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }
}
