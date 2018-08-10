package com.narmware.doormojovendor.pojo;

/**
 * Created by rohitsavant on 08/08/18.
 */

public class Order {

    public Order(String id, String name, String category, String timestamp) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.timestamp = timestamp;
    }

    public String id, name, category, timestamp, image;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getImage() {
        return image;
    }
}
