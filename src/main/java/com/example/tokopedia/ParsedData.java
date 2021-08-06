package com.example.tokopedia;

public class ParsedData {
    private int index;
    private String name;
    private String desc;
    private String img;
    private String price;
    private String rating;
    private String store;

    public ParsedData(int index, String name, String desc, String img, String price, String rating, String store) {
        this.index = index;
        this.name = name;
        this.desc = desc;
        this.img = img;
        this.price = price;
        this.rating = rating;
        this.store = store;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
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

    public String getStore() {
        return store;
    }
}
