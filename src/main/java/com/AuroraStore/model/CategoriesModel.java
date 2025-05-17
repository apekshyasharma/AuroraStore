package com.AuroraStore.model;
public class CategoriesModel{
    private int category_id;
    private String category_name;

    public CategoriesModel(String category_name) {
        super();
        this.category_name = category_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
    
    public int getCategory_id() {
        return category_id;
    }
    
    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public CategoriesModel() {
        super();
        // TODO Auto-generated constructor stub
    }
    
}