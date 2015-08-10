package com.example.nem.recycle.app;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Alexandra on 05.08.2015.
 */
public class Person {
    private String id;
    private boolean isActive;
    private String picture;
    private int age;
    private String name;
    private String gender;
    private String email;
    private String phone;
    private String address;
    private Date registered;



    public String getId() {
        return id;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getPicture() {
        return picture;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public Date getRegistered() {
        return registered;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setPicture(String pictureUrl) {
        this.picture = pictureUrl;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRegistered(Date registered) {
        this.registered = registered;
    }
}
