package edu.bbte.idde.scim2304.backend.model;

import java.util.Date;

public class Advert extends BaseEntity {
    private String name;
    private String brand;
    private int year;
    private float price;
    private Date uploadDate;

    public Advert() {
        super();
    }

    public Advert(String name, String brand, int year, float price, Date uploadDate) {
        super();
        this.name = name;
        this.brand = brand;
        this.year = year;
        this.price = price;
        this.uploadDate = uploadDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    @Override
    public String toString() {
        return "Advert{"
                +
                "name='" + name + '\''
                +
                ", brand='" + brand + '\''
                +
                ", year=" + year
                +
                ", price=" + price
                +
                ", publicationDate="
                +
                uploadDate
                +
                '}';
    }
}
