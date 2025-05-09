package com.AuroraStore.model;
public class ProductsModel{
	private String product_name;
	private double product_price;
	private String product_description;
	private String product_status;
	private int product_quantity;
	private int category_id;
	private int brand_id;
	private String category_name;
	private String brand_name;
	private int product_id;
	
	public ProductsModel(String product_name, float product_price, String product_description, String product_status,
			int product_quantity, int category_id, int brand_id, String category_name, String brand_name, int product_id) {
		super();
		this.product_name = product_name;
		this.product_price = product_price;
		this.product_description = product_description;
		this.product_status = product_status;
		this.product_quantity = product_quantity;
		this.category_id=category_id;
		this.brand_id=brand_id;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public double getProduct_price() {
		return product_price;
	}
	public void setProduct_price(double d) {
		this.product_price = d;
	}
	public String getProduct_description() {
		return product_description;
	}
	public void setProduct_description(String product_description) {
		this.product_description = product_description;
	}
	public String getProduct_status() {
		return product_status;
	}
	public void setProduct_status(String product_status) {
		this.product_status = product_status;
	}
	public int getProduct_quantity() {
		return product_quantity;
	}
	public void setProduct_quantity(int product_quantity) {
		this.product_quantity = product_quantity;
	}
	
	public int getCategory_id() {
		return category_id;
	}
	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}
	public int getBrand_id() {
		return brand_id;
	}
	public void setBrand_id(int brand_id) {
		this.brand_id = brand_id;
	}
	
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public String getBrand_name() {
		return brand_name;
	}
	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}
	public int getProduct_id() {
		return product_id;
	}
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	public ProductsModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}