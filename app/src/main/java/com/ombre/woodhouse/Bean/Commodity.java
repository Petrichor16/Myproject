package com.ombre.woodhouse.Bean;

import java.util.Date;

/**
 * Merchandise entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Commodity implements java.io.Serializable {

	// Fields

	private Integer id;
	private String merName;//商品名
	private Double price;//价格
	private String picture;//图片地址
	private String merDesc;//商品简介
	private String  status;//商品状态
	private Integer sale_value;//销量
	private Integer stock;//库存
	private String firstClassification;//一级分类
	private String secondClassification;//二级分类

	public Commodity() {
	}

	public Commodity(Integer id, String merName, Double price, String picture, String merDesc, String status, Integer sale_value, Integer stock, String firstClassification, String secondClassification) {
		this.id = id;
		this.merName = merName;
		this.price = price;
		this.picture = picture;
		this.merDesc = merDesc;
		this.status = status;
		this.sale_value = sale_value;
		this.stock = stock;
		this.firstClassification = firstClassification;
		this.secondClassification = secondClassification;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMerName() {
		return merName;
	}

	public void setMerName(String merName) {
		this.merName = merName;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getMerDesc() {
		return merDesc;
	}

	public void setMerDesc(String merDesc) {
		this.merDesc = merDesc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getSale_value() {
		return sale_value;
	}

	public void setSale_value(Integer sale_value) {
		this.sale_value = sale_value;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public String getFirstClassification() {
		return firstClassification;
	}

	public void setFirstClassification(String firstClassification) {
		this.firstClassification = firstClassification;
	}

	public String getSecondClassification() {
		return secondClassification;
	}

	public void setSecondClassification(String secondClassification) {
		this.secondClassification = secondClassification;
	}
}