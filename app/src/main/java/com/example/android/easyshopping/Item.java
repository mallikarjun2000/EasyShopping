package com.example.android.easyshopping;

public class Item {
    private String name;
    private String brand;
    private String expirydate;
    private String img;
    private String price;
    private String rating;
    private String review;


    public Item(String name , String brand , String expirydate ,String img, String price, String rating , String review){
        this.name = name;
        this.brand = brand;
        this.expirydate= expirydate;
        this.img = img;
        this.price=price;
        this.rating=rating;
        this.review=review;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getExpirydate() {
        return expirydate;
    }

    public String getImg() {
        return img;
    }

    public String getPrice() {
        return price;
    }

    public String getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }
}
