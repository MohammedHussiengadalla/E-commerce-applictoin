package com.example.test.Model;

public class OrderDetails {
    private int orderId,ProId,quantity;


    public OrderDetails() {
    }

    public OrderDetails(int orderId, int proId, int quantity) {
        this.orderId = orderId;
        ProId = proId;
        this.quantity = quantity;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProId() {
        return ProId;
    }

    public void setProId(int proId) {
        ProId = proId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
