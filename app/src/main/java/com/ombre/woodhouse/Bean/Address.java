package com.ombre.woodhouse.Bean;

/**
 * Created by OMBRE on 2017/12/5.
 */
//地址类
public class Address {
    private Integer id;
    private String account_num;//账户名
    private String username;//姓名
    private String user_phone;//收货电话
    private String area;//省

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    private String address_remark;//地址的备注

    public String getAccount_num() {
        return account_num;
    }

    public void setAccount_num(String account_num) {
        this.account_num = account_num;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress_remark() {
        return address_remark;
    }

    public void setAddress_remark(String address_remark) {
        this.address_remark = address_remark;
    }

    public Address(Integer id, String account_num, String username, String user_phone, String area, String address_remark) {
        this.id = id;
        this.account_num = account_num;
        this.username = username;
        this.user_phone = user_phone;
        this.area = area;
        this.address_remark = address_remark;
    }

    public Address(String account_num, String username, String user_phone, String area, String address_remark) {
    
        this.account_num = account_num;
        this.username = username;
        this.user_phone = user_phone;
        this.area = area;
        this.address_remark = address_remark;
    }

    public Address() {
    
    }
}