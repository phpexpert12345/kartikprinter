package com.justfoodzorderreceivers.Model;

public class NotificationList {

    String id,orderid,BookingID,order_reference_number,number_of_items_order,
            OrderNotificationMessage,error;

    public NotificationList(String id,String orderid,String BookingID,String order_reference_number,String number_of_items_order,
                            String OrderNotificationMessage,String error)
    {
        this.id = id;
        this.orderid = orderid;
        this.BookingID = BookingID;
        this.order_reference_number = order_reference_number;
        this.number_of_items_order = number_of_items_order;
        this.OrderNotificationMessage = OrderNotificationMessage;
        this.OrderNotificationMessage = OrderNotificationMessage;
        this.error = error;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getOrder_reference_number() {
        return order_reference_number;
    }

    public void setOrder_reference_number(String order_reference_number) {
        this.order_reference_number = order_reference_number;
    }

    public String getNumber_of_items_order() {
        return number_of_items_order;
    }

    public void setNumber_of_items_order(String number_of_items_order) {
        this.number_of_items_order = number_of_items_order;
    }

    public String getOrderNotificationMessage() {
        return OrderNotificationMessage;
    }

    public void setOrderNotificationMessage(String orderNotificationMessage) {
        OrderNotificationMessage = orderNotificationMessage;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }




}
