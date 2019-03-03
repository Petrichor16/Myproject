package com.ombre.woodhouse.Bean;


import java.sql.Timestamp;

//订单表
public class Orders {
    private Integer id;
    private String addressId;//地址
    private String adName;//收货人姓名
    private String adTelphone;//收货人电话
    private String orderNo;//订单号
    private String orderDate;//订单时间
    private Integer orderStatus;//订单状态

    public Orders() {
    }

    public Orders(Integer id, String addressId, String adName, String adTelphone, String orderNo, String orderDate, Integer orderStatus) {
        this.id = id;
        this.addressId = addressId;
        this.adName = adName;
        this.adTelphone = adTelphone;
        this.orderNo = orderNo;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public String getAdTelphone() {
        return adTelphone;
    }

    public void setAdTelphone(String adTelphone) {
        this.adTelphone = adTelphone;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }
}
