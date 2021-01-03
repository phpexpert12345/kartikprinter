package com.justfoodzorderreceivers.Model;

public class HistoryModel {

    String BookingID,payment_mode,order_date,order_time,Currency,ordPrice;

    public HistoryModel(String bookingID, String payment_mode, String order_date, String order_time, String currency, String ordPrice) {
        BookingID = bookingID;
        this.payment_mode = payment_mode;
        this.order_date = order_date;
        this.order_time = order_time;
        Currency = currency;
        this.ordPrice = ordPrice;
    }

    public String getBookingID() {
        return BookingID;
    }

    public void setBookingID(String bookingID) {
        BookingID = bookingID;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public String getOrdPrice() {
        return ordPrice;
    }

    public void setOrdPrice(String ordPrice) {
        this.ordPrice = ordPrice;
    }
}
