package com.mvp.mobile_art.Model.Basic;

/**
 * Created by Zackzack on 19/07/2017.
 */

public class UserContact {
    private Integer id;
    private User user;
    private String phone;
    private String address;
    private String location;
    private Integer province;
    private Integer city;
    private String emergency_numb;
    private String acc_no;

    public String getAcc_no() {
        return acc_no;
    }

    public void setAcc_no(String acc_no) {
        this.acc_no = acc_no;
    }

    public Integer getId() {
        return id;
    }

    public String getEmergency_numb() {
        return emergency_numb;
    }

    public void setEmergency_numb(String emergency_numb) {
        this.emergency_numb = emergency_numb;
    }

    public User getUser_id() {
        return user;
    }

    public void setUser_id(User user) {
        this.user = user;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getProvince() {
        return province;
    }

    public void setProvince(Integer province) {
        this.province = province;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }
}
