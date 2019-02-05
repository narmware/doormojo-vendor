package com.narmware.doormojovendor.pojo;

/**
 * Created by rohitsavant on 11/08/18.
 */

public class OrderResponse {

    String response;
    Order[] data;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Order[] getData() {
        return data;
    }

    public void setData(Order[] data) {
        this.data = data;
    }
}
