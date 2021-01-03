package com.justfoodzorderreceivers.Model;

public class OrderList {

    String id,restaurant_Logo,customer_name,orderid,
            BookingID,order_status_msg,order_time,order_date,ordPrice,order_type_img,order_status_color_code
            ,FoodOrderType,restaurant_address,payment_mode;

    public OrderList(String id,String restaurant_Logo,String customer_name,String orderid,
                     String BookingID,String order_status_msg,String order_time,String order_date,String ordPrice,String order_type_img,
                     String order_status_color_code,String FoodOrderType,String restaurant_address,String payment_mode)
    {
        this.id = id;
        this.restaurant_Logo = restaurant_Logo;
        this.customer_name = customer_name;
        this.orderid = orderid;
        this.BookingID = BookingID;
        this.order_status_msg = order_status_msg;
        this.order_time = order_time;
        this.order_date = order_date;
        this.ordPrice = ordPrice;
        this.order_type_img = order_type_img;
        this.order_status_color_code = order_status_color_code;
        this.FoodOrderType = FoodOrderType;
        this.restaurant_address = restaurant_address;
        this.payment_mode = payment_mode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrdPrice() {
        return ordPrice;
    }

    public void setOrdPrice(String ordPrice) {
        this.ordPrice = ordPrice;
    }

    public String getOrder_type_img() {
        return order_type_img;
    }

    public void setOrder_type_img(String order_type_img) {
        this.order_type_img = order_type_img;
    }

    public String getOrder_status_color_code() {
        return order_status_color_code;
    }

    public void setOrder_status_color_code(String order_status_color_code) {
        this.order_status_color_code = order_status_color_code;
    }

    public String getRestaurant_address() {
        return restaurant_address;
    }

    public void setRestaurant_address(String restaurant_address) {
        this.restaurant_address = restaurant_address;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getRestaurant_Logo() {
        return restaurant_Logo;
    }

    public void setRestaurant_Logo(String restaurant_Logo) {
        this.restaurant_Logo = restaurant_Logo;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getBookingID() {
        return BookingID;
    }

    public void setBookingID(String bookingID) {
        BookingID = bookingID;
    }

    public String getOrder_status_msg() {
        return order_status_msg;
    }

    public void setOrder_status_msg(String order_status_msg) {
        this.order_status_msg = order_status_msg;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }


    public String getordPrice() {
        return ordPrice;
    }

    public void setordPrice(String ordPrice) {
        this.ordPrice = ordPrice;
    }

    public String getorder_type_img() {
        return order_type_img;
    }

    public void setorder_type_img(String order_type_img) {
        this.order_type_img = order_type_img;
    }

    public String getorder_status_color_code() {
        return order_status_color_code;
    }

    public void setorder_status_color_code(String order_status_color_code) {
        this.order_status_color_code = order_status_color_code;
    }

    public String getFoodOrderType() {
        return FoodOrderType;
    }

    public void setFoodOrderType(String FoodOrderType) {
        this.FoodOrderType = FoodOrderType;
    }

}

