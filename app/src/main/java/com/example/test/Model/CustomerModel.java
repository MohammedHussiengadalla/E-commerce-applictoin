package com.example.test.Model;

public class CustomerModel {

    private int id;
    private String custName,mail, password, gender, custJop,custBirthDate;

    public CustomerModel() {
    }

    public CustomerModel( String custName, String mail,String password, String gender, String custJop, String custBirthDate) {
        this.custName = custName;
        this.mail=mail;
        this.password = password;
        this.gender = gender;
        this.custJop = custJop;
        this.custBirthDate = custBirthDate;
    }

    public int getId() {
        return id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCustJop() {
        return custJop;
    }

    public void setCustJop(String custJop) {
        this.custJop = custJop;
    }

    public String getCustBirthDate() {
        return custBirthDate;
    }

    public void setCustBirthDate(String custBirthDate) {
        this.custBirthDate = custBirthDate;
    }
}
